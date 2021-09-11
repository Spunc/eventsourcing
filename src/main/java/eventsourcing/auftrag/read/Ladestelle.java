package eventsourcing.auftrag.read;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ladestelle {

	private String plz;

	private ZonedDateTime ladzeit;
}
