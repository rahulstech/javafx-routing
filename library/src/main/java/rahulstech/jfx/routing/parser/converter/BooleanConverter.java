package rahulstech.jfx.routing.parser.converter;

import rahulstech.jfx.routing.parser.AttributeValueConverter;
import rahulstech.jfx.routing.parser.ConverterException;

import java.util.regex.Pattern;


/**
 * The {@code BooleanConverter} class is a concrete implementation of the {@link AttributeValueConverter}
 * for converting attribute values to {@code Boolean} objects.
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
    public Boolean parse(String value) {
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
