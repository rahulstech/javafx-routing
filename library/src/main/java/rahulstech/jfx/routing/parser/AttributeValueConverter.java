package rahulstech.jfx.routing.parser;

import rahulstech.jfx.routing.util.StringUtil;

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
     * Creates new {@code AttributeValueConverter} instance
     */
    public AttributeValueConverter() {}

    /**
     * Converts the given {@link Attribute} value to an object of the desired type.
     *
     * @param attr the {@link Attribute} value to convert
     * @return the converted object
     */
    public Object convert(Attribute attr) {
        String value = attr.getValue();
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        return parse(value);
    }

    /**
     * Parses the given {@code String} value to an object
     *
     * <p>This method must be implemented by subclasses to define the specific parsing logic for converting
     * the string value to the desired type.
     *
     * @param value the {@code String} value to parse
     * @return the parsed value
     */
    public abstract Object parse(String value);

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
