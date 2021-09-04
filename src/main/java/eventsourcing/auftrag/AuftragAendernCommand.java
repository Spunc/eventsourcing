package eventsourcing.auftrag;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class AuftragAendernCommand {

	private int gewicht;

	private BigDecimal warenwert;
}
