package org.github.seonwkim.core.behaviors;

import org.apache.pekko.actor.typed.ActorSystem;

public interface ShardConfigurationBehavior {
    void create(ActorSystem<?> system);
}
