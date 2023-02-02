package recipeo.exceptions;
/**
 * Exception to throw when a given category name is not found in the database.
 */
public class CategoryNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 907515573029951977L;

    /**
     * Exception with no message or cause.
     */
    public CategoryNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public CategoryNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public CategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public CategoryNotFoundException(Throwable cause) {
        super(cause);
    }

}
