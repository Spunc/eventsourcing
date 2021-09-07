package eventsourcing.auftrag;

import lombok.Data;

@Data
public class AuftragAendernCommand {

	private Ladestelle beladestelle;

	private Ladestelle entladestelle;
}
