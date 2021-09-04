package eventsourcing.auftrag;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

public class AuftragAendernTest {

	@Test
	void auftragAendern_weiterhin_keine_Versicherung() {
		// Precondition
		AuftragErstelltEvent erstelltEvent = new AuftragErstelltEvent();
		erstelltEvent.setGewicht(1_000);
		erstelltEvent.setWarenwert(BigDecimal.valueOf(4_999));
		Auftrag auftrag = new Auftrag();
		auftrag.apply(erstelltEvent);

		// Command
		int gewicht = 2_000;
		BigDecimal warenwert = BigDecimal.valueOf(1_234);
		AuftragAendernCommand command = new AuftragAendernCommand();
		command.setGewicht(gewicht);
		command.setWarenwert(warenwert);

		// Execute
		auftrag.aendern(command);

		// Check
		var events = auftrag.getUncommittedEvents();

		assertThat(events).singleElement().isInstanceOfSatisfying(AuftragGeaendertEvent.class, e -> {
			assertThat(e.getGewicht()).isEqualTo(gewicht);
			assertThat(e.getWarenwert()).isEqualTo(warenwert);
		});
	}

	@Test
	void auftragAendern_weiterhin_Versicherung() {
		// Precondition
		AuftragErstelltEvent erstelltEvent = new AuftragErstelltEvent();
		erstelltEvent.setGewicht(1_000);
		erstelltEvent.setWarenwert(BigDecimal.valueOf(5_000));

		VersicherungAngefordertEvent versicherungAngefordertEvent = new VersicherungAngefordertEvent();

		Auftrag auftrag = new Auftrag();
		auftrag.applyAll(List.of(erstelltEvent, versicherungAngefordertEvent));

		// Command
		int gewicht = 2_000;
		BigDecimal warenwert = BigDecimal.valueOf(5_500);
		AuftragAendernCommand command = new AuftragAendernCommand();
		command.setGewicht(gewicht);
		command.setWarenwert(warenwert);

		// Execute
		auftrag.aendern(command);

		var events = auftrag.getUncommittedEvents();

		assertThat(events).singleElement().isInstanceOfSatisfying(AuftragGeaendertEvent.class, e -> {
			assertThat(e.getGewicht()).isEqualTo(gewicht);
			assertThat(e.getWarenwert()).isEqualTo(warenwert);
		});
	}
}
