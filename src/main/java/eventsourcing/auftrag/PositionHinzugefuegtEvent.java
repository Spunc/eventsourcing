package eventsourcing.auftrag;

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
