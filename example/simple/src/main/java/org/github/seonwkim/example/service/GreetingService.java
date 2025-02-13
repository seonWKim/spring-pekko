package org.github.seonwkim.example.service;

import org.github.seonwkim.core.ActorRefWrapper;
import org.github.seonwkim.example.behaviors.WorldBehavior;
import org.github.seonwkim.example.behaviors.WorldBehavior.HelloCommand;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    private final ActorRefWrapper<WorldBehavior.Command> actorRefWrapper;

    public GreetingService(
            @Qualifier("seonwkim-world-behavior")
            ActorRefWrapper<WorldBehavior.Command> actorRefWrapper
    ) {
        this.actorRefWrapper = actorRefWrapper;
    }

    public void hello() {
        actorRefWrapper.unwrap().tell(new HelloCommand());
    }
}
