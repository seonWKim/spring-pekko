package org.github.seonwkim.core;

import java.util.List;

import org.github.seonwkim.core.behaviors.ClusterRootBehavior;
import org.github.seonwkim.core.behaviors.NonClusterRootBehavior;
import org.github.seonwkim.core.behaviors.SingletonBehavior;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DependencyContainer {
	private final GenericApplicationContext applicationContext;
	private final ClusterRootBehavior clusterRootBehavior;
	private final NonClusterRootBehavior nonClusterRootBehavior;
	private final List<SingletonBehavior<?>> singletonBehaviors;
	public DependencyContainer(
            GenericApplicationContext applicationContext,
            ClusterRootBehavior clusterRootBehavior,
            NonClusterRootBehavior nonClusterRootBehavior,
            List<SingletonBehavior<?>> singletonBehaviors) {
		this.applicationContext = applicationContext;
		this.clusterRootBehavior = clusterRootBehavior;
		this.nonClusterRootBehavior = nonClusterRootBehavior;
		this.singletonBehaviors = singletonBehaviors;
    }

	public GenericApplicationContext getApplicationContext() {
		return applicationContext;
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
