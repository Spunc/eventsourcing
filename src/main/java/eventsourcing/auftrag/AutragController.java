package eventsourcing.auftrag;

import eventsourcing.auftrag.command.AendereAuftragCommand;
import eventsourcing.auftrag.command.ErstelleAuftragCommand;
import eventsourcing.auftrag.command.FuegePositionHinzuCommand;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

	@PutMapping(PATH + "/{id}")
	public ResponseEntity<Void> aendern(@PathVariable UUID id, @RequestBody AendereAuftragCommand command) {
		auftragCommandHandler.aendern(id, command);
		return ResponseEntity.noContent().build();
	}

	@PostMapping(PATH + "/{id}/position")
	public ResponseEntity<Void> fuegePositionHinzu(@PathVariable UUID id, @RequestBody FuegePositionHinzuCommand command) {
		var positionId = auftragCommandHandler.positionHinzufuegen(id, command);
		return ResponseEntity.created(URI.create(PATH + '/' + id + "/position/" + positionId)).build();
	}

}
