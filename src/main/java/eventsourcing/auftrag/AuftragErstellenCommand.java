package eventsourcing.auftrag;

import lombok.Data;

@Data
public class AuftragErstellenCommand {

	private Ladestelle beladestelle;

	private Ladestelle entladestelle;
}
