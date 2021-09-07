package eventsourcing.auftrag.command;

import eventsourcing.auftrag.domain.Ladestelle;
import lombok.Data;

@Data
public class ErstelleAuftragCommand {

	private Ladestelle beladestelle;

	private Ladestelle entladestelle;
}
