package eventsourcing.base;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;

@RequiredArgsConstructor
public class PublishingEventStore<T extends Event> implements EventStore<T> {

	private final String destination;

	private final EventStore<T> eventStore;

	private final JmsTemplate jmsTemplate;


	@Override
	public List<T> get(UUID id) {
		return eventStore.get(id);
	}

	@Override
	public void append(UUID id, List<T> events) {
		eventStore.append(id, events);
		for (Event event : events) {
			jmsTemplate.convertAndSend(destination, new EventEnvelop(id, event));
		}

	}
}
