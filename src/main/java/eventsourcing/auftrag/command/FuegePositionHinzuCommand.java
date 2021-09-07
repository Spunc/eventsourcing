package eventsourcing.auftrag.command;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FuegePositionHinzuCommand {

	private String bezeichnung;

	private BigDecimal warenwert;
}
