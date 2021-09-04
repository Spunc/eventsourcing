package eventsourcing.auftrag;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class Auftrag {

	private int gewicht;

	private BigDecimal warenwert;

	private VersicherungsStatus versicherungsStatus = VersicherungsStatus.KEINE;

	public void erstellen(AuftragErstellenCommand command) {
		if (command.getGewicht() > 3_000) throw new IllegalArgumentException("Höchstgewicht überschritten");

		AuftragErstelltEvent erstelltEvent = new AuftragErstelltEvent();
		erstelltEvent.setGewicht(command.getGewicht());
		erstelltEvent.setWarenwert(command.getWarenwert());
		applyAndSave(erstelltEvent);

		boolean brauchtVersicherung = BigDecimal.valueOf(5_000).compareTo(command.getWarenwert()) <= 0;
		if (brauchtVersicherung)
			applyAndSave((new VersicherungAngefordertEvent()));
	}

	public void aendern(AuftragAendernCommand command) {
		if (command.getGewicht() > 3_000) throw new IllegalArgumentException("Höchstgewicht überschritten");

		AuftragGeaendertEvent geaendertEvent = new AuftragGeaendertEvent();
		geaendertEvent.setGewicht(command.getGewicht());
		geaendertEvent.setWarenwert(command.getWarenwert());
		applyAndSave(geaendertEvent);

		boolean brauchtVersicherung = BigDecimal.valueOf(5_000).compareTo(command.getWarenwert()) <= 0;
		if (brauchtVersicherung && versicherungsStatus == VersicherungsStatus.KEINE) {
			applyAndSave(new VersicherungAngefordertEvent());
		} else if (!brauchtVersicherung && versicherungsStatus != VersicherungsStatus.KEINE) {
			applyAndSave(new VersicherungStorniertEvent());
		}
	}

	public void apply(AuftragErstelltEvent event) {
		gewicht = event.getGewicht();
		warenwert = event.getWarenwert();
	}

	public void apply(AuftragGeaendertEvent event) {
		gewicht = event.getGewicht();
		warenwert = event.getWarenwert();
	}

	public void apply(VersicherungAngefordertEvent event) {
		versicherungsStatus = VersicherungsStatus.ANGEFORDERT;
	}

	public void apply(VersicherungStorniertEvent event) {
		versicherungsStatus = VersicherungsStatus.KEINE;
	}


	// -- Technik

	@Getter
	private final List<AuftragEvent> uncommittedEvents = new ArrayList<>();

	private void applyAndSave(AuftragEvent event) {
		event.accept(this);
		uncommittedEvents.add(event);
	}

	public void applyAll(List<AuftragEvent> events) {
		events.forEach(e -> e.accept(this));
	}
}
