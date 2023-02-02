package recipeo.exceptions;
/**
 * Exception to throw when a there is an invalid attribute changed in the RecipeoServiceS.
 */
public class InvalidAttributeChangeException extends RuntimeException {


    private static final long serialVersionUID = 4324979293375589407L;

    /**
     * Exception with no message or cause.
     */
    public InvalidAttributeChangeException() {
        super();
    }


    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public InvalidAttributeChangeException(String message) {
        super(message);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public InvalidAttributeChangeException(String message, Throwable cause) {
        super(message, cause);
    }


    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public InvalidAttributeChangeException(Throwable cause) {
        super(cause);
    }

}
