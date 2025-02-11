package org.github.seonwkim.core.behaviors;

import java.util.List;
import org.apache.pekko.actor.typed.ActorSystem;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.cluster.typed.Cluster;

public class ClusterRootBehavior {
	public static Behavior<String> create(List<SingletonBehavior<?>> singletonBehaviors) {
		return Behaviors.setup(
				context -> {
					Cluster cluster = Cluster.get(context.getSystem());
					context.getLog().debug("Cluster behavior started at {}", cluster.selfMember().address());

					singletonBehaviors.forEach(
							behavior -> {
								// TODO: register those beans
								context.spawn(behavior.create(), behavior.name());
							});
					setUpShardedActors(context.getSystem());

					return Behaviors.receiveMessage(
							message -> {
								context.getLog().debug("Received message: {}", message);
								return Behaviors.same();
							});
				});
	}

	private static void setUpShardedActors(ActorSystem<?> system) {
		ShardedActorsSetUpBehavior.create(system);
	}
}
