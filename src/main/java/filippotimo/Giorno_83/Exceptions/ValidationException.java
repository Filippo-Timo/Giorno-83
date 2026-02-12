package filippotimo.Giorno_83.Exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {

    private List<String> errorsMessages;

    public ValidationException(List<String> errorsMessages) {
        super("ATTENZIONE! errori nel payload");
        this.errorsMessages = errorsMessages;
    }
}
