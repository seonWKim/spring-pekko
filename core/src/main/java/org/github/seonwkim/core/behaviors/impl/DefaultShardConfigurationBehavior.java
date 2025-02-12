package org.github.seonwkim.core.behaviors.impl;

import org.apache.pekko.actor.typed.ActorSystem;
import org.github.seonwkim.core.behaviors.ShardConfigurationBehavior;

public class DefaultShardConfigurationBehavior implements ShardConfigurationBehavior {
	public void create(ActorSystem<?> system) {
		system.log().info("Please implement ShardConfigurationBehavior to configure shard logic.");
	}
}
