package eventsourcing.base;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventEnvelop<T extends Event> {

	private UUID id;

	private T event;
}
