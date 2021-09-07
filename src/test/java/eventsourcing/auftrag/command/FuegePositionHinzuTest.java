package eventsourcing.auftrag.command;

import static org.assertj.core.api.Assertions.assertThat;

import eventsourcing.auftrag.domain.Auftrag;
import eventsourcing.auftrag.domain.Ladestelle;
import eventsourcing.auftrag.domain.Position;
import eventsourcing.auftrag.event.AuftragErstelltEvent;
import eventsourcing.auftrag.event.PositionHinzugefuegtEvent;
import eventsourcing.auftrag.event.VersicherungAngefordertEvent;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FuegePositionHinzuTest {

	private final Auftrag auftrag = new Auftrag();

	@BeforeEach
	void init() {
		// Ein erstellter Auftrag
		ZonedDateTime beladezeit = ZonedDateTime.now().plusDays(2);
		AuftragErstelltEvent erstelltEvent = new AuftragErstelltEvent();
		erstelltEvent.setBeladestelle(new Ladestelle("12345", beladezeit));
		erstelltEvent.setEntladestelle(new Ladestelle("98765", beladezeit.plusDays(2)));

		auftrag.apply(erstelltEvent);
	}

	@Test
	void fuege_postion_hinzu__keine_versicherung() {
		// Command erstellen
		String bezeichnung = "Wurst";
		BigDecimal warenwert = BigDecimal.valueOf(4_999);
		FuegePositionHinzuCommand command = new FuegePositionHinzuCommand(bezeichnung, warenwert);

		// Ausführen
		UUID id = auftrag.fuegePositionHinzu(command);

		// Verifizieren
		var events = auftrag.getUncommittedEvents();
		assertThat(events).hasSize(1);

		var event = events.get(0);
		assertThat(event).isInstanceOfSatisfying(PositionHinzugefuegtEvent.class,
				e -> assertThat(e.getPosition()).isEqualTo(new Position(id, bezeichnung, warenwert)));
	}

	@Test
	void fuege_postion_hinzu__versicherung_erforderlich() {
		// Command erstellen
		BigDecimal warenwert = BigDecimal.valueOf(5_000);
		FuegePositionHinzuCommand command = new FuegePositionHinzuCommand("Wurst", warenwert);

		// Ausführen
		auftrag.fuegePositionHinzu(command);

		// Verifizieren
		var events = auftrag.getUncommittedEvents();
		assertThat(events).hasSize(2);

		var event = events.get(1);
		assertThat(event).isInstanceOfSatisfying(VersicherungAngefordertEvent.class,
				e -> assertThat(e.getGesamtWarenwert()).isEqualTo(warenwert));
	}

	@Test
	void fuege_zweite_position_hinzu__versicherung_erforderlich() {
		// Auftrag soll bereits eine Position ohne Versicherung haben
		BigDecimal warenwert_bestehende_Position = BigDecimal.valueOf(4_000);
		auftrag.apply(new PositionHinzugefuegtEvent(
				new Position(UUID.randomUUID(), "Wurst", warenwert_bestehende_Position)));

		// Command erstellen
		BigDecimal warenwert_neue_Position = BigDecimal.valueOf(2_000);
		FuegePositionHinzuCommand command = new FuegePositionHinzuCommand("Käse", warenwert_neue_Position);

		// Ausführen
		auftrag.fuegePositionHinzu(command);

		// Verifizieren
		var events = auftrag.getUncommittedEvents();
		assertThat(events).hasSize(2);

		var event = events.get(1);
		assertThat(event).isInstanceOfSatisfying(VersicherungAngefordertEvent.class,
				e -> assertThat(e.getGesamtWarenwert()).isEqualTo(warenwert_bestehende_Position.add(warenwert_neue_Position)));
	}

	@Test
	void fuege_zweite_position_hinzu__versicherung_besteht_bereits() {
		// Auftrag soll bereits eine Position mit Versicherung haben
		BigDecimal warenwert_bestehende_Position = BigDecimal.valueOf(6_000);
		auftrag.apply(new PositionHinzugefuegtEvent(
				new Position(UUID.randomUUID(), "Wurst", warenwert_bestehende_Position)));
		auftrag.apply(new VersicherungAngefordertEvent(warenwert_bestehende_Position));

		// Command erstellen
		BigDecimal warenwert_neue_Position = BigDecimal.valueOf(2_000);
		FuegePositionHinzuCommand command = new FuegePositionHinzuCommand("Käse", warenwert_neue_Position);

		// Ausführen
		auftrag.fuegePositionHinzu(command);

		// Verifizieren
		var events = auftrag.getUncommittedEvents();
		assertThat(events).hasSize(1);

		var event = events.get(0);
		assertThat(event).isInstanceOf(PositionHinzugefuegtEvent.class);
	}

}
