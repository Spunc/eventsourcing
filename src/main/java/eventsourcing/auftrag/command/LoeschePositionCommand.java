package eventsourcing.auftrag.command;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoeschePositionCommand {

	private UUID id;
}
