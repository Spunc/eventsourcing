package eventsourcing.auftrag.domain;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
public class Position {

	private UUID id;

	private String bezeichnung;

	private BigDecimal warenwert;
}
