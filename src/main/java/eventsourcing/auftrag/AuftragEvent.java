package eventsourcing.auftrag;

import eventsourcing.base.Event;

public interface AuftragEvent extends Event {

	void accept(Auftrag auftrag);

}
