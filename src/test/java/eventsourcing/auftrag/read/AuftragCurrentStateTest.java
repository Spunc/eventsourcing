package eventsourcing.auftrag.read;

import static org.assertj.core.api.Assertions.assertThat;

import eventsourcing.auftrag.builder.AuftragErstelltEventBuilder;
import eventsourcing.auftrag.event.AuftragErstelltEvent;
import eventsourcing.auftrag.event.PositionHinzugefuegtEvent;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

class AuftragCurrentStateTest {

	@Test
	void auftragErstelltEvent() {
		ZonedDateTime beladezeit = ZonedDateTime.now().plusDays(1);
		ZonedDateTime entladezeit = ZonedDateTime.now().plusDays(2);
		String beladePlz = "12345";
		String entladePlz = "54321";
		eventsourcing.auftrag.domain.Ladestelle beladestelle = new eventsourcing.auftrag.domain.Ladestelle(beladePlz, beladezeit);
		eventsourcing.auftrag.domain.Ladestelle enladestelle = new eventsourcing.auftrag.domain.Ladestelle(entladePlz, entladezeit);

		AuftragErstelltEvent event = new AuftragErstelltEventBuilder()
				.mitBeladestelle(beladestelle)
				.mitEntladestelle(enladestelle).build();

		AuftragCurrentState currentState = new AuftragCurrentState();

		currentState.apply(event);

		SoftAssertions sa = new SoftAssertions();
		sa.assertThat(currentState.getBeladestelle().getLadzeit()).isEqualTo(beladezeit);
		sa.assertThat(currentState.getBeladestelle().getPlz()).isEqualTo(beladePlz);
		sa.assertThat(currentState.getEntladestelle().getLadzeit()).isEqualTo(entladezeit);
		sa.assertThat(currentState.getEntladestelle().getPlz()).isEqualTo(entladePlz);
		sa.assertAll();
	}

	@Test
	void positionHinzugefuegtEvent() {
		UUID positionId = UUID.randomUUID();
		String bezeichnung = "Gemischtwaren";
		BigDecimal warenwert = BigDecimal.valueOf(100_30, 2);
		eventsourcing.auftrag.domain.Position position = new eventsourcing.auftrag.domain.Position(positionId, bezeichnung, warenwert);

		PositionHinzugefuegtEvent event = new PositionHinzugefuegtEvent(position);

		AuftragCurrentState currentState = new AuftragCurrentState();

		currentState.apply(event);

		assertThat(currentState.getPositionen()).singleElement().satisfies(pos -> {
			assertThat(pos.getId()).isEqualTo(positionId);
			assertThat(pos.getBezeichnung()).isEqualTo(bezeichnung);
			assertThat(pos.getWarenwert()).isEqualTo("100,30\u00A0€");
		});
	}

}
