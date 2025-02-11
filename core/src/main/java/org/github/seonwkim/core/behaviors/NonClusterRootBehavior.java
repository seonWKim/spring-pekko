package org.github.seonwkim.core.behaviors;

import java.util.List;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.Behaviors;

public class NonClusterRootBehavior {
  public static Behavior<String> create(List<SingletonBehavior<?>> singletonBehaviors) {
    return Behaviors.setup(
        context -> {
          context.getLog().debug("Non-cluster behavior started");

          singletonBehaviors.forEach(
              behavior -> {
                context.spawn(behavior.create(), behavior.name());
              });

          return Behaviors.receiveMessage(
              message -> {
                context.getLog().debug("Received message: {}", message);
                return Behaviors.same();
              });
        });
  }
}
