package org.github.seonwkim.core;

import org.github.seonwkim.core.common.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "pekko")
public class PekkoConfiguration {

    private final Actor actor;
    private final Remote remote;
    @Nullable
    private final Cluster cluster;

    public PekkoConfiguration(Actor actor, Remote remote, Cluster cluster) {
        this.actor = actor;
        this.remote = remote;
        this.cluster = cluster;
    }

    public Actor getActor() {
        return actor;
    }

    public Remote getRemote() {
        return remote;
    }

    public Cluster getCluster() {
        return cluster;
    }

    @ConstructorBinding
    public static class Actor {
        private final String provider;
        private final String allowJavaSerialization;
        private final String warnAboutJavaSerializerUsage;

        public Actor(String provider, String allowJavaSerialization, String warnAboutJavaSerializerUsage) {
            this.provider = provider;
            this.allowJavaSerialization = allowJavaSerialization;
            this.warnAboutJavaSerializerUsage = warnAboutJavaSerializerUsage;
        }

        public String getProvider() {
            return provider;
        }

        public String getAllowJavaSerialization() {
            return allowJavaSerialization;
        }

        public String getWarnAboutJavaSerializerUsage() {
            return warnAboutJavaSerializerUsage;
        }
    }

    @ConstructorBinding
    public static class Remote {
        private final Artery artery;

        public Remote(Artery artery) {
            this.artery = artery;
        }

        public Artery getArtery() {
            return artery;
        }

        @ConstructorBinding
        public static class Artery {
            private final Canonical canonical;

            public Artery(Canonical canonical) {
                this.canonical = canonical;
            }

            public Canonical getCanonical() {
                return canonical;
            }

            @ConstructorBinding
            public static class Canonical {
                private final String hostname;
                private final int port;

                public Canonical(String hostname, int port) {
                    this.hostname = hostname;
                    this.port = port;
                }

                public String getHostname() {
                    return hostname;
                }

                public int getPort() {
                    return port;
                }
            }
        }
    }

    @ConstructorBinding
    public static class Cluster {
        private final String[] seedNodes;
        private final String downingProviderClass;

        public Cluster(String[] seedNodes, String downingProviderClass) {
            this.seedNodes = seedNodes;
            this.downingProviderClass = downingProviderClass;
        }

        public String[] getSeedNodes() {
            return seedNodes;
        }

        public String getDowningProviderClass() {
            return downingProviderClass;
        }
    }
}
