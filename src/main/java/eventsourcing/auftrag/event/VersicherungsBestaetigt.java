package eventsourcing.auftrag.event;

import eventsourcing.auftrag.domain.Auftrag;

public class VersicherungsBestaetigt implements AuftragEvent {

	@Override
	public void accept(Auftrag auftrag) {
		auftrag.apply(this);
	}
}
