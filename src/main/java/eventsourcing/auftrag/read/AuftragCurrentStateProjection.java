package eventsourcing.auftrag.read;

import eventsourcing.Config;
import eventsourcing.base.EventEnvelop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuftragCurrentStateProjection {

	@JmsListener(destination = Config.AUFTRAG_TOPIC)
	public void onAuftragEvent(EventEnvelop<?> event) {
		log.info(event.toString());
	}

}
