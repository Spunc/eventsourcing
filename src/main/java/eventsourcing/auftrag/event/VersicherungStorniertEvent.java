package eventsourcing.auftrag.event;

import eventsourcing.auftrag.domain.Auftrag;

public class VersicherungStorniertEvent implements AuftragEvent {

	@Override
	public void accept(Auftrag auftrag) {}
}
