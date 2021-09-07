package eventsourcing.auftrag.event;

import eventsourcing.auftrag.domain.Auftrag;
import eventsourcing.base.Event;

public interface AuftragEvent extends Event {

	void accept(Auftrag auftrag);

}
