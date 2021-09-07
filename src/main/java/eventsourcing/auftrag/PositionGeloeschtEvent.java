package eventsourcing.auftrag;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PositionGeloeschtEvent implements AuftragEvent {

	private UUID id;

	@Override
	public void accept(Auftrag auftrag) {
		auftrag.apply(this);
	}
}
