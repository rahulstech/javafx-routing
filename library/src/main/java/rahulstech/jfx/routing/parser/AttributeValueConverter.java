package rahulstech.jfx.routing.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code AttributeValueConverter} class is an abstract class designed to convert and validate
 * attribute values. This class provides methods for checking empty strings, validating values, and
 * matching regular expressions.
 *
 * <p>Subclasses should implement the {@link #convert(Attribute)} method to convert attribute values
 * to the desired type, and the {@link #check(String)} method to validate the attribute values.</p>
 *
 * @author Rahul Bagchi
 * @since 1.0
 */
public abstract class AttributeValueConverter {

    /**
     * Converts the given {@link Attribute} to an object of the desired type.
     *
     * <p>Subclasses must provide an implementation for this method to define how attribute values are
     * converted.</p>
     *
     * @param attr the {@link Attribute} to convert
     * @return the converted object
     */
    public abstract Object convert(Attribute attr);

    /**
     * Checks if the given string is empty. A string is considered empty if it is {@code null} or
     * contains only whitespace characters.
     *
     * @param text the string to check
     * @return {@code true} if the string is {@code null} or empty, {@code false} otherwise
     */
    public boolean isEmptyString(String text) {
        return null==text || text.trim().isEmpty();
    }

    /**
     * Validates the given value. Subclasses must provide an implementation for this method to define
     * how attribute values are validated.
     *
     * @param value the value to check
     * @return {@code true} if the value is valid, {@code false} otherwise
     */
    public abstract boolean check(String value);

    /**
     * Creates a {@link Matcher} object by compiling the given regular expression and matching it against
     * the provided test string.
     *
     * @param regex the regular expression to compile
     * @param test  the string to match against the regular expression
     * @return a {@link Matcher} object for the given regular expression and test string
     */
    public static Matcher match(String regex, String test) {
        return match(regex,test,0);
    }

    /**
     * Creates a {@link Matcher} object by compiling the given regular expression and matching it against
     * the provided test string with pattern matching flags.
     *
     * @param regex the regular expression to compile
     * @param test  the string to match against the regular expression
     * @param flag  the pattern matching flags (e.g., {@link Pattern#CASE_INSENSITIVE})
     * @return a {@link Matcher} object for the given regular expression and test string
     */
    public static Matcher match(String regex, String test, int flag) {
        Pattern pattern = 0==flag ? Pattern.compile(regex) : Pattern.compile(regex,flag);
        return pattern.matcher(test);
    }
}
