package eventsourcing.auftrag.read;

import eventsourcing.Config;
import eventsourcing.auftrag.event.AuftragEvent;
import eventsourcing.auftrag.event.AuftragReadEvent;
import eventsourcing.base.EventEnvelop;
import eventsourcing.base.ProjectionStore;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuftragCurrentStateProjection {

	private final ProjectionStore<AuftragCurrentState> projectionStore;

	@JmsListener(destination = Config.AUFTRAG_TOPIC)
	public void onAuftragEvent(EventEnvelop<AuftragEvent> envelop) {
		var event = envelop.getEvent();
		// Filtere Events nach AuftragReadEvent Interface
		if (event instanceof AuftragReadEvent ) {
			AuftragCurrentState auftragCurrentState = getOrCreate(envelop.getId());
			((AuftragReadEvent) event).accept(auftragCurrentState);
			projectionStore.upsert(envelop.getId(), auftragCurrentState);
		}
	}

	private AuftragCurrentState getOrCreate(UUID id) {
		return projectionStore.get(id).orElse(new AuftragCurrentState());
	}

}
