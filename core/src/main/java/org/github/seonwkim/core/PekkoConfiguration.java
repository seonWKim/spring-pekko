package org.github.seonwkim.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "spring.actors.pekko", name = "enabled", havingValue = "true")
@ConfigurationProperties(prefix = "pekko")
public class PekkoConfiguration {

    private Actor actor;
    private Remote remote;
    private Cluster cluster;

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Remote getRemote() {
        return remote;
    }

    public void setRemote(Remote remote) {
        this.remote = remote;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public static class Actor {
        private String provider;
        private String allowJavaSerialization;
        private String warnAboutJavaSerializerUsage;

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public String getAllowJavaSerialization() {
            return allowJavaSerialization;
        }

        public void setAllowJavaSerialization(String allowJavaSerialization) {
            this.allowJavaSerialization = allowJavaSerialization;
        }

        public String getWarnAboutJavaSerializerUsage() {
            return warnAboutJavaSerializerUsage;
        }

        public void setWarnAboutJavaSerializerUsage(String warnAboutJavaSerializerUsage) {
            this.warnAboutJavaSerializerUsage = warnAboutJavaSerializerUsage;
        }
    }

    public static class Remote {
        private Artery artery;

        public Artery getArtery() {
            return artery;
        }

        public void setArtery(Artery artery) {
            this.artery = artery;
        }

        public static class Artery {
            private String canonicalHostname;
            private int canonicalPort;

            public String getCanonicalHostname() {
                return canonicalHostname;
            }

            public void setCanonicalHostname(String canonicalHostname) {
                this.canonicalHostname = canonicalHostname;
            }

            public int getCanonicalPort() {
                return canonicalPort;
            }

            public void setCanonicalPort(int canonicalPort) {
                this.canonicalPort = canonicalPort;
            }
        }
    }

    public static class Cluster {
        private String[] seedNodes;
        private String downingProviderClass;

        public String[] getSeedNodes() {
            return seedNodes;
        }

        public void setSeedNodes(String[] seedNodes) {
            this.seedNodes = seedNodes;
        }

        public String getDowningProviderClass() {
            return downingProviderClass;
        }

        public void setDowningProviderClass(String downingProviderClass) {
            this.downingProviderClass = downingProviderClass;
        }
    }
}
