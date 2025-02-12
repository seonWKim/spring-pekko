package org.github.seonwkim.core.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.github.seonwkim.core.PekkoConfiguration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootApplication
@DirtiesContext
public class PekkoConfigurationTest {

	@Nested
	@SpringBootTest
	@TestPropertySource(
			properties = {
				"pekko.name=simple",
				"pekko.actor.provider=local",
			},
			locations = "")
	class TestNullableClusterConfiguration {

		@Autowired private PekkoConfiguration pekkoConfiguration;

		@Test
		public void testNonClusterConfiguration() {
			assertThat(pekkoConfiguration).isNotNull();
			assertThat(pekkoConfiguration.getName()).isEqualTo("simple");
			assertThat(pekkoConfiguration.getActor().getProvider()).isEqualTo("local");
			assertThat(pekkoConfiguration.getRemote()).isNull();
			assertThat(pekkoConfiguration.getCluster()).isNull();
		}
	}

	@Nested
	@SpringBootTest
	@TestPropertySource(
			properties = {
				"pekko.name=cluster",
				"pekko.actor.provider=cluster",
				"pekko.actor.allow-java-serialization=on",
				"pekko.actor.warn-about-java-serializer-usage=off",
				"pekko.remote.artery.canonical.hostname=127.0.0.1",
				"pekko.remote.artery.canonical.port=62552",
				"pekko.cluster.name=clusterName",
				"pekko.cluster.seed-nodes=pekko://clusterName@127.0.0.1:62552,pekko://clusterName@127.0.0.1:62553,pekko://clusterName@127.0.0.1:62554",
				"pekko.cluster.downing-provider-class=org.apache.pekko.cluster.sbr.SplitBrainResolverProvider"
			},
			locations = "")
	class TestPekkoConfiguration {

		@Autowired private PekkoConfiguration pekkoConfiguration;

		@Test
		public void testClusterPekkoConfiguration() {
			assertThat(pekkoConfiguration).isNotNull();
			assertThat(pekkoConfiguration.getActor().getProvider()).isEqualTo("cluster");
			assertThat(pekkoConfiguration.getActor().getAllowJavaSerialization()).isEqualTo("on");
			assertThat(pekkoConfiguration.getActor().getWarnAboutJavaSerializerUsage()).isEqualTo("off");
			assertThat(pekkoConfiguration.getRemote().getArtery().getCanonical().getHostname())
					.isEqualTo("127.0.0.1");
			assertThat(pekkoConfiguration.getRemote().getArtery().getCanonical().getPort())
					.isEqualTo(62552);
			assertThat(pekkoConfiguration.getCluster().getSeedNodes())
					.containsExactly(
							"pekko://clusterName@127.0.0.1:62552",
							"pekko://clusterName@127.0.0.1:62553",
							"pekko://clusterName@127.0.0.1:62554");
			assertThat(pekkoConfiguration.getCluster().getDowningProviderClass())
					.isEqualTo("org.apache.pekko.cluster.sbr.SplitBrainResolverProvider");
		}
	}
}
