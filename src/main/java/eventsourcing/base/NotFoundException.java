package eventsourcing.base;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
	public NotFoundException(UUID id) {
		super(String.format("Resource id=%s nicht gefunden", id));
	}
}
