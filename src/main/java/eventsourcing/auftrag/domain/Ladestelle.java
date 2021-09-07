package eventsourcing.auftrag.domain;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ladestelle {

	String plz;

	ZonedDateTime ladezeit;
}
