package eventsourcing.auftrag;

import eventsourcing.auftrag.command.ErstelleAuftragCommand;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AutragController {

	private static final String PATH = "/auftrag";

	private final AuftragCommandHandler auftragCommandHandler;

	@PostMapping(PATH)
	public ResponseEntity<Void> erstellen(@RequestBody ErstelleAuftragCommand command) {
		var id = auftragCommandHandler.erstellen(command);
		return ResponseEntity.created(URI.create(PATH + '/' + id)).build();
	}
}
