package org.github.seonwkim.core;

import org.apache.pekko.cluster.sharding.typed.javadsl.ClusterSharding;

public class PekkoClusterSharding {

    private final ClusterSharding clusterSharding;

    public PekkoClusterSharding(ClusterSharding clusterSharding) {this.clusterSharding = clusterSharding;}

    public ClusterSharding getClusterSharding() {
        if (clusterSharding == null) {
            throw new IllegalStateException("ClusterSharding is not configured");
        }
        return clusterSharding;
    }
}
