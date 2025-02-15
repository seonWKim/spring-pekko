package org.github.seonwkim.core.service;

import org.apache.pekko.actor.typed.ActorSystem;
import org.apache.pekko.cluster.sharding.typed.javadsl.EntityRef;
import org.apache.pekko.cluster.sharding.typed.javadsl.EntityTypeKey;
import org.github.seonwkim.core.ActorRefWrapper;
import org.github.seonwkim.core.PekkoClusterSharding;
import org.github.seonwkim.core.behaviors.impl.ActorCreationBehavior.Command;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ActorClusterService {

    private final PekkoClusterSharding clusterSharding;

    public ActorClusterService(PekkoClusterSharding clusterSharding) {
        this.clusterSharding = clusterSharding;
    }

    public <T> EntityRef<T> getShardedActor(EntityTypeKey<T> entityTypeKey, String entityId) {
        return clusterSharding.getClusterSharding().entityRefFor(entityTypeKey, entityId);
    }
}
