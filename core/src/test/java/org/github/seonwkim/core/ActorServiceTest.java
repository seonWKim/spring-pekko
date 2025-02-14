package org.github.seonwkim.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
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
}

class SimpleActorBehavior {

    public interface Command {}

    public static class PrintMessageCommand implements Command {
        public final String message;

        public PrintMessageCommand(String message) {
            this.message = message;
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
                                            command -> handlePrintMessageCommand(command))
                                 .build());
    }

    private static Behavior<Command> handlePrintMessageCommand(PrintMessageCommand command) {
        System.out.println(command.message);
        return Behaviors.same();
    }
}
