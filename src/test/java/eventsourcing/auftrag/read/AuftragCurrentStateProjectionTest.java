package eventsourcing.auftrag.read;

import static org.assertj.core.api.Assertions.assertThat;

import eventsourcing.auftrag.builder.AuftragErstelltEventBuilder;
import eventsourcing.auftrag.domain.Position;
import eventsourcing.auftrag.event.PositionHinzugefuegtEvent;
import eventsourcing.base.EventEnvelop;
import eventsourcing.base.InMemoryProjectionStore;
import eventsourcing.base.ProjectionStore;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AuftragCurrentStateProjectionTest {

	private final ProjectionStore<AuftragCurrentState> projectionStore = new InMemoryProjectionStore<>("AuftragCurrentState");

	private final AuftragCurrentStateProjection projection = new AuftragCurrentStateProjection(projectionStore);

	@Test
	void auftragErstelltEvent() {
		UUID id = UUID.randomUUID();
		projection.onAuftragEvent(new EventEnvelop<>(id, new AuftragErstelltEventBuilder().build()));
		assertThat(projectionStore.get(id)).isPresent();
	}

	@Test
	void auftragErstelltEvent_und_positionHinzugefuegtEvent() {
		UUID id = UUID.randomUUID();
		projection.onAuftragEvent(new EventEnvelop<>(id, new AuftragErstelltEventBuilder().build()));
		projection.onAuftragEvent(new EventEnvelop<>(id, new PositionHinzugefuegtEvent(new Position(UUID.randomUUID(), "Ware", BigDecimal.TEN))));
		assertThat(projectionStore.get(id)).isPresent().hasValueSatisfying(currentState -> {
			assertThat(currentState.getPositionen()).hasSize(1);
		});
	}
}
