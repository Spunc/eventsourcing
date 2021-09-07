package eventsourcing.auftrag.command;

import java.util.UUID;
import lombok.Data;

@Data
public class LoeschePositionCommand {

	private UUID id;
}
