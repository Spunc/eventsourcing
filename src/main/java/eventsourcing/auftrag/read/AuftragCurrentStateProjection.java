package eventsourcing.auftrag.read;

import eventsourcing.Config;
import eventsourcing.base.EventEnvelop;
import eventsourcing.base.InMemoryProjectionStore;
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
	public void onAuftragEvent(EventEnvelop<?> event) {
		projectionStore.upsert(event.getId(), new AuftragCurrentState());
	}

}
