package eventsourcing.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InMemoryEventStore<T extends Event> implements EventStore<T> {

	private final Map<UUID, List<T>> store = new HashMap<>();

	@Override
	public List<T> get(UUID id) {
		var result = store.get(id);
		if (result == null) {
			throw new NotFoundException();
		}
		return result;
	}

	@Override
	public void append(UUID id, List<T> newEvents) {
		var oldEvents = store.get(id);
		if (oldEvents == null) {
			oldEvents = new ArrayList<>();
			store.put(id, oldEvents);
		}
		oldEvents.addAll(newEvents);
	}
}
