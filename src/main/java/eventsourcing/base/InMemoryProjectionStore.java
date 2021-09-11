package eventsourcing.base;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InMemoryProjectionStore<T> implements ProjectionStore<T> {

	private final Map<UUID, T> map = new HashMap<>();

	@Override
	public void upsert(UUID id, T t) {
		map.put(id, t);
		log.info("AuftragCurrentState {} upsert", id);
	}

	@Override
	public T get(UUID id) {
		T result = map.get(id);
		if (result == null) {
			throw new NoSuchElementException();
		}
		return result;
	}

	@Override
	public void delete(UUID id) {
		var result = map.remove(id);
		if (result != null) log.info("AuftargCurrentState {} deleted", id);
	}
}
