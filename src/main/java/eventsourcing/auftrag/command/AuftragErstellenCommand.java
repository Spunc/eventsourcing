package eventsourcing.auftrag.command;

import eventsourcing.auftrag.domain.Ladestelle;
import lombok.Data;

@Data
public class AuftragErstellenCommand {

	private Ladestelle beladestelle;

	private Ladestelle entladestelle;
}
