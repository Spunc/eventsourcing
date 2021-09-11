package eventsourcing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eventsourcing.auftrag.event.AuftragEvent;
import eventsourcing.base.EventStore;
import eventsourcing.base.InMemoryEventStore;
import eventsourcing.base.PublishingEventStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;

@Configuration
public class Config {

	public static final String AUFTRAG_TOPIC = "Auftrag";

	@Bean
	Jackson2ObjectMapperBuilder jsonConfig() {
		return new Jackson2ObjectMapperBuilder().modules(new JavaTimeModule());
	}

	@Bean
	EventStore<AuftragEvent> auftagEventStore(JmsTemplate jmsTemplate) {
		return new PublishingEventStore<>(AUFTRAG_TOPIC, new InMemoryEventStore<>(), jmsTemplate);
	}

	@Bean
	MessageConverter jacksonMessageConverter(ObjectMapper objectMapper) {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setObjectMapper(objectMapper);
		converter.setTypeIdPropertyName("__type");
		return converter;
	}
}
