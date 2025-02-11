package org.github.seonwkim.core.behaviors;

import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.cluster.ClusterEvent;
import org.apache.pekko.cluster.ClusterEvent.MemberEvent;
import org.apache.pekko.cluster.typed.Cluster;
import org.apache.pekko.cluster.typed.Subscribe;
import org.springframework.stereotype.Component;

@Component
class ClusterListenerSingletonBehavior implements SingletonBehavior<ClusterEvent.MemberEvent> {

    @Override
    public String name() {
        return "cluster-listener-singleton-behavior";
    }

    @Override
    public Behavior<MemberEvent> create() {
        return Behaviors.setup(context -> {
            Cluster cluster = Cluster.get(context.getSystem());
            context.getLog().info("Cluster Listener initialized on node: {}", cluster.selfMember().address());

            // Subscribe to cluster membership changes
            cluster.subscriptions().tell(new Subscribe<>(context.getSelf(), MemberEvent.class));

            return Behaviors.receiveMessage(event -> {
                if (event instanceof ClusterEvent.MemberUp) {
                    ClusterEvent.MemberUp memberUp = (ClusterEvent.MemberUp) event;
                    context.getLog().info("🚀 New node joined the cluster: {}", memberUp.member().address());
                } else if (event instanceof ClusterEvent.MemberRemoved) {
                    ClusterEvent.MemberRemoved memberRemoved = (ClusterEvent.MemberRemoved) event;
                    context.getLog().info("❌ Node left the cluster: {} (was {})",
                                          memberRemoved.member().address(), memberRemoved.previousStatus());
                } else if (event instanceof ClusterEvent.MemberWeaklyUp) {
                    ClusterEvent.MemberWeaklyUp memberWeaklyUp = (ClusterEvent.MemberWeaklyUp) event;
                    context.getLog().info("⚠️ Node is weakly up: {}", memberWeaklyUp.member().address());
                } else {
                    context.getLog().debug("Other cluster event: {}", event);
                }
                return Behaviors.same();
            });
        });
    }
}
