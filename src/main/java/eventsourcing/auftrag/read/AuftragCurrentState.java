package eventsourcing.auftrag.read;

import eventsourcing.auftrag.event.AuftragErstelltEvent;
import eventsourcing.auftrag.event.AuftragGeaendertEvent;
import eventsourcing.auftrag.event.PositionGeloeschtEvent;
import eventsourcing.auftrag.event.PositionHinzugefuegtEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class AuftragCurrentState {

	private Ladestelle beladestelle;

	private Ladestelle entladestelle;

	private final List<Position> positionen = new ArrayList<>();

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
		String geldwert = NumberFormat.getCurrencyInstance(Locale.GERMANY).format(position.getWarenwert());
		positionen.add(new Position(position.getId(), position.getBezeichnung(), geldwert));
	}

	public void apply(PositionGeloeschtEvent positionGeloeschtEvent) {
		UUID id = positionGeloeschtEvent.getId();
		positionen.removeIf(p -> p.getId().equals(id));
	}
}
