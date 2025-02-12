package org.github.seonwkim.core;

import java.util.List;
import org.github.seonwkim.core.behaviors.ClusterRootBehavior;
import org.github.seonwkim.core.behaviors.NonClusterRootBehavior;
import org.github.seonwkim.core.behaviors.SingletonBehavior;
import org.springframework.stereotype.Component;

@Component
public class DependencyContainer {
	private final ClusterRootBehavior clusterRootBehavior;
	private final NonClusterRootBehavior nonClusterRootBehavior;
	private final List<SingletonBehavior<?>> singletonBehaviors;

	public DependencyContainer(
			ClusterRootBehavior clusterRootBehavior,
			NonClusterRootBehavior nonClusterRootBehavior,
			List<SingletonBehavior<?>> singletonBehaviors) {
		this.clusterRootBehavior = clusterRootBehavior;
		this.nonClusterRootBehavior = nonClusterRootBehavior;
		this.singletonBehaviors = singletonBehaviors;
	}

	public ClusterRootBehavior getClusterRootBehavior() {
		return clusterRootBehavior;
	}

	public NonClusterRootBehavior getNonClusterRootBehavior() {
		return nonClusterRootBehavior;
	}

	public List<SingletonBehavior<?>> getSingletonBehaviors() {
		return singletonBehaviors;
	}
}
