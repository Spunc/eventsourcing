package eventsourcing.auftrag.read;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Position {

	private UUID id;

	private String bezeichnung;

	private String warenwert;
}
