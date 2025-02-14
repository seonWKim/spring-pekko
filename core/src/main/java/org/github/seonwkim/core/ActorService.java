package org.github.seonwkim.core;

import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.ActorSystem;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.AskPattern;
import org.github.seonwkim.core.behaviors.ActorCreationBehavior;
import org.github.seonwkim.core.behaviors.ActorCreationBehavior.ChildCreated;
import org.github.seonwkim.core.behaviors.ActorCreationBehavior.CreateChild;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ActorService {

    private final GenericApplicationContext genericApplicationContext;
    private final ActorSystem actorSystem;

    // lazily initialized
    private ActorRefWrapper<ActorCreationBehavior.Command> actorCreationBehavior;

    public ActorService(GenericApplicationContext genericApplicationContext, ActorSystem actorSystem) {
        this.genericApplicationContext = genericApplicationContext;
        this.actorSystem = actorSystem;
    }

    @SuppressWarnings("unchcked")
    public <T> ActorRefWrapper<T> getActorRefWrapper(String  beanName, Class<?> clazz) {
        return (ActorRefWrapper<T>) genericApplicationContext.getBean(beanName, clazz);
    }

    public <T> CompletionStage<ActorRef<T>> createActor(Supplier<Behavior<T>> behaviorSupplier, Duration timeout) {
        return AskPattern.<ActorCreationBehavior.Command, ChildCreated<T>>ask(
                actorCreationBehavior.unwrap(),
                replyTo -> new CreateChild<>(behaviorSupplier, replyTo),
                timeout,
                actorSystem.scheduler()
        ).thenApply(ChildCreated::getChildRef);
    }

    public <T> void tell(ActorRefWrapper<T> wrapper, T message) {
        wrapper.unwrap().tell(message);
    }

    public <T, R> CompletionStage<R> ask(ActorRefWrapper<T> wrapper, T message, Duration timeout) {
        ActorRef<T> actorRef = wrapper.unwrap();
        // TODO: I'm not sure whether it's okay to use actorSystem's scheduler
        return AskPattern.ask(actorRef, replyTo -> message, timeout, actorSystem.scheduler());
    }

    @EventListener(ContextRefreshedEvent.class)
    public void initializeActorCreationBehavior() {
        actorCreationBehavior = getActorRefWrapper(ActorCreationBehavior.BEAN_NAME, ActorRefWrapper.class);
    }
}
