package eventsourcing.auftrag.event;

import eventsourcing.auftrag.domain.Auftrag;
import eventsourcing.auftrag.domain.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PositionHinzugefuegtEvent implements AuftragEvent {

	private Position position;

	@Override
	public void accept(Auftrag auftrag) {
		auftrag.apply(this);
	}
}
