package org.github.seonwkim.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.github.seonwkim.core.SimpleActorBehavior.AskMessageCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootApplication
@SpringBootTest
@TestPropertySource(
        properties = {
                "pekko.name=simple",
                "pekko.actor.provider=local",
        },
        locations = "")
class ActorServiceTest {

    @Autowired
    private ActorService actorService;

    @Test
    void test_createActor() throws InterruptedException, ExecutionException {
        CompletionStage<ActorRef<SimpleActorBehavior.Command>> actor = actorService.createActor(
                SimpleActorBehavior::create, Duration.ofSeconds(5));
        ActorRef<SimpleActorBehavior.Command> actorRef = actor.toCompletableFuture().get();
        assertThat(actorRef).isNotNull();
    }

    @Test
    void test_tell() throws InterruptedException, ExecutionException {
        CompletionStage<ActorRef<SimpleActorBehavior.Command>> actor = actorService.createActor(
                SimpleActorBehavior::create, Duration.ofSeconds(5));
        ActorRef<SimpleActorBehavior.Command> actorRef = actor.toCompletableFuture().get();
        assertThat(actorRef).isNotNull();

        SimpleActorBehavior.PrintMessageCommand message = new SimpleActorBehavior.PrintMessageCommand(
                "Hello, World!");
        actorService.tell(actorRef, message);
        Thread.sleep(500); // wait for message to be sent
        assertThat(SimpleActorBehavior.counterForTest).isEqualTo(1);
    }

    @Test
    void test_ask() throws InterruptedException, ExecutionException {
        // Create an actor
        CompletionStage<ActorRef<SimpleActorBehavior.Command>> actor = actorService.createActor(
                SimpleActorBehavior::create, Duration.ofSeconds(5));
        ActorRef<SimpleActorBehavior.Command> actorRef = actor.toCompletableFuture().get();
        assertThat(actorRef).isNotNull();

        // Call the ask method
        CompletableFuture<String> result = actorService.<SimpleActorBehavior.Command, String>ask(
                actorRef,
                replyTo -> new AskMessageCommand<>("Hello, World!", replyTo),
                Duration.ofSeconds(5)
        ).toCompletableFuture();
        String response = result.get();
        assertThat(response).isEqualTo("Received: Hello, World!");
    }
}

class SimpleActorBehavior {

    public static int counterForTest = 0;

    public interface Command {}

    public static class PrintMessageCommand implements Command {
        public final String message;

        public PrintMessageCommand(String message) {
            this.message = message;
        }
    }

    public static class AskMessageCommand<T> implements Command{
        public final String message;
        public final ActorRef<T> replyTo;

        public AskMessageCommand(String message, ActorRef<T> replyTo) {
            this.message = message;
            this.replyTo = replyTo;
        }
    }

    public static String beanName() {
        return "system-simple-actor-behavior";
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(
                context ->
                        Behaviors.receive(Command.class)
                                 .onMessage(PrintMessageCommand.class,
                                            SimpleActorBehavior::handlePrintMessageCommand)
                                 .onMessage(AskMessageCommand.class,
                                            SimpleActorBehavior::handleAskMessageCommand)
                                 .build());
    }

    private static Behavior<Command> handlePrintMessageCommand(PrintMessageCommand command) {
        counterForTest++;
        return Behaviors.same();
    }

    private static Behavior<Command> handleAskMessageCommand(AskMessageCommand<String> command) {
        command.replyTo.tell("Received: " + command.message);
        return Behaviors.same();
    }
}
