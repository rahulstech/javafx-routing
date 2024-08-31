package rahulstech.jfx.routing.parser.converter;

import rahulstech.jfx.routing.parser.ConverterException;

import java.util.regex.Pattern;


/**
 * The {@code BooleanConverter} class is a concrete implementation of the {@link BaseAttributeValueConverter}
 * for converting {@link String} values to {@code Boolean} values.
 *
 * <p>This class follows the Singleton design pattern to ensure only one instance is created. It provides
 * methods to convert a string representation of a boolean value and validate it.</p>
 *
 * @author Rahul Bagchi
 * @since 1.0
 */
public class BooleanConverter extends BaseAttributeValueConverter<Boolean> {

    private static BooleanConverter INSTANCE;

    /**
     * Returns the singleton instance of the {@code BooleanConverter}.
     *
     * @return the singleton instance of {@code BooleanConverter}
     */
    public static BooleanConverter getInstance() {
        if (null==INSTANCE) {
            INSTANCE = new BooleanConverter();
        }
        return INSTANCE;
    }

    /**
     * Creates new {@code BooleanConverter} instance
     */
    public BooleanConverter() {}

    /**
     * Parses the given {@code String} value to an object of type {@code Boolean}.
     *
     * @param value the {@code String} value to parse
     * @return the parsed value of type {@code Boolean}
     */
    @Override
    protected Boolean parse(String value) {
        if (!check(value)) {
            throw new ConverterException("can not convert '"+value+"' to boolean");
        }
        return Boolean.parseBoolean(value);
    }

    /** {@inheritDoc} */
    @Override
    public boolean check(String value) {
        return match("(true|false)",value, Pattern.CASE_INSENSITIVE).matches();
    }
}
