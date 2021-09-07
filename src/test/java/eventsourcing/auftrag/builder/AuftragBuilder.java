package eventsourcing.auftrag.builder;

import eventsourcing.auftrag.domain.Auftrag;
import eventsourcing.auftrag.domain.Ladestelle;
import eventsourcing.auftrag.domain.Position;
import eventsourcing.auftrag.domain.Versicherungsstatus;
import eventsourcing.auftrag.event.AuftragErstelltEvent;
import eventsourcing.auftrag.event.PositionHinzugefuegtEvent;
import eventsourcing.auftrag.event.VersicherungAngefordertEvent;
import eventsourcing.auftrag.event.VersicherungsBestaetigt;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuftragBuilder {

	private Ladestelle beladestelle = new Ladestelle("12345", ZonedDateTime.now().plusDays(2));

	private Ladestelle entladestelle = new Ladestelle("98765", beladestelle.getLadezeit().plusDays(1));

	private List<Position> positionen = new ArrayList<>();

	private Versicherungsstatus versicherungsstatus = Versicherungsstatus.KEINE;

	private BigDecimal gesamtWarenewert;

	public AuftragBuilder mitPosition(Position position) {
		positionen.add(position);
		return this;
	}

	public AuftragBuilder mitVersicherungsstatus(Versicherungsstatus versicherungsstatus, BigDecimal gesamtWarenewert) {
		this.versicherungsstatus = versicherungsstatus;
		this.gesamtWarenewert = gesamtWarenewert;
		return this;
	}

	public Auftrag build() {
		Auftrag auftrag = new Auftrag();
		auftrag.apply(new AuftragErstelltEvent(beladestelle, entladestelle));
		for(Position position : positionen) {
			auftrag.apply(new PositionHinzugefuegtEvent(position));
		}
		handleVericherungsstatus(auftrag);
		return auftrag;
	}

	private void handleVericherungsstatus(Auftrag auftrag) {
		switch (versicherungsstatus) {
			case ANGEFORDERT:
				auftrag.apply(new VersicherungAngefordertEvent(gesamtWarenewert));
				// absichtlich kein break, weil der Bestätigung stets die Anforderung vorausgehen muss
			case BESTAETIGT:
				auftrag.apply(new VersicherungsBestaetigt());
				break;
		}
	}
}
