package eventsourcing.auftrag.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import eventsourcing.auftrag.builder.AuftragBuilder;
import eventsourcing.auftrag.builder.PositionBuilder;
import eventsourcing.auftrag.domain.Auftrag;
import eventsourcing.auftrag.domain.Versicherungsstatus;
import eventsourcing.auftrag.event.VersicherungStorniertEvent;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class LoeschePositionTest {

	@Test
	void loesche_bestehende_Position() {
		// Ein Auftrag mit einer Position
		UUID positionId = UUID.randomUUID();
		Auftrag auftrag = new AuftragBuilder()
				.mitPosition(new PositionBuilder().mitId(positionId).build())
				.build();

		// Command erstellen
		LoeschePositionCommand command = new LoeschePositionCommand(positionId);

		// Ausführen
		auftrag.loeschePosition(command);

		// Verifizieren
		var events = auftrag.getUncommittedEvents();
		assertThat(events).hasSize(1);
	}

	@Test
	void kann_nicht_gleiche_Position_zweimal_loeschen() {
		// Ein Auftrag mit einer Position
		UUID positionId = UUID.randomUUID();
		Auftrag auftrag = new AuftragBuilder()
				.mitPosition(new PositionBuilder().mitId(positionId).build())
				.build();

		// Command erstellen
		LoeschePositionCommand command = new LoeschePositionCommand(positionId);

		// Ausführen und Verifizieren
		auftrag.loeschePosition(command);
		assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> auftrag.loeschePosition(command));
	}

	@Test
	void versicherung_wird_storniert_wenn_gesamter_Warenwert_unter_5000_liegt() {
		// Ein Auftrag mit einer Position und Versicherung
		UUID positionId = UUID.randomUUID();
		BigDecimal gesamtWarenwert = BigDecimal.valueOf(6_000);
		Auftrag auftrag = new AuftragBuilder()
				.mitPosition(new PositionBuilder().mitId(positionId).mitWarenwert(gesamtWarenwert).build())
				.mitVersicherungsstatus(Versicherungsstatus.ANGEFORDERT, gesamtWarenwert)
				.build();

		// Command erstellen
		LoeschePositionCommand command = new LoeschePositionCommand(positionId);

		// Ausführen
		auftrag.loeschePosition(command);

		// Verifizieren
		var events = auftrag.getUncommittedEvents();
		assertThat(events).hasSize(2);

		var event = events.get(1);
		assertThat(event).isInstanceOf(VersicherungStorniertEvent.class);
	}
}
