package eventsourcing.auftrag.read;

import eventsourcing.base.NotFoundException;
import eventsourcing.base.ProjectionStore;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuftragReadController {

	private static final String PATH = "/auftrag";

	private final ProjectionStore<AuftragCurrentState> auftragCurrentStateProjection;

	@GetMapping(PATH + "/{id}")
	public AuftragCurrentState get(@PathVariable UUID id) {
		return auftragCurrentStateProjection.get(id).orElseThrow(() -> new NotFoundException(id));
	}
}
