package eventsourcing.auftrag.domain;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ladestelle {

	String plz;

	ZonedDateTime ladezeit;
}
