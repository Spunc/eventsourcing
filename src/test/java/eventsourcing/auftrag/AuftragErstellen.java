package eventsourcing.auftrag;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class AuftragErstellen {

	@Test
	void erstelle_auftrag_ohne_versicherung() {
		int gewicht = 2_000;
		BigDecimal warenwert = BigDecimal.valueOf(4_999);

		AuftragErstellenCommand command = new AuftragErstellenCommand();
		command.setGewicht(gewicht);
		command.setWarenwert(warenwert);

		Auftrag auftrag = new Auftrag(UUID.randomUUID());
		auftrag.erstellen(command);

		var events = auftrag.getUncommittedEvents();

		assertThat(events).singleElement().isInstanceOfSatisfying(AuftragErstelltEvent.class, e -> {
			assertThat(e.getGewicht()).isEqualTo(gewicht);
			assertThat(e.getWarenwert()).isEqualTo(warenwert);
		});
	}

	@Test
	void erstelle_mit_versicherung() {
		int gewicht = 2_000;
		BigDecimal warenwert = BigDecimal.valueOf(5_000);

		AuftragErstellenCommand command = new AuftragErstellenCommand();
		command.setGewicht(gewicht);
		command.setWarenwert(warenwert);

		Auftrag auftrag = new Auftrag(UUID.randomUUID());
		auftrag.erstellen(command);

		var events = auftrag.getUncommittedEvents();

		assertThat(events).hasSize(2);
		assertThat(events).element(0).isInstanceOf(AuftragErstelltEvent.class);
		assertThat(events).element(1).isInstanceOf(VersicherungAngefordertEvent.class);
	}
}
