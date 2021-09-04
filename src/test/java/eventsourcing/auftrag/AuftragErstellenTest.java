package eventsourcing.auftrag;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

public class AuftragErstellenTest {

	@Test
	void auftragErstellen_ohne_Versicherung() {
		// Precondition
		Auftrag auftrag = new Auftrag();

		// Command
		int gewicht = 2_000;
		BigDecimal warenwert = BigDecimal.valueOf(4_999);
		AuftragErstellenCommand command = new AuftragErstellenCommand();
		command.setGewicht(gewicht);
		command.setWarenwert(warenwert);

		// Execute
		auftrag.erstellen(command);

		// Check
		var events = auftrag.getUncommittedEvents();

		assertThat(events).singleElement().isInstanceOfSatisfying(AuftragErstelltEvent.class, e -> {
			assertThat(e.getGewicht()).isEqualTo(gewicht);
			assertThat(e.getWarenwert()).isEqualTo(warenwert);
		});
	}

	@Test
	void auftragErstellen_mit_Versicherung() {
		// Precondition
		Auftrag auftrag = new Auftrag();

		// Command
		int gewicht = 2_000;
		BigDecimal warenwert = BigDecimal.valueOf(5_000);
		AuftragErstellenCommand command = new AuftragErstellenCommand();
		command.setGewicht(gewicht);
		command.setWarenwert(warenwert);

		// Execute
		auftrag.erstellen(command);

		// Check
		var events = auftrag.getUncommittedEvents();

		assertThat(events).hasSize(2);
		assertThat(events).element(0).isInstanceOf(AuftragErstelltEvent.class);
		assertThat(events).element(1).isInstanceOf(VersicherungAngefordertEvent.class);
	}

	@Test
	void gewicht_ueberschritten() {
		// Precondition
		Auftrag auftrag = new Auftrag();

		// Command
		int gewicht = 3_001;
		BigDecimal warenwert = BigDecimal.valueOf(1_000);
		AuftragErstellenCommand command = new AuftragErstellenCommand();
		command.setGewicht(gewicht);
		command.setWarenwert(warenwert);

		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> auftrag.erstellen(command));
	}
}
