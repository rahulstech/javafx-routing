package rahulstech.jfx.routing.util;

/**
 * A utility class for working with strings.
 *
 * @author Rahul Bagchi
 * @since 1.0
 */
public class StringUtil {

    private StringUtil() {}

    /**
     * Checks if a given string is empty.
     * <p>
     * A string is considered empty if it is {@code null} or
     * contains only whitespace characters.
     *
     * @param string the string to check
     * @return {@code true} if the string is {@code null} or contains
     *         only whitespace characters, {@code false} otherwise
     */
    public static boolean isEmpty(String string) {
        return null == string || string.trim().isEmpty();
    }
}
