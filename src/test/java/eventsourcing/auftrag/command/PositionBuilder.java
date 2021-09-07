package eventsourcing.auftrag.command;

import eventsourcing.auftrag.domain.Position;
import java.math.BigDecimal;
import java.util.UUID;

public class PositionBuilder {

	private UUID id = UUID.randomUUID();

	private String bezeichnung = "Diverse Ware";

	private BigDecimal warenwert = BigDecimal.valueOf(1_000);

	public PositionBuilder mitId(UUID id) {
		this.id = id;
		return this;
	}

	public PositionBuilder mitBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
		return this;
	}

	public PositionBuilder mitWarenwert(BigDecimal warenwert) {
		this.warenwert = warenwert;
		return this;
	}

	public Position build() {
		return new Position(id, bezeichnung, warenwert);
	}

}
