package eventsourcing.auftrag.event;

import eventsourcing.auftrag.domain.Auftrag;
import eventsourcing.auftrag.domain.Ladestelle;
import eventsourcing.auftrag.read.AuftragCurrentState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuftragErstelltEvent implements AuftragEvent, AuftragReadEvent {

	private Ladestelle beladestelle;

	private Ladestelle entladestelle;

	@Override
	public void accept(Auftrag auftrag) {
		auftrag.apply(this);
	}

	@Override
	public void accept(AuftragCurrentState auftragCurrentState) {auftragCurrentState.apply(this); }
}
