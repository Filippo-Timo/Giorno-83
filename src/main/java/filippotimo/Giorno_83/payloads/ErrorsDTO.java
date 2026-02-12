package filippotimo.Giorno_83.payloads;

import java.time.LocalDateTime;

public record ErrorsDTO(String message, LocalDateTime timestamp) {
}

// Questo è un esempio di ErrorsDTO senza lista