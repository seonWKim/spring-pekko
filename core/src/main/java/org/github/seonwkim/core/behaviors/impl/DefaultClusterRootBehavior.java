package org.github.seonwkim.core.behaviors.impl;

import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.cluster.typed.Cluster;
import org.github.seonwkim.core.DependencyContainer;
import org.github.seonwkim.core.behaviors.ClusterRootBehavior;

public class DefaultClusterRootBehavior implements ClusterRootBehavior {
    @Override
    public Behavior<String> create(DependencyContainer container) {
        return Behaviors.setup(
                context -> {
                    Cluster cluster = Cluster.get(context.getSystem());
                    context.getLog().debug("Cluster behavior started at {}", cluster.selfMember().address());

                    container
                            .getSingletonBehaviors()
                            .forEach(
                                    behavior -> {
                                        // TODO: register those beans
                                        context.spawn(behavior.create(), behavior.name());
                                    });
                    container.getShardConfigurationBehavior().create(context.getSystem());

                    return Behaviors.receiveMessage(
                            message -> {
                                context.getLog().debug("Received message: {}", message);
                                return Behaviors.same();
                            });
                });
    }
}
