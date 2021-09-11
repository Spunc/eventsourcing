package eventsourcing.auftrag.event;

import eventsourcing.auftrag.read.AuftragCurrentState;
import eventsourcing.base.Event;

public interface AuftragReadEvent extends Event {

	void accept(AuftragCurrentState auftragCurrentState);
}
