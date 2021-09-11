package eventsourcing.auftrag.builder;

import eventsourcing.auftrag.domain.Ladestelle;
import eventsourcing.auftrag.event.AuftragErstelltEvent;
import java.time.ZonedDateTime;

public class AuftragErstelltEventBuilder {

	private Ladestelle beladestelle = new Ladestelle("12345", ZonedDateTime.now().plusDays(2));

	private Ladestelle entladestelle = new Ladestelle("98765", beladestelle.getLadezeit().plusDays(1));

	public AuftragErstelltEventBuilder mitBeladestelle(Ladestelle beladestelle) {
		this.beladestelle = beladestelle;
		return this;
	}

	public AuftragErstelltEventBuilder mitEntladestelle(Ladestelle entladestelle) {
		this.entladestelle = entladestelle;
		return this;
	}

	public AuftragErstelltEvent build() {
		return new AuftragErstelltEvent(beladestelle, entladestelle);
	}
}
