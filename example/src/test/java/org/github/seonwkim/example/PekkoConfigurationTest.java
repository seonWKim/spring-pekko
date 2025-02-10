package org.github.seonwkim.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.github.seonwkim.core.PekkoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.actors.pekko.enabled=true",
        "pekko.actor.provider=cluster",
        "pekko.actor.allow-java-serialization=on",
        "pekko.actor.warn-about-java-serializer-usage=off",
        "pekko.remote.artery.canonical-hostname=127.0.0.1",
        "pekko.remote.artery.canonical-port=2551",
        "pekko.cluster.seed-nodes=pekko://clusterName@127.0.0.1:2551,pekko://clusterName@127.0.0.1:2552,pekko://clusterName@127.0.0.1:2553",
        "pekko.cluster.downing-provider-class=org.apache.pekko.cluster.sbr.SplitBrainResolverProvider"
})
public class PekkoConfigurationTest {

    @Autowired
    private PekkoConfiguration pekkoConfiguration;

    @Test
    public void testPekkoConfiguration() {
        assertThat(pekkoConfiguration).isNotNull();
        assertThat(pekkoConfiguration.getActor().getProvider()).isEqualTo("cluster");
        assertThat(pekkoConfiguration.getActor().getAllowJavaSerialization()).isEqualTo("on");
        assertThat(pekkoConfiguration.getActor().getWarnAboutJavaSerializerUsage()).isEqualTo("off");
        assertThat(pekkoConfiguration.getRemote().getArtery().getCanonicalHostname()).isEqualTo("127.0.0.1");
        assertThat(pekkoConfiguration.getRemote().getArtery().getCanonicalPort()).isEqualTo(2551);
        assertThat(pekkoConfiguration.getCluster().getSeedNodes()).containsExactly(
                "pekko://clusterName@127.0.0.1:2551",
                "pekko://clusterName@127.0.0.1:2552",
                "pekko://clusterName@127.0.0.1:2553"
        );
        assertThat(pekkoConfiguration.getCluster().getDowningProviderClass()).isEqualTo("org.apache.pekko.cluster.sbr.SplitBrainResolverProvider");
    }
}
