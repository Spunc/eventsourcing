package eventsourcing.auftrag.event;

import eventsourcing.auftrag.domain.Auftrag;
import eventsourcing.auftrag.read.AuftragCurrentState;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PositionGeloeschtEvent implements AuftragEvent, AuftragReadEvent {

	private UUID id;

	@Override
	public void accept(Auftrag auftrag) {
		auftrag.apply(this);
	}

	@Override
	public void accept(AuftragCurrentState auftragCurrentState) { auftragCurrentState.apply(this); }
}
