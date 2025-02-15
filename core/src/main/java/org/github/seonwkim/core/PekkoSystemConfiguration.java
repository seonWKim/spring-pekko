package org.github.seonwkim.core;

import java.util.List;

import org.apache.pekko.actor.typed.ActorSystem;
import org.apache.pekko.cluster.sharding.typed.javadsl.ClusterSharding;
import org.apache.pekko.cluster.sharding.typed.javadsl.Entity;
import org.github.seonwkim.core.behaviors.ClusterRootBehavior;
import org.github.seonwkim.core.behaviors.NonClusterRootBehavior;
import org.github.seonwkim.core.behaviors.ShardBehavior;
import org.github.seonwkim.core.behaviors.impl.DefaultClusterRootBehavior;
import org.github.seonwkim.core.behaviors.impl.DefaultNonClusterRootBehavior;
import org.github.seonwkim.core.utils.PekkoConfigurationUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@Configuration
public class PekkoSystemConfiguration {

    @Bean
    public ActorSystem<?> actorSystem(DependencyContainer container, PekkoConfiguration pekkoConfig) {
        final Config config =
                ConfigFactory.parseString(PekkoConfigurationUtils.toPropertiesString(pekkoConfig));
        if (pekkoConfig.isClusterMode()) {
            return ActorSystem.create(
                    container.getClusterRootBehavior().create(container),
                    pekkoConfig.getCluster().getName(),
                    config);
        } else {
            return ActorSystem.create(
                    container.getNonClusterRootBehavior().create(container), pekkoConfig.getName(), config);
        }
    }

    @Bean
    @ConditionalOnMissingBean(ClusterRootBehavior.class)
    public ClusterRootBehavior clusterRootBehavior() {
        return new DefaultClusterRootBehavior();
    }

    @Bean
    @ConditionalOnMissingBean(NonClusterRootBehavior.class)
    public NonClusterRootBehavior nonClusterRootBehavior() {
        return new DefaultNonClusterRootBehavior();
    }

    @Bean
    public PekkoClusterSharding pekkoClusterSharding(ActorSystem<?> actorSystem,
                                                     PekkoConfiguration configuration,
                                                     List<ShardBehavior<?>> shardBehaviors
                                                     ) {
        if (configuration.isClusterMode()) {
            ClusterSharding sharding = ClusterSharding.get(actorSystem);
            for (ShardBehavior<?> shardBehavior : shardBehaviors) {
                initEntity(sharding, shardBehavior);
            }
            return new PekkoClusterSharding(ClusterSharding.get(actorSystem));
        }

        return new PekkoClusterSharding(null);
    }

    private <T> void initEntity(ClusterSharding clusterSharding, ShardBehavior<T> shardBehavior) {
        clusterSharding.init(
                Entity.of(shardBehavior.getEntityTypeKey(), shardBehavior::create));
    }
}
