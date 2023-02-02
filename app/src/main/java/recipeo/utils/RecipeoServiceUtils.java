package recipeo.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;


public final class RecipeoServiceUtils {

    private static final int PLAYLIST_ID_LENGTH = 5;
    private static final Pattern INVALID_CHARACTER_PATTERN = Pattern.compile("[\"'\\\\]");


    private RecipeoServiceUtils() {
    }

    /**
     * Method to validate is a given string has invalid characters.
     * @param stringToValidate String
     * @return a boolean indicating if the string is valid(true) or not(False)
     */
    public static boolean isValidString(String stringToValidate) {
        if (StringUtils.isBlank(stringToValidate)) {
            return false;
        } else {
            return !INVALID_CHARACTER_PATTERN.matcher(stringToValidate).find();
        }
    }

    /**
     * Method to generate a random alphanumeric recipeId.
     * @return a String composed of an alphanumeric of length 5
     */
    public static String generateRecipeId() {
        return RandomStringUtils.randomAlphanumeric(5);
    }
}
