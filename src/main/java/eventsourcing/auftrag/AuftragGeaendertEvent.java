package eventsourcing.auftrag;

import lombok.Data;

@Data
public class AuftragGeaendertEvent implements AuftragEvent {

	private Ladestelle beladestelle;

	private Ladestelle entladestelle;

	@Override
	public void accept(Auftrag auftrag) {
		auftrag.apply(this);
	}
}
