package org.github.seonwkim.core.behaviors;

import org.apache.pekko.actor.typed.Behavior;
import org.springframework.context.annotation.Scope;

@Scope("singleton")
public interface SingletonBehavior<T> {
    String name();

    // TODO: maybe we could add dependency inversion box as an argument
    Behavior<T> create();
}
