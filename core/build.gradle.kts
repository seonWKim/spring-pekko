dependencies {
	implementation("org.apache.pekko:pekko-actor-typed_3")
	implementation("org.apache.pekko:pekko-cluster-typed_3")
	implementation("org.apache.pekko:pekko-cluster-sharding-typed_3")
	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
