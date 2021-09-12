package eventsourcing;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eventsourcing.auftrag.event.AuftragEvent;
import eventsourcing.auftrag.read.AuftragCurrentState;
import eventsourcing.base.EventStore;
import eventsourcing.base.InMemoryEventStore;
import eventsourcing.base.InMemoryProjectionStore;
import eventsourcing.base.ProjectionStore;
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
		return new Jackson2ObjectMapperBuilder()
				.modules(new JavaTimeModule())
				.featuresToDisable(
						SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
						DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
	}

	@Bean
	MessageConverter jacksonMessageConverter(ObjectMapper objectMapper) {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setObjectMapper(objectMapper);
		converter.setTypeIdPropertyName("__type");
		return converter;
	}

	@Bean
	EventStore<AuftragEvent> auftagEventStore(JmsTemplate jmsTemplate) {
		return new PublishingEventStore<>(AUFTRAG_TOPIC, new InMemoryEventStore<>(), jmsTemplate);
	}

	@Bean
	ProjectionStore<AuftragCurrentState> auftragCurrentStateProjectionStore() {
		return new InMemoryProjectionStore<>("AuftragCurrentState");
	}
}
