package eventsourcing.auftrag.read;

import eventsourcing.Config;
import eventsourcing.auftrag.event.AuftragReadEvent;
import eventsourcing.base.EventEnvelop;
import eventsourcing.base.InMemoryProjectionStore;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuftragCurrentStateProjection {

	private final InMemoryProjectionStore<AuftragCurrentState> projectionStore;

	@JmsListener(destination = Config.AUFTRAG_TOPIC)
	public void onAuftragEvent(EventEnvelop<AuftragReadEvent> event) {
		AuftragCurrentState auftragCurrentState = getOrCreate(event.getId());
		event.getEvent().accept(auftragCurrentState);
		projectionStore.upsert(event.getId(), new AuftragCurrentState());
	}

	private AuftragCurrentState getOrCreate(UUID id) {
		return projectionStore.get(id).orElse(new AuftragCurrentState());
	}

}
