package org.github.seonwkim.core.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Properties;

import org.github.seonwkim.core.PekkoConfiguration;
import org.junit.jupiter.api.Test;

public class PekkoConfigurationUtilsTest {

    @Test
    public void testToProperties() {
        PekkoConfiguration.Actor actor = new PekkoConfiguration.Actor("cluster", "on", "off");
        PekkoConfiguration.Remote.Artery.Canonical canonical = new PekkoConfiguration.Remote.Artery.Canonical("127.0.0.1", 2551);
        PekkoConfiguration.Remote.Artery artery = new PekkoConfiguration.Remote.Artery(canonical);
        PekkoConfiguration.Remote remote = new PekkoConfiguration.Remote(artery);
        PekkoConfiguration.Cluster cluster = new PekkoConfiguration.Cluster(
                new String[] {
                        "pekko://clusterName@127.0.0.1:2551",
                        "pekko://clusterName@127.0.0.1:2552",
                        "pekko://clusterName@127.0.0.1:2553"
                },
                "org.apache.pekko.cluster.sbr.SplitBrainResolverProvider"
        );

        PekkoConfiguration pekkoConfiguration = new PekkoConfiguration(actor, remote, cluster);

        Properties properties = PekkoConfigurationUtils.toProperties(pekkoConfiguration);

        assertThat(properties).isNotNull();
        assertThat(properties).containsEntry("pekko.actor.provider", "cluster");
        assertThat(properties).containsEntry("pekko.actor.allow-java-serialization", "on");
        assertThat(properties).containsEntry("pekko.actor.warn-about-java-serializer-usage", "off");
        assertThat(properties).containsEntry("pekko.remote.artery.canonical.hostname", "127.0.0.1");
        assertThat(properties).containsEntry("pekko.remote.artery.canonical.port", "2551");
        assertThat(properties).containsEntry("pekko.cluster.seed-nodes",
                                             "[pekko://clusterName@127.0.0.1:2551, pekko://clusterName@127.0.0.1:2552, pekko://clusterName@127.0.0.1:2553]");
        assertThat(properties).containsEntry("pekko.cluster.downing-provider-class",
                                             "org.apache.pekko.cluster.sbr.SplitBrainResolverProvider");
    }
}
