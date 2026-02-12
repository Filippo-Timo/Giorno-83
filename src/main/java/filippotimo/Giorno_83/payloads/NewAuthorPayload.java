package filippotimo.Giorno_83.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@ToString
public class NewAuthorPayload {
    @NotBlank(message = "Il nome è obbligatorio")
    @Size(min = 2, max = 30, message = "Il nome deve contenere tra i 2 e i 30 caratteri")
    private String nome;
    @NotBlank(message = "Il cognome è obbligatorio")
    @Size(min = 2, max = 30, message = "Il cognome deve contenere tra i 2 e i 30 caratteri")
    private String cognome;
    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "L'indirizzo email inserito non è nel formato corretto!")
    private String email;
    @NotBlank(message = "La data di nascita è obbligatoria")
    private LocalDate dataDiNascita;
}

// Questo è un esempio di Payload senza usare una classe di tipo record (DTO)
