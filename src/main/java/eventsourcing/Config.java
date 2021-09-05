package eventsourcing;

import eventsourcing.auftrag.AuftragEvent;
import eventsourcing.base.EventStore;
import eventsourcing.base.InMemoryEventStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

	@Bean
	EventStore<AuftragEvent> auftragEventStore() {
		return new InMemoryEventStore<>();
	}
}
