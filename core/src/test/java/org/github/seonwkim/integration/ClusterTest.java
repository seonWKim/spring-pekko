package org.github.seonwkim.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.pekko.actor.typed.ActorRef;
import org.github.seonwkim.core.PekkoAutoConfiguration;
import org.github.seonwkim.core.service.ActorService;
import org.github.seonwkim.integration.SimpleActorBehavior.Command;
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

    @Test
    public void test() throws Exception {
        // 1. Get ActorService from each context
        ActorService actorService1 = context1.getBean(ActorService.class);
        ActorService actorService2 = context2.getBean(ActorService.class);
        ActorService actorService3 = context3.getBean(ActorService.class);

//        // 2. Get sharded actors from ActorService
//        ActorRef<Command> shardedActor1 = actorService1.getShardedActor("shard-1");
//        ActorRef<SimpleActorBehavior.Command> shardedActor2 = actorService2.getShardedActor("shard-2");
//        ActorRef<SimpleActorBehavior.Command> shardedActor3 = actorService3.getShardedActor("shard-3");
//
//        // 3. Send messages to sharded actors
//        actorService1.tell(shardedActor1, new SimpleActorBehavior.PrintMessageCommand("Message to shard-1"));
//        actorService2.tell(shardedActor2, new SimpleActorBehavior.PrintMessageCommand("Message to shard-2"));
//        actorService3.tell(shardedActor3, new SimpleActorBehavior.PrintMessageCommand("Message to shard-3"));
//
//        // 4. Verify whether the sharded actors acted appropriately
//        Thread.sleep(500); // wait for messages to be processed
//        assertEquals(1, ((SimpleActorBehavior) shardedActor1.underlyingActor()).getCounterForTest());
//        assertEquals(1, ((SimpleActorBehavior) shardedActor2.underlyingActor()).getCounterForTest());
//        assertEquals(1, ((SimpleActorBehavior) shardedActor3.underlyingActor()).getCounterForTest());
    }

    @AfterAll
    public static void tearDown() {
        context1.close();
        context2.close();
        context3.close();
    }
}
