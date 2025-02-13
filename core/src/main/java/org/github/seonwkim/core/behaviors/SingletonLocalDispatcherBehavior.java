package org.github.seonwkim.core.behaviors;

import java.io.Serializable;

import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.ActorContext;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.springframework.stereotype.Component;

@Component
public class SingletonLocalDispatcherBehavior
        implements SingletonBehavior<SingletonLocalDispatcherBehavior.Command> {
    interface Command extends Serializable {}

    @FunctionalInterface
    public interface DispatchRunnable<REQ> {
        void run(ActorContext<REQ> context);
    }

    static class TellCommand<REQ> implements Command {
        private final DispatchRunnable<REQ> command;

        TellCommand(DispatchRunnable<REQ> command) {this.command = command;}
    }

    @Override
    public String beanName() {
        return "seonwkim-singleton-local-dispatcher-behavior";
    }

    public Behavior<Command> create() {
        return Behaviors.setup(context -> Behaviors.receive(Command.class)
                                                   .onMessage(TellCommand.class, command -> {
                                                       command.command.run(context);
                                                       return Behaviors.same();
                                                   })
                                                   .build());
    }
}
