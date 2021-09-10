package eventsourcing;

import eventsourcing.auftrag.event.AuftragEvent;
import eventsourcing.base.EventStore;
import eventsourcing.base.InMemoryEventStore;
import eventsourcing.base.PublishingEventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class Config {

	@Autowired
	JmsTemplate jsmTempate;

	@Bean
	EventStore<AuftragEvent> auftagEventStore() {
		return new PublishingEventStore<>(AuftragEvent.class.getSimpleName(), new InMemoryEventStore<>(), jsmTempate);
	}
}
