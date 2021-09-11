package eventsourcing.auftrag.read;

import static org.assertj.core.api.Assertions.assertThat;

import eventsourcing.auftrag.builder.AuftragErstelltEventBuilder;
import eventsourcing.auftrag.event.AuftragErstelltEvent;
import eventsourcing.base.EventEnvelop;
import eventsourcing.base.InMemoryProjectionStore;
import eventsourcing.base.ProjectionStore;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AuftragCurrentStateProjectionTest {

	private final ProjectionStore<AuftragCurrentState> projectionStore = new InMemoryProjectionStore<>();

	private AuftragCurrentStateProjection projection = new AuftragCurrentStateProjection(projectionStore);

	@Test
	void auftragErstelltEvent() {
		UUID id = UUID.randomUUID();
		eventsourcing.auftrag.domain.Ladestelle beladestelle = new eventsourcing.auftrag.domain.Ladestelle("12345", ZonedDateTime.now().plusDays(1));
		eventsourcing.auftrag.domain.Ladestelle enladestelle = new eventsourcing.auftrag.domain.Ladestelle("45678", ZonedDateTime.now().plusDays(2));
		AuftragErstelltEvent event = new AuftragErstelltEventBuilder()
				.mitBeladestelle(beladestelle)
				.mitEntladestelle(enladestelle).build();

		projection.onAuftragEvent(new EventEnvelop<>(id, new AuftragErstelltEventBuilder().build()));

		assertThat(projectionStore.get(id)).isPresent().hasValueSatisfying(currentState -> {
			assertThat(currentState.getBeladestelle()).isEqualTo(new Ladestelle(beladestelle.getPlz(), beladestelle.getLadezeit()));
			assertThat(currentState.getEntladestelle()).isEqualTo(new Ladestelle(enladestelle.getPlz(), enladestelle.getLadezeit()));
		});
	}
}
