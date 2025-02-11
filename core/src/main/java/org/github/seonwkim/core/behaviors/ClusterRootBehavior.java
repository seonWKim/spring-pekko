package org.github.seonwkim.core.behaviors;

import org.apache.pekko.actor.typed.ActorSystem;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.cluster.typed.Cluster;

public class ClusterRootBehavior {
    public static Behavior<String> create() {
        return Behaviors.setup(context -> {
            Cluster cluster = Cluster.get(context.getSystem());
            context.getLog().info("Cluster node started at {}", cluster.selfMember().address());

            setUpShardedActors(context.getSystem());

            return Behaviors.receiveMessage(message -> {
                context.getLog().info("Received message: {}", message);
                return Behaviors.same();
            });
        });
    }

    private static void setUpShardedActors(ActorSystem<?> system) {
        ShardedActorsSetUpBehavior.create(system);
    }
}
