package eventsourcing.auftrag.read;

import eventsourcing.auftrag.event.AuftragErstelltEvent;
import eventsourcing.auftrag.event.AuftragGeaendertEvent;
import eventsourcing.auftrag.event.PositionHinzugefuegtEvent;
import java.text.NumberFormat;
import java.util.List;
import lombok.Data;

@Data
public class AuftragCurrentState {

	private Ladestelle beladestelle;

	private Ladestelle entladestelle;

	private List<Position> positionen;

	public void apply(AuftragErstelltEvent event) {
		beladestelle = new Ladestelle(event.getBeladestelle().getPlz(), event.getBeladestelle().getLadezeit());
		entladestelle = new Ladestelle(event.getEntladestelle().getPlz(), event.getEntladestelle().getLadezeit());
	}

	public void apply(AuftragGeaendertEvent event) {
		beladestelle = new Ladestelle(event.getBeladestelle().getPlz(), event.getBeladestelle().getLadezeit());
		entladestelle = new Ladestelle(event.getEntladestelle().getPlz(), event.getEntladestelle().getLadezeit());
	}

	public void apply(PositionHinzugefuegtEvent event) {
		var position = event.getPosition();
		String geldwert = NumberFormat.getCurrencyInstance().format(position.getWarenwert());
		positionen.add(new Position(position.getId(), position.getBezeichnung(), geldwert));
	}
}
