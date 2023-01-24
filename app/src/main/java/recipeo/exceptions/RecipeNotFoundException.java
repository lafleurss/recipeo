package recipeo.exceptions;
/**
 * Exception to throw when a given recipe ID is not found in the database.
 */
public class RecipeNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 5651563543130131784L;

    public RecipeNotFoundException() {
        super();
    }

    public RecipeNotFoundException(String message) {
        super(message);
    }

    public RecipeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecipeNotFoundException(Throwable cause) {
        super(cause);
    }

    public RecipeNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}