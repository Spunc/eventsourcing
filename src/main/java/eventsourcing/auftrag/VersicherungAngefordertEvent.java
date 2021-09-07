package eventsourcing.auftrag;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VersicherungAngefordertEvent implements AuftragEvent {

	private BigDecimal gesamtWarenwert;

	@Override
	public void accept(Auftrag auftrag) {
		auftrag.apply(this);
	}
}
