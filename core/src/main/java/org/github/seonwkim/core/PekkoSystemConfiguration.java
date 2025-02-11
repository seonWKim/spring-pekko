package org.github.seonwkim.core;

import java.util.List;

import org.apache.pekko.actor.typed.ActorSystem;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.github.seonwkim.core.behaviors.ClusterRootBehavior;
import org.github.seonwkim.core.behaviors.SingletonBehavior;
import org.github.seonwkim.core.utils.PekkoConfigurationUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@Configuration
public class PekkoSystemConfiguration {

    private final List<SingletonBehavior<?>> singletonBehaviors;

    public PekkoSystemConfiguration(List<SingletonBehavior<?>> singletonBehaviors) {
        this.singletonBehaviors = singletonBehaviors;
    }

    @Bean
    public ActorSystem<?> actorSystem(PekkoConfiguration pekkoConfig) {
        final boolean configureCluster = pekkoConfig.getCluster() != null;
        final Config config = ConfigFactory.parseString(PekkoConfigurationUtils.toPropertiesString(pekkoConfig));
        if (configureCluster) {
            return ActorSystem.create(
                    ClusterRootBehavior.create(singletonBehaviors),
                    // TODO: Do we have to allow users to configure their actor system name?
                    "cluster-actor-system",
                    config
            );
        } else {
            return ActorSystem.create(
                    Behaviors.empty(),
                    // TODO: Do we have to allow users to configure their actor system name?
                    "non-cluster-actor-system",
                    config
            );
        }
    }
}
