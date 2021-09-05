package eventsourcing.auftrag;

import static org.assertj.core.api.Assertions.assertThat;

import eventsourcing.base.EventStore;
import eventsourcing.base.InMemoryEventStore;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AuftragCommandHandlerTest {

	private EventStore<AuftragEvent> eventStore = new InMemoryEventStore<>();

	private AuftragCommandHandler commandHandler = new AuftragCommandHandler(eventStore);

	@Test
	void erstellen() {
		AuftragErstellenCommand command = new AuftragErstellenCommand();
		command.setGewicht(2_500);
		command.setWarenwert(BigDecimal.valueOf(3_000));

		UUID id = commandHandler.erstellen(command);

		var auftragEvents = eventStore.get(id);
		assertThat(auftragEvents.size()).isGreaterThan(0);
	}
}
