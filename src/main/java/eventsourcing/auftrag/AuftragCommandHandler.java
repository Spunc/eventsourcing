package eventsourcing.auftrag;

import eventsourcing.auftrag.command.AendereAuftragCommand;
import eventsourcing.auftrag.command.ErstelleAuftragCommand;
import eventsourcing.auftrag.command.FuegePositionHinzuCommand;
import eventsourcing.auftrag.domain.Auftrag;
import eventsourcing.auftrag.event.AuftragEvent;
import eventsourcing.base.EventStore;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuftragCommandHandler {

	private final EventStore<AuftragEvent> auftragEventStore;

	public UUID erstellen(ErstelleAuftragCommand command) {
		UUID id = UUID.randomUUID();
		Auftrag auftrag = new Auftrag();
		auftrag.erstellen(command);

		auftragEventStore.append(id, auftrag.getUncommittedEvents());
		return id;
	}

	public void aendern(AendereAuftragCommand command, UUID id) {
		Auftrag auftrag = new Auftrag();
		auftrag.apply(auftragEventStore.get(id));
		auftrag.aendern(command);

		auftragEventStore.append(id, auftrag.getUncommittedEvents());
	}

	public void positionHinzufuegen(FuegePositionHinzuCommand command, UUID id) {
		Auftrag auftrag = new Auftrag();
		auftrag.apply(auftragEventStore.get(id));
		auftrag.fuegePositionHinzu(command);

		auftragEventStore.append(id, auftrag.getUncommittedEvents());
	}
}
