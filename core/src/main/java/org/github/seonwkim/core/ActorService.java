package org.github.seonwkim.core;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.ActorSystem;
import org.apache.pekko.actor.typed.javadsl.AskPattern;
import org.github.seonwkim.core.behaviors.ActorCreationBehavior;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ActorService {

    private final GenericApplicationContext genericApplicationContext;
    private final ActorSystem actorSystem;

    // lazily initialized
    private ActorRefWrapper<ActorCreationBehavior> actorCreationBehavior;

    public ActorService(GenericApplicationContext genericApplicationContext, ActorSystem actorSystem) {
        this.genericApplicationContext = genericApplicationContext;
        this.actorSystem = actorSystem;
    }

    @SuppressWarnings("unchcked")
    public <T> ActorRefWrapper<T> getActor(String  beanName, Class<?> clazz) {
        return (ActorRefWrapper<T>) genericApplicationContext.getBean(beanName, clazz);
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
        actorCreationBehavior = getActor(ActorCreationBehavior.BEAN_NAME, ActorRefWrapper.class);
    }
}
