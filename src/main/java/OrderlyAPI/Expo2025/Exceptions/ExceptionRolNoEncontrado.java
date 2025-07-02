package OrderlyAPI.Expo2025.Exceptions;

public class ExceptionRolNoEncontrado extends RuntimeException {

    public ExceptionRolNoEncontrado(String message) {
        super(message);
    }

    public ExceptionRolNoEncontrado(String message, Throwable cause) {
        super(message, cause);
    }

    public ExceptionRolNoEncontrado(Throwable cause) {
        super(cause);
    }
}
