package org.github.seonwkim.core.behaviors;

import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.github.seonwkim.core.DependencyContainer;

public class DefaultNonClusterRootBehavior implements NonClusterRootBehavior {
	@Override
	public Behavior<String> create(DependencyContainer container) {
		return Behaviors.setup(
				context -> {
					context.getLog().debug("Non-cluster behavior started");

					container
							.getSingletonBehaviors()
							.forEach(
									behavior -> {
										context.spawn(behavior.create(), behavior.name());
									});

					return Behaviors.receiveMessage(
							message -> {
								context.getLog().debug("Received message: {}", message);
								return Behaviors.same();
							});
				});
	}
}
