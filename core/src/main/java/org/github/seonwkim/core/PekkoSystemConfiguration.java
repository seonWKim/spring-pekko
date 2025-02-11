package org.github.seonwkim.core;

import java.util.List;

import org.apache.pekko.actor.typed.ActorSystem;
import org.github.seonwkim.core.behaviors.ClusterRootBehavior;
import org.github.seonwkim.core.behaviors.NonClusterRootBehavior;
import org.github.seonwkim.core.behaviors.SingletonBehavior;
import org.github.seonwkim.core.utils.PekkoConfigurationUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@Configuration
public class PekkoSystemConfiguration {

    @Bean
    public ActorSystem<?> actorSystem(
            List<SingletonBehavior<?>> singletonBehaviors,
            PekkoConfiguration pekkoConfig) {
        final boolean configureCluster = pekkoConfig.getCluster() != null;
        final Config config = ConfigFactory.parseString(PekkoConfigurationUtils.toPropertiesString(pekkoConfig));
        if (configureCluster) {
            return ActorSystem.create(
                    ClusterRootBehavior.create(singletonBehaviors),
                    pekkoConfig.getCluster().getName(),
                    config
            );
        } else {
            return ActorSystem.create(
                    NonClusterRootBehavior.create(singletonBehaviors),
                    pekkoConfig.getName(),
                    config
            );
        }
    }
}
