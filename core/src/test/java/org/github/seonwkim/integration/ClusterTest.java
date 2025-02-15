package org.github.seonwkim.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.github.seonwkim.core.PekkoAutoConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.TestPropertySource;

public class ClusterTest {

    private static ConfigurableApplicationContext context1;
    private static ConfigurableApplicationContext context2;
    private static ConfigurableApplicationContext context3;

    @PropertySource("classpath:application1.properties")
    @SpringBootApplication(scanBasePackages = "org.github.seonwkim.core")
    static class Application1Config {
    }

    @PropertySource("classpath:application2.properties")
    @SpringBootApplication(scanBasePackages = "org.github.seonwkim.core")
    static class Application2Config {
    }

    @PropertySource("classpath:application3.properties")
    @SpringBootApplication(scanBasePackages = "org.github.seonwkim.core")
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

        assertEquals("cluster-node-1", pekkoName1);
        assertEquals("cluster-node-2", pekkoName2);
        assertEquals("cluster-node-3", pekkoName3);
    }

    @AfterAll
    public static void tearDown() {
        context1.close();
        context2.close();
        context3.close();
    }
}
