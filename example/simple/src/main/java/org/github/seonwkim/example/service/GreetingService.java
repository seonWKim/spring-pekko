package org.github.seonwkim.example.service;

import org.github.seonwkim.core.ActorRefWrapper;
import org.github.seonwkim.core.ActorService;
import org.github.seonwkim.core.behaviors.ActorCreationBehavior;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    private final ActorService service;

    public GreetingService(ActorService service) {this.service = service;}

    public void hello() {
        ActorRefWrapper<ActorCreationBehavior> actorRefWrapper = service.getActor(
                ActorCreationBehavior.BEAN_NAME, ActorRefWrapper.class);
    }
}
