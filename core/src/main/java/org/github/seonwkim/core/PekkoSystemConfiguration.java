package org.github.seonwkim.core;

import org.apache.pekko.actor.ActorSystem;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.typesafe.config.ConfigFactory;

@Configuration
@ConditionalOnBean(PekkoConfiguration.class)
public class PekkoSystemConfiguration {

//    @Bean
//    public ActorSystem<?> actorSystem(PekkoConfiguration configuration) {
//        final boolean configureCluster = configuration.getCluster() != null;
//        final String clusterName = configuration.getCluster().getName();
//        final Object config = ConfigFactory.parse
//    }
}
