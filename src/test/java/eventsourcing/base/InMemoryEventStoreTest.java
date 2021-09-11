package eventsourcing.base;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class InMemoryEventStoreTest {

	static class CreatedEvent implements Event {}
	static class ModifiedEvent implements Event {}

	private final EventStore<Event> eventStore = new InMemoryEventStore<>();

	@Test
	void test_mit_zwei_uuids() {
		UUID uuid_1 = UUID.randomUUID();
		UUID uuid_2 = UUID.randomUUID();

		eventStore.append(uuid_1, List.of(
				new CreatedEvent(),
				new ModifiedEvent()));
		eventStore.append(uuid_1, List.of(new ModifiedEvent()));

		eventStore.append(uuid_2, List.of(new CreatedEvent()));

		var list_1 = eventStore.get(uuid_1);
		assertThat(list_1).hasSize(3);
		assertThat(list_1).element(0).isInstanceOf(CreatedEvent.class);
		assertThat(list_1).element(1).isInstanceOf(ModifiedEvent.class);
		assertThat(list_1).element(2).isInstanceOf(ModifiedEvent.class);

		var list_2 = eventStore.get(uuid_2);
		assertThat(list_2).hasSize(1);
		assertThat(list_2).element(0).isInstanceOf(CreatedEvent.class);
	}

	@Test
	void nicht_vorhanden() {
		assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> eventStore.get(UUID.randomUUID()));
	}
}
