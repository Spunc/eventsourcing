package eventsourcing.auftrag.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import eventsourcing.auftrag.domain.Auftrag;
import eventsourcing.auftrag.domain.Ladestelle;
import eventsourcing.auftrag.event.AuftragErstelltEvent;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

public class AuftragErstellenTest {

	private static final String BELADESTELLE_PLZ = "27283";
	private static final String ENTLADESTELLE_PLZ = "86150";

	private final Auftrag auftrag = new Auftrag();

	@Test
	void erstellen() {
		// Command erstellen
		ZonedDateTime beladezeit = ZonedDateTime.now().plusDays(1);
		ZonedDateTime entladezeit = beladezeit.plusDays(1);
		ErstelleAuftragCommand command = createCommand(beladezeit, entladezeit);

		// Ausführen
		auftrag.erstellen(command);

		// Verifizieren
		var events = auftrag.getUncommittedEvents();
		assertThat(events).hasSize(1);

		var event = events.get(0);
		assertThat(event).isInstanceOfSatisfying(AuftragErstelltEvent.class, e -> {
			assertThat(e.getBeladestelle()).isEqualTo(new Ladestelle(BELADESTELLE_PLZ, beladezeit));
			assertThat(e.getEntladestelle()).isEqualTo(new Ladestelle(ENTLADESTELLE_PLZ, entladezeit));
		});
	}

	@Test
	void beladezeit_in_Vergangenheit() {
		// Command erstellen
		ZonedDateTime beladezeit = ZonedDateTime.now().minusDays(1);
		ErstelleAuftragCommand command = createCommand(beladezeit, beladezeit.plusDays(1));

		// Ausführen und Verifizieren
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> auftrag.erstellen(command));
	}

	@Test
	void entladezeit_vor_Beladezeit() {
		// Command erstellen
		ZonedDateTime beladezeit = ZonedDateTime.now().plusDays(5);
		ErstelleAuftragCommand command = createCommand(beladezeit, beladezeit.minusDays(1));

		// Ausführen und Verifizieren
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> auftrag.erstellen(command));
	}


	private ErstelleAuftragCommand createCommand(ZonedDateTime beladezeit, ZonedDateTime entladezeit) {
		ErstelleAuftragCommand command = new ErstelleAuftragCommand();
		command.setBeladestelle(new Ladestelle(BELADESTELLE_PLZ, beladezeit));
		command.setEntladestelle(new Ladestelle(ENTLADESTELLE_PLZ, entladezeit));
		return command;
	}
}
