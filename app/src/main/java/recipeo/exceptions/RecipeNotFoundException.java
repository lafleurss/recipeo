package recipeo.exceptions;
/**
 * Exception to throw when a given recipe ID is not found in the database.
 */
public class RecipeNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 5651563543130131784L;

    /**
     * Exception with no message or cause.
     */
    public RecipeNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public RecipeNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public RecipeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public RecipeNotFoundException(Throwable cause) {
        super(cause);
    }

}
