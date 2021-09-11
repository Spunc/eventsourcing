package eventsourcing.auftrag.event;

import eventsourcing.auftrag.domain.Auftrag;
import eventsourcing.auftrag.domain.Ladestelle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuftragGeaendertEvent implements AuftragEvent {

	private Ladestelle beladestelle;

	private Ladestelle entladestelle;

	@Override
	public void accept(Auftrag auftrag) {
		auftrag.apply(this);
	}
}
