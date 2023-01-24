package recipeo.exceptions;

public class InvalidAttributeChangeException extends RuntimeException{


    private static final long serialVersionUID = 4324979293375589407L;

    public InvalidAttributeChangeException() {
        super();
    }

    public InvalidAttributeChangeException(String message) {
        super(message);
    }

    public InvalidAttributeChangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAttributeChangeException(Throwable cause) {
        super(cause);
    }

    public InvalidAttributeChangeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
