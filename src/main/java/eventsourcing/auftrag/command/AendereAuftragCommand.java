package eventsourcing.auftrag.command;

import eventsourcing.auftrag.domain.Ladestelle;
import lombok.Data;

@Data
public class AendereAuftragCommand {

	private Ladestelle beladestelle;

	private Ladestelle entladestelle;
}
