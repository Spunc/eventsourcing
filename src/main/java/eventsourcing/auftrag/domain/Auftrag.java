package eventsourcing.auftrag.domain;

import eventsourcing.auftrag.command.AendereAuftragCommand;
import eventsourcing.auftrag.command.ErstelleAuftragCommand;
import eventsourcing.auftrag.command.FuegePositionHinzuCommand;
import eventsourcing.auftrag.command.LoeschePositionCommand;
import eventsourcing.auftrag.event.AuftragErstelltEvent;
import eventsourcing.auftrag.event.AuftragEvent;
import eventsourcing.auftrag.event.AuftragGeaendertEvent;
import eventsourcing.auftrag.event.PositionGeloeschtEvent;
import eventsourcing.auftrag.event.PositionHinzugefuegtEvent;
import eventsourcing.auftrag.event.VersicherungAngefordertEvent;
import eventsourcing.auftrag.event.VersicherungStorniertEvent;
import eventsourcing.auftrag.event.VersicherungsBestaetigt;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;

public class Auftrag {
	private static final BigDecimal VERSICHERUNGS_LIMIT = BigDecimal.valueOf(5_000);

	private Ladestelle beladestelle;

	private Ladestelle entladestelle;

	private final Map<UUID, Position> positionen = new HashMap<>();

	private Versicherungsstatus versicherungsstatus = Versicherungsstatus.KEINE;

	// Commands

	public void erstellen(ErstelleAuftragCommand command) {
		if (command.getBeladestelle().getLadezeit().isBefore(ZonedDateTime.now())) {
			throw new BadRequestException("Ladezeit der Beladestelle ist in der Vergangenheit");
		}
		if (command.getEntladestelle().getLadezeit().isBefore(command.getBeladestelle().getLadezeit())) {
			throw new BadRequestException("Ladezeit der Entladestelle ist früher als Ladezeit der Beladestelle");
		}

		AuftragErstelltEvent erstelltEvent = new AuftragErstelltEvent(command.getBeladestelle(), command.getEntladestelle());
		applyAndSave(erstelltEvent);
	}

	public void aendern(AendereAuftragCommand command) {
		if (command.getBeladestelle().getLadezeit().isBefore(ZonedDateTime.now())) {
			throw new BadRequestException("Ladezeit der Beladestelle ist in der Vergangenheit");
		}
		if (command.getEntladestelle().getLadezeit().isBefore(command.getBeladestelle().getLadezeit())) {
			throw new BadRequestException("Ladezeit der Entladestelle ist früher als Ladezeit der Beladestelle");
		}

		AuftragGeaendertEvent geaendertEvent = new AuftragGeaendertEvent(command.getBeladestelle(), command.getEntladestelle());
		applyAndSave(geaendertEvent);
	}

	public UUID fuegePositionHinzu(FuegePositionHinzuCommand command) {
		UUID id = UUID.randomUUID();
		Position position = new Position(id, command.getBezeichnung(), command.getWarenwert());

		applyAndSave(new PositionHinzugefuegtEvent(position));

		versicherungsCheck();
		return id;
	}

	public void loeschePosition(LoeschePositionCommand command) {
		if (positionen.containsKey(command.getId())) {
			applyAndSave(new PositionGeloeschtEvent(command.getId()));
			versicherungsCheck();
		}
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
		positionen.put(event.getPosition().getId(), event.getPosition());
	}

	public void apply(PositionGeloeschtEvent event) { positionen.remove(event.getId()); }

	public void apply(VersicherungAngefordertEvent event) {
		versicherungsstatus = Versicherungsstatus.ANGEFORDERT;
	}

	public void apply(VersicherungsBestaetigt versicherungsBestaetigt) { versicherungsstatus = Versicherungsstatus.BESTAETIGT; }

	public void apply(VersicherungStorniertEvent event) {
		versicherungsstatus = Versicherungsstatus.KEINE;
	}


	// Helfer

	private BigDecimal getGesamtWarenwert() {
		return positionen.values().stream().map(Position::getWarenwert).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private void versicherungsCheck() {
		BigDecimal gesamtWarenwert = getGesamtWarenwert();
		boolean brauchtVersicherung = VERSICHERUNGS_LIMIT.compareTo(gesamtWarenwert) <= 0;

		if (brauchtVersicherung && versicherungsstatus == Versicherungsstatus.KEINE) {
			applyAndSave(new VersicherungAngefordertEvent(gesamtWarenwert));
		} else if (!brauchtVersicherung && versicherungsstatus != Versicherungsstatus.KEINE) {
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

	public void apply(List<AuftragEvent> events) {
		events.forEach(e -> e.accept(this));
	}


}
