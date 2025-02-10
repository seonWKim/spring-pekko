package org.github.seonwkim.core;

import org.github.seonwkim.core.common.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "spring.actors.pekko", name = "enabled", havingValue = "true")
@ConfigurationProperties(prefix = "pekko")
public class PekkoConfiguration {

    private Actor actor;
    private Remote remote;

    @Nullable
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
            private Canonical canonical;

            public Canonical getCanonical() {
                return canonical;
            }

            public void setCanonical(Canonical canonical) {
                this.canonical = canonical;
            }

            public static class Canonical {
                private String hostname;
                private int port;

                public String getHostname() {
                    return hostname;
                }

                public void setHostname(String hostname) {
                    this.hostname = hostname;
                }

                public int getPort() {
                    return port;
                }

                public void setPort(int port) {
                    this.port = port;
                }
            }
        }
    }

    public static class Cluster {
        private String name;
        private String[] seedNodes;
        private String downingProviderClass;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

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
