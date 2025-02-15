package org.github.seonwkim.core.service;

import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.ActorSystem;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.AskPattern;
import org.github.seonwkim.core.ActorRefWrapper;
import org.github.seonwkim.core.behaviors.ActorCreationBehavior;
import org.github.seonwkim.core.behaviors.ActorCreationBehavior.ChildCreated;
import org.github.seonwkim.core.behaviors.ActorCreationBehavior.Command;
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
    private ActorRefWrapper<Command> actorCreationBehavior;

    public ActorService(GenericApplicationContext genericApplicationContext, ActorSystem actorSystem) {
        this.genericApplicationContext = genericApplicationContext;
        this.actorSystem = actorSystem;
    }

    @SuppressWarnings("unchcked")
    public <REQ> ActorRefWrapper<REQ> getActorRefWrapper(String  beanName, Class<?> clazz) {
        return (ActorRefWrapper<REQ>) genericApplicationContext.getBean(beanName, clazz);
    }

    public <REQ> CompletionStage<ActorRef<REQ>> createActor(
            String childName,
            Supplier<Behavior<REQ>> behaviorSupplier,
            Duration timeout) {
        return AskPattern.<ActorCreationBehavior.Command, ChildCreated<REQ>>ask(
                actorCreationBehavior.unwrap(),
                replyTo -> new CreateChild<>(childName, behaviorSupplier, replyTo),
                timeout,
                actorSystem.scheduler()
        ).thenApply(ChildCreated::getChildRef);
    }

    public <REQ> void tell(ActorRef<REQ> actorRef, REQ message) {
        actorRef.tell(message);
    }

    public <REQ, RES> CompletionStage<RES> ask(ActorRef<REQ> actorRef, Function<ActorRef<RES>, REQ> messageFactory, Duration timeout) {
        return AskPattern.ask(
                actorRef,
                messageFactory::apply,
                timeout,
                actorSystem.scheduler()
        );
    }

    @EventListener(ContextRefreshedEvent.class)
    public void initializeActorCreationBehavior() {
        actorCreationBehavior = getActorRefWrapper(ActorCreationBehavior.BEAN_NAME, ActorRefWrapper.class);
    }
}
