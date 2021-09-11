package eventsourcing.auftrag.event;

import eventsourcing.auftrag.domain.Auftrag;
import lombok.AllArgsConstructor;

public class VersicherungsBestaetigt implements AuftragEvent {

	@Override
	public void accept(Auftrag auftrag) {
		auftrag.apply(this);
	}
}
