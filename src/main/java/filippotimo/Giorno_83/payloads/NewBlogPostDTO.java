package filippotimo.Giorno_83.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewBlogPostDTO(
        @NotBlank(message = "La categoria è obbligatoria")
        @Size(min = 2, max = 30, message = "La categoria deve contenere tra i 2 e i 30 caratteri")
        String categoria,
        @NotBlank(message = "Il titolo è obbligatorio")
        @Size(min = 2, max = 50, message = "Il titolo deve contenere tra i 2 e i 50 caratteri")
        String titolo,
        @NotBlank(message = "Il contenuto è obbligatorio")
        @Size(max = 500, message = "Limite massimo di 500 caratteri raggiunto")
        String contenuto,
        int tempoDiLettura,
        @NotNull(message = "L'ID dell'autore è obbligatorio")
        Long autohorId
) {
}
