plugins {
	java
	id("org.springframework.boot") version "2.7.0"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "org.github.seonwkim"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(8)
	}
}

repositories {
	mavenCentral()
}

subprojects {
	apply(plugin = "java")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")

	repositories {
		mavenCentral()
	}

	dependencies {
		implementation("org.apache.pekko:pekko-actor-typed_3:1.1.3")
		implementation("org.apache.pekko:pekko-cluster-typed_3:1.1.3")
		implementation("org.apache.pekko:pekko-cluster-sharding-typed_3:1.1.3")

		implementation("org.springframework.boot:spring-boot-starter")

		testImplementation("org.springframework.boot:spring-boot-starter-test")

		testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}
