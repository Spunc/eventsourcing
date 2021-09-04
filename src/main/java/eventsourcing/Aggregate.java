package eventsourcing;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Aggregate<T extends Event> {

	private final UUID id;

	@Getter
	private List<T> uncommittedEvents = new ArrayList<>();

	protected void applyAndSave(T event) {
		apply(event);
		uncommittedEvents.add(event);
	}

	protected abstract void apply(T event);
}
