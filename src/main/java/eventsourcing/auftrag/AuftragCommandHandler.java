package eventsourcing.auftrag;

import eventsourcing.auftrag.command.AendereAuftragCommand;
import eventsourcing.auftrag.command.ErstelleAuftragCommand;
import eventsourcing.auftrag.command.FuegePositionHinzuCommand;
import eventsourcing.auftrag.command.LoeschePositionCommand;
import eventsourcing.auftrag.domain.Auftrag;
import eventsourcing.auftrag.event.AuftragEvent;
import eventsourcing.base.EventStore;
import java.util.UUID;
import java.util.function.Consumer;
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

	public void aendern(UUID id, AendereAuftragCommand command) {
		handleUpdate(id, auftrag -> auftrag.aendern(command));
	}

	public UUID positionHinzufuegen(UUID id, FuegePositionHinzuCommand command) {
		Auftrag auftrag = getAuftrag(id);
		UUID positionId = auftrag.fuegePositionHinzu(command);
		auftragEventStore.append(id, auftrag.getUncommittedEvents());
		return positionId;
	}

	public void positionLoeschen(UUID id, LoeschePositionCommand command){
		handleUpdate(id, auftrag -> auftrag.loeschePosition(command));
	}

	private void handleUpdate(UUID id, Consumer<Auftrag> auftragConsumer) {
		Auftrag auftrag = getAuftrag(id);
		auftragConsumer.accept(auftrag);
		auftragEventStore.append(id, auftrag.getUncommittedEvents());
	}

	private Auftrag getAuftrag(UUID id) {
		Auftrag auftrag = new Auftrag();
		auftrag.apply(auftragEventStore.get(id));
		return auftrag;
	}

}
