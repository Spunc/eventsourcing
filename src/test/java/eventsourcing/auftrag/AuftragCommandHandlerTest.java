package eventsourcing.auftrag;

import static org.assertj.core.api.Assertions.assertThat;

import eventsourcing.base.EventStore;
import eventsourcing.base.InMemoryEventStore;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AuftragCommandHandlerTest {

	private final EventStore<AuftragEvent> eventStore = new InMemoryEventStore<>();

	private final AuftragCommandHandler commandHandler = new AuftragCommandHandler(eventStore);

	@Test
	void erstellen() {
		ZonedDateTime beladezeit = ZonedDateTime.now().plusDays(1);
		ZonedDateTime entladezeit = beladezeit.plusDays(1);
		AuftragErstellenCommand command = new AuftragErstellenCommand();
		command.setBeladestelle(new Ladestelle("12345", beladezeit));
		command.setEntladestelle(new Ladestelle("98765", entladezeit));

		UUID id = commandHandler.erstellen(command);

		var auftragEvents = eventStore.get(id);
		assertThat(auftragEvents).hasSize(1);
	}

}
