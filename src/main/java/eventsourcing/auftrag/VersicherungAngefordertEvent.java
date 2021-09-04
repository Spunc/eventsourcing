package eventsourcing.auftrag;

public class VersicherungAngefordertEvent implements AuftragEvent {

	@Override
	public void accept(Auftrag auftrag) {
		auftrag.apply(this);
	}
}
