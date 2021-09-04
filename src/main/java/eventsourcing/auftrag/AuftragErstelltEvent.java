package eventsourcing.auftrag;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class AuftragErstelltEvent implements AuftragEvent {

	private int gewicht;

	private BigDecimal warenwert;

	@Override
	public void accept(Auftrag auftrag) {
		auftrag.apply(this);
	}
}
