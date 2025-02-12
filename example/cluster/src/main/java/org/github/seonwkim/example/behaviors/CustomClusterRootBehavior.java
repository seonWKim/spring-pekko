package org.github.seonwkim.example.behaviors;

import org.apache.pekko.actor.typed.Behavior;
import org.github.seonwkim.core.DependencyContainer;
import org.github.seonwkim.core.behaviors.ClusterRootBehavior;
import org.github.seonwkim.core.behaviors.impl.DefaultClusterRootBehavior;
import org.springframework.stereotype.Component;

@Component
public class CustomClusterRootBehavior implements ClusterRootBehavior {
	@Override
	public Behavior<String> create(DependencyContainer container) {
		DefaultClusterRootBehavior clusterRootBehavior = new DefaultClusterRootBehavior();
		System.out.println("This is how you customize ClusterRootBehavior");
		return clusterRootBehavior.create(container);
	}
}
