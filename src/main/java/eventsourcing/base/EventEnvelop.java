package eventsourcing.base;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventEnvelop<T extends Event> {

	private UUID id;

	private T event;
}
