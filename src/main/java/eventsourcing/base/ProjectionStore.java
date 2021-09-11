package eventsourcing.base;

import java.util.Optional;
import java.util.UUID;

public interface ProjectionStore<T> {

	void upsert(UUID id, T t);

	Optional<T> get(UUID id);

	void delete(UUID id);
}
