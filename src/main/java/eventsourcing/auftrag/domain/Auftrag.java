package eventsourcing.auftrag.domain;

import eventsourcing.auftrag.command.AuftragAendernCommand;
import eventsourcing.auftrag.command.AuftragErstellenCommand;
import eventsourcing.auftrag.command.FuegePositionHinzuCommand;
import eventsourcing.auftrag.command.LoeschePositionCommand;
import eventsourcing.auftrag.event.AuftragErstelltEvent;
import eventsourcing.auftrag.event.AuftragEvent;
import eventsourcing.auftrag.event.AuftragGeaendertEvent;
import eventsourcing.auftrag.event.PositionGeloeschtEvent;
import eventsourcing.auftrag.event.PositionHinzugefuegtEvent;
import eventsourcing.auftrag.event.VersicherungAngefordertEvent;
import eventsourcing.auftrag.event.VersicherungStorniertEvent;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.Getter;

public class Auftrag {
	private static final BigDecimal VERSICHERUNGS_LIMIT = BigDecimal.valueOf(5_000);

	private Ladestelle beladestelle;

	private Ladestelle entladestelle;

	private List<Position> positionen = new ArrayList<>();

	private VersicherungsStatus versicherungsStatus = VersicherungsStatus.KEINE;

	// Commands

	public void erstellen(AuftragErstellenCommand command) {
		if (command.getBeladestelle().getLadezeit().isBefore(ZonedDateTime.now())) {
			throw new IllegalArgumentException("Ladezeit der Beladestelle ist in der Vergangenheit");
		}
		if (command.getEntladestelle().getLadezeit().isBefore(command.getBeladestelle().getLadezeit())) {
			throw new IllegalArgumentException("Ladezeit der Entladestelle ist früher als Ladezeit der Beladestelle");
		}

		AuftragErstelltEvent erstelltEvent = new AuftragErstelltEvent();
		erstelltEvent.setBeladestelle(command.getBeladestelle());
		erstelltEvent.setEntladestelle(command.getEntladestelle());
		applyAndSave(erstelltEvent);
	}

	public void aendern(AuftragAendernCommand command) {
		if (command.getBeladestelle().getLadezeit().isBefore(ZonedDateTime.now())) {
			throw new IllegalArgumentException("Ladezeit der Beladestelle ist in der Vergangenheit");
		}
		if (command.getEntladestelle().getLadezeit().isBefore(command.getBeladestelle().getLadezeit())) {
			throw new IllegalArgumentException("Ladezeit der Entladestelle ist früher als Ladezeit der Beladestelle");
		}

		AuftragGeaendertEvent geaendertEvent = new AuftragGeaendertEvent();
		geaendertEvent.setBeladestelle(command.getBeladestelle());
		geaendertEvent.setEntladestelle(command.getEntladestelle());
		applyAndSave(geaendertEvent);
	}

	public UUID fuegePositionHinzu(FuegePositionHinzuCommand command) {
		UUID id = UUID.randomUUID();
		Position position = new Position();
		position.setId(id);
		position.setBezeichnung(command.getBezeichnung());
		position.setWarenwert(command.getWarenwert());

		applyAndSave(new PositionHinzugefuegtEvent(position));

		versicherungsCheck();
		return id;
	}

	public void loeschePosition(LoeschePositionCommand command) {
		boolean vorhanden = positionen.stream()
				.map(Position::getId)
				.anyMatch(p -> command.getId().equals(p));
		if (!vorhanden) {
			throw new NoSuchElementException("Position nicht gefunden");
		}

		applyAndSave(new PositionGeloeschtEvent(command.getId()));

		versicherungsCheck();
	}


	// Event-apply-Methoden

	public void apply(AuftragErstelltEvent event) {
		beladestelle = event.getBeladestelle();
		entladestelle = event.getEntladestelle();
	}

	public void apply(AuftragGeaendertEvent event) {
		beladestelle = event.getBeladestelle();
		entladestelle = event.getEntladestelle();
	}

	public void apply(PositionHinzugefuegtEvent event) {
		positionen.add(event.getPosition());
	}

	public void apply(PositionGeloeschtEvent event) {
		positionen.removeIf(p -> event.getId().equals(p.getId()));
	}

	public void apply(VersicherungAngefordertEvent event) {
		versicherungsStatus = VersicherungsStatus.ANGEFORDERT;
	}

	public void apply(VersicherungStorniertEvent event) {
		versicherungsStatus = VersicherungsStatus.KEINE;
	}


	// Helfer

	private BigDecimal getGesamtWarenwert() {
		return positionen.stream().map(Position::getWarenwert).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private void versicherungsCheck() {
		BigDecimal gesamtWarenwert = getGesamtWarenwert();
		boolean brauchtVersicherung = VERSICHERUNGS_LIMIT.compareTo(gesamtWarenwert) <= 0;

		if (brauchtVersicherung && versicherungsStatus == VersicherungsStatus.KEINE) {
			applyAndSave(new VersicherungAngefordertEvent(gesamtWarenwert));
		} else if (!brauchtVersicherung && versicherungsStatus != VersicherungsStatus.KEINE) {
			applyAndSave(new VersicherungStorniertEvent());
		}
	}


	// -- Technik

	@Getter
	private final List<AuftragEvent> uncommittedEvents = new ArrayList<>();

	private void applyAndSave(AuftragEvent event) {
		event.accept(this);
		uncommittedEvents.add(event);
	}

	public void applyAll(List<AuftragEvent> events) {
		events.forEach(e -> e.accept(this));
	}

}
