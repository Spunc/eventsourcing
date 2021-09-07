package eventsourcing.auftrag.event;

import eventsourcing.auftrag.domain.Auftrag;
import eventsourcing.auftrag.domain.Position;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PositionHinzugefuegtEvent implements AuftragEvent {

	private Position position;

	@Override
	public void accept(Auftrag auftrag) {
		auftrag.apply(this);
	}
}
