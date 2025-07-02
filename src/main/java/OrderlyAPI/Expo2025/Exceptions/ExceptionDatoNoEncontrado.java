package OrderlyAPI.Expo2025.Exceptions;

public class ExceptionDatoNoEncontrado extends RuntimeException {

    public ExceptionDatoNoEncontrado(String message) {
        super(message);
    }

    public ExceptionDatoNoEncontrado(String message, Throwable cause) {
        super(message, cause);
    }

    public ExceptionDatoNoEncontrado(Throwable cause) {
        super(cause);
    }
}
