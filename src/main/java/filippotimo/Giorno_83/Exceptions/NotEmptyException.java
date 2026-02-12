package filippotimo.Giorno_83.Exceptions;

public class NotEmptyException extends RuntimeException {
    public NotEmptyException() {
        super("Il file non puo' essere vuoto!");
    }
}
