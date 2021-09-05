package eventsourcing.auftrag;

import eventsourcing.base.EventStore;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuftragCommandHandler {

	private final EventStore<AuftragEvent> auftragEventStore;

	public UUID erstellen(AuftragErstellenCommand command) {
		UUID id = UUID.randomUUID();
		Auftrag auftrag = new Auftrag();
		auftrag.erstellen(command);

		auftragEventStore.append(id, auftrag.getUncommittedEvents());
		return id;
	}
}
