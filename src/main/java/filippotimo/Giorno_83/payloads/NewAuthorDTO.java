package filippotimo.Giorno_83.payloads;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record NewAuthorDTO(
        @NotBlank(message = "Il nome è obbligatorio")
        @Size(min = 2, max = 30, message = "Il nome deve contenere tra i 2 e i 30 caratteri")
        String nome,
        @NotBlank(message = "Il cognome è obbligatorio")
        @Size(min = 2, max = 30, message = "Il cognome deve contenere tra i 2 e i 30 caratteri")
        String cognome,
        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "L'indirizzo email inserito non è nel formato corretto!")
        String email,
        @NotNull(message = "La data di nascita è obbligatoria") // ! ! ! NON SI USA @NotBlank PER I LOCAL DATE ! ! !
        @Past
        LocalDate dataDiNascita
) {
}
