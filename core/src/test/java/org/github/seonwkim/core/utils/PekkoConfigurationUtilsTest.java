package org.github.seonwkim.core.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.github.seonwkim.core.PekkoConfiguration;
import org.github.seonwkim.core.PekkoConfiguration.Actor;
import org.github.seonwkim.core.PekkoConfiguration.Cluster;
import org.github.seonwkim.core.PekkoConfiguration.Remote;
import org.github.seonwkim.core.PekkoConfiguration.Remote.Artery;
import org.github.seonwkim.core.PekkoConfiguration.Remote.Artery.Canonical;
import org.junit.jupiter.api.Test;

public class PekkoConfigurationUtilsTest {

    @Test
    public void testToMap() {
        PekkoConfiguration pekkoConfiguration = new PekkoConfiguration();

        Actor actor = new Actor();
        actor.setProvider("cluster");
        actor.setAllowJavaSerialization("on");
        actor.setWarnAboutJavaSerializerUsage("off");
        pekkoConfiguration.setActor(actor);

        Canonical canonical = new Canonical();
        canonical.setHostname("127.0.0.1");
        canonical.setPort(2551);

        Artery artery = new Artery();
        artery.setCanonical(canonical);

        Remote remote = new Remote();
        remote.setArtery(artery);
        pekkoConfiguration.setRemote(remote);

        Cluster cluster = new Cluster();
        cluster.setName("cluster-actor-system");
        cluster.setSeedNodes(new String[] {
                "pekko://clusterName@127.0.0.1:2551",
                "pekko://clusterName@127.0.0.1:2552",
                "pekko://clusterName@127.0.0.1:2553"
        });
        cluster.setDowningProviderClass("org.apache.pekko.cluster.sbr.SplitBrainResolverProvider");
        pekkoConfiguration.setCluster(cluster);

        Map<String, String> propertiesMap = PekkoConfigurationUtils.toMap(pekkoConfiguration);

        assertThat(propertiesMap).isNotNull();
        assertThat(propertiesMap).containsEntry("pekko.actor.provider", "cluster");
        assertThat(propertiesMap).containsEntry("pekko.actor.allow-java-serialization", "on");
        assertThat(propertiesMap).containsEntry("pekko.actor.warn-about-java-serializer-usage", "off");
        assertThat(propertiesMap).containsEntry("pekko.remote.artery.canonical.hostname", "127.0.0.1");
        assertThat(propertiesMap).containsEntry("pekko.remote.artery.canonical.port", "2551");
        assertThat(propertiesMap).containsEntry("pekko.cluster.name", "cluster-actor-system");
        assertThat(propertiesMap).containsEntry("pekko.cluster.seed-nodes", "[pekko://clusterName@127.0.0.1:2551, pekko://clusterName@127.0.0.1:2552, pekko://clusterName@127.0.0.1:2553]");
        assertThat(propertiesMap).containsEntry("pekko.cluster.downing-provider-class", "org.apache.pekko.cluster.sbr.SplitBrainResolverProvider");
    }
}
