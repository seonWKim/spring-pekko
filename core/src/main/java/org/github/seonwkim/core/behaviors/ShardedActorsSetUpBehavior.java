package org.github.seonwkim.core.behaviors;

import org.apache.pekko.actor.typed.ActorSystem;

public class ShardedActorsSetUpBehavior {
  public static void create(ActorSystem<?> system) {
    // register sharded entities
    system.log().info("Sharded actors registered");
  }
}
