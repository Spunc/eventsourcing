package eventsourcing.auftrag;

import eventsourcing.Event;

public interface AuftragEvent extends Event {

	void accept(Auftrag auftrag);

}
