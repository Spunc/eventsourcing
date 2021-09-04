package eventsourcing.auftrag;

import eventsourcing.Aggregate;
import java.math.BigDecimal;
import java.util.UUID;

public class Auftrag extends Aggregate<AuftragEvent> {

	private int gewicht;

	private BigDecimal warenwert;

	private VersicherungsStatus versicherungsStatus = VersicherungsStatus.KEINE;

	public Auftrag(UUID id) {
		super(id);
	}

	public void erstellen(AuftragErstellenCommand command) {
		if (command.getGewicht() > 3_000) throw new IllegalArgumentException("Höchstgewicht überschritten");

		AuftragErstelltEvent erstelltEvent = new AuftragErstelltEvent();
		erstelltEvent.setGewicht(command.getGewicht());
		erstelltEvent.setWarenwert(command.getWarenwert());
		applyAndSave(erstelltEvent);

		if (BigDecimal.valueOf(5_000).compareTo(command.getWarenwert()) <= 0)
			applyAndSave((new VersicherungAngefordertEvent()));
	}

	@Override
	protected void apply(AuftragEvent event) {
		event.accept(this);
	}

	public void apply(AuftragErstelltEvent event) {
		gewicht = event.getGewicht();
		warenwert = event.getWarenwert();
	}

	public void apply(VersicherungAngefordertEvent event) {
		versicherungsStatus = VersicherungsStatus.ANGEFORDERT;
	}
}
