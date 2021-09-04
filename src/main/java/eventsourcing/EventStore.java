package eventsourcing;

import java.util.List;
import java.util.UUID;

public interface EventStore<T extends Event> {

	List<T> get(UUID id);

	void append(UUID id, List<T> events);

}
