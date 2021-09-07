package eventsourcing.auftrag.command;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class FuegePositionHinzuCommand {

	private String bezeichnung;

	private BigDecimal warenwert;
}
