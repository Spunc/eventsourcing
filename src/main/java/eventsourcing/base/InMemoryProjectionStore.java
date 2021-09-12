package eventsourcing.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class InMemoryProjectionStore<T> implements ProjectionStore<T> {

	private final String name;

	private final Map<UUID, T> map = new HashMap<>();

	@Override
	public void upsert(UUID id, T t) {
		map.put(id, t);
		log.info("Projection {} id={} upsert", name, id);
	}

	@Override
	public Optional<T> get(UUID id) {
		return Optional.ofNullable(map.get(id));
	}

	@Override
	public void delete(UUID id) {
		var result = map.remove(id);
		if (result != null) log.info("Projection {} id={} deleted", name, id);
	}
}
