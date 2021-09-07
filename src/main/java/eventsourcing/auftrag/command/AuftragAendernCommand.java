package eventsourcing.auftrag.command;

import eventsourcing.auftrag.domain.Ladestelle;
import lombok.Data;

@Data
public class AuftragAendernCommand {

	private Ladestelle beladestelle;

	private Ladestelle entladestelle;
}
