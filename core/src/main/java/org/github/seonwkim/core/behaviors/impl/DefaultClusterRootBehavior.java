package org.github.seonwkim.core.behaviors.impl;

import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.cluster.typed.Cluster;
import org.github.seonwkim.core.ActorRefWrapper;
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
                                        ActorRef<?> actorRef = context.spawn(behavior.create(),
                                                                             behavior.beanName());

                                        ActorRefWrapper<?> wrapper =
                                                new ActorRefWrapper() {
                                                    @Override
                                                    public String beanName() {
                                                        return behavior.beanName();
                                                    }

                                                    @Override
                                                    public ActorRef unwrap() {
                                                        return actorRef;
                                                    }
                                                };
                                        container
                                                .getApplicationContext()
                                                .registerBean(behavior.beanName(), ActorRefWrapper.class,
                                                              () -> wrapper);

                                    });

                    return Behaviors.receiveMessage(
                            message -> {
                                context.getLog().debug("Received message: {}", message);
                                return Behaviors.same();
                            });
                });
    }
}
