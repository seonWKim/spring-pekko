package org.github.seonwkim.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootApplication(scanBasePackages = "org.github.seonwkim.core")
public class ClusterTest {

    private static ConfigurableApplicationContext context1;
    private static ConfigurableApplicationContext context2;
    private static ConfigurableApplicationContext context3;

    @SpringBootTest
    @TestPropertySource(
            properties = {
                    "server.port=16001",
                    "pekko.name=cluster1",
                    "pekko.actor.provider=cluster",
                    "pekko.actor.allow-java-serialization=on",
                    "pekko.actor.warn-about-java-serializer-usage=off",
                    "pekko.remote.artery.canonical.hostname=127.0.0.1",
                    "pekko.remote.artery.canonical.port=62552",
                    "pekko.cluster.name=clusterName1",
                    "pekko.cluster.seed-nodes=pekko://clusterName1@127.0.0.1:62552,pekko://clusterName1@127.0.0.1:62553,pekko://clusterName1@127.0.0.1:62554",
                    "pekko.cluster.downing-provider-class=org.apache.pekko.cluster.sbr.SplitBrainResolverProvider"
            })
    static class Application1Config {
    }

    @SpringBootTest
    @TestPropertySource(
            properties = {
                    "server.port=16002",
                    "pekko.name=cluster2",
                    "pekko.actor.provider=cluster",
                    "pekko.actor.allow-java-serialization=on",
                    "pekko.actor.warn-about-java-serializer-usage=off",
                    "pekko.remote.artery.canonical.hostname=127.0.0.1",
                    "pekko.remote.artery.canonical.port=62553",
                    "pekko.cluster.name=clusterName2",
                    "pekko.cluster.seed-nodes=pekko://clusterName2@127.0.0.1:62552,pekko://clusterName2@127.0.0.1:62553,pekko://clusterName2@127.0.0.1:62554",
                    "pekko.cluster.downing-provider-class=org.apache.pekko.cluster.sbr.SplitBrainResolverProvider"
            })
    static class Application2Config {
    }

    @SpringBootTest
    @TestPropertySource(
            properties = {
                    "server.port=16003",
                    "pekko.name=cluster3",
                    "pekko.actor.provider=cluster",
                    "pekko.actor.allow-java-serialization=on",
                    "pekko.actor.warn-about-java-serializer-usage=off",
                    "pekko.remote.artery.canonical.hostname=127.0.0.1",
                    "pekko.remote.artery.canonical.port=62554",
                    "pekko.cluster.name=clusterName3",
                    "pekko.cluster.seed-nodes=pekko://clusterName3@127.0.0.1:62552,pekko://clusterName3@127.0.0.1:62553,pekko://clusterName3@127.0.0.1:62554",
                    "pekko.cluster.downing-provider-class=org.apache.pekko.cluster.sbr.SplitBrainResolverProvider"
            })
    static class Application3Config {
    }

    @BeforeAll
    public static void setup() {
        context1 = SpringApplication.run(Application1Config.class);
        context2 = SpringApplication.run(Application2Config.class);
        context3 = SpringApplication.run(Application3Config.class);
    }

    @Test
    public void testPekkoNameProperty() {
        String pekkoName1 = context1.getEnvironment().getProperty("pekko.name");
        String pekkoName2 = context2.getEnvironment().getProperty("pekko.name");
        String pekkoName3 = context3.getEnvironment().getProperty("pekko.name");

        assertEquals("cluster1", pekkoName1);
        assertEquals("cluster2", pekkoName2);
        assertEquals("cluster3", pekkoName3);
    }

    @AfterAll
    public static void tearDown() {
        context1.close();
        context2.close();
        context3.close();
    }
}
