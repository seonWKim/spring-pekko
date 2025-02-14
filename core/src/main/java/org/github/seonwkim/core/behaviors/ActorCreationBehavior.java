package org.github.seonwkim.core.behaviors;

import java.util.UUID;
import java.util.function.Supplier;
import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.springframework.stereotype.Component;

@Component
public class ActorCreationBehavior implements SingletonBehavior<ActorCreationBehavior.Command> {
	public interface Command {}

	// Message to request a child actor creation
	public static class CreateChild<T> implements Command {
		private final Supplier<Behavior<T>> behaviorSupplier;
		private final ActorRef<ChildCreated<T>> replyTo;

		public CreateChild(Supplier<Behavior<T>> behaviorSupplier, ActorRef<ChildCreated<T>> replyTo) {
			this.behaviorSupplier = behaviorSupplier;
			this.replyTo = replyTo;
		}

		public Supplier<Behavior<T>> getBehaviorSupplier() {
			return behaviorSupplier;
		}

		public ActorRef<ChildCreated<T>> getReplyTo() {
			return replyTo;
		}
	}

	// Message containing the created child actor reference
	public static class ChildCreated<T> {
		public final ActorRef<T> childRef;

		public ChildCreated(ActorRef<T> childRef) {
			this.childRef = childRef;
		}

		public ActorRef<T> getChildRef() {
			return childRef;
		}
	}

	@Override
	public String beanName() {
		return "system-actor-creation-behavior";
	}

	/** The behavior that listens for child actor creation requests. */
	public Behavior<Command> create() {
		return Behaviors.setup(
				context ->
						Behaviors.receive(Command.class)
								.onMessage(
										CreateChild.class,
										cmd -> {
											CreateChild<?> message = (CreateChild<?>) cmd;
											String childName = "child-" + UUID.randomUUID();
											ActorRef<?> childRef =
													context.spawn(message.getBehaviorSupplier().get(), childName);
											ChildCreated childCreated = new ChildCreated(childRef);
											message.getReplyTo().tell(childCreated);
											return Behaviors.same();
										})
								.build());
	}
}
