package eventsourcing.auftrag;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class AuftragErstellenCommand {

	private int gewicht;

	private BigDecimal warenwert;

}
