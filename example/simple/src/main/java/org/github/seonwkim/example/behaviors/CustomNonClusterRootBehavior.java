package org.github.seonwkim.example.behaviors;

import org.apache.pekko.actor.typed.Behavior;
import org.github.seonwkim.core.DependencyContainer;
import org.github.seonwkim.core.behaviors.DefaultNonClusterRootBehavior;
import org.github.seonwkim.core.behaviors.NonClusterRootBehavior;
import org.springframework.stereotype.Component;

@Component
public class CustomNonClusterRootBehavior implements NonClusterRootBehavior {
	@Override
	public Behavior<String> create(DependencyContainer container) {
		NonClusterRootBehavior behavior = new DefaultNonClusterRootBehavior();
		System.out.println("This is how you customize NonClusterRootBehavior");
		return behavior.create(container);
	}
}
