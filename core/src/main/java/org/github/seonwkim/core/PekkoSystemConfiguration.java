package org.github.seonwkim.core;

import org.apache.pekko.actor.typed.ActorSystem;
import org.github.seonwkim.core.behaviors.ClusterRootBehavior;
import org.github.seonwkim.core.behaviors.NonClusterRootBehavior;
import org.github.seonwkim.core.behaviors.ShardConfigurationBehavior;
import org.github.seonwkim.core.behaviors.impl.DefaultClusterRootBehavior;
import org.github.seonwkim.core.behaviors.impl.DefaultNonClusterRootBehavior;
import org.github.seonwkim.core.behaviors.impl.DefaultShardConfigurationBehavior;
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
        final boolean configureCluster = pekkoConfig.getCluster() != null;
        final Config config =
                ConfigFactory.parseString(PekkoConfigurationUtils.toPropertiesString(pekkoConfig));
        if (configureCluster) {
            return ActorSystem.create(
                    container.getClusterRootBehavior().create(),
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
    @ConditionalOnMissingBean(ShardConfigurationBehavior.class)
    public ShardConfigurationBehavior shardConfigurationBehavior() {
        return new DefaultShardConfigurationBehavior();
    }
}
