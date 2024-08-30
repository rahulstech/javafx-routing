package rahulstech.jfx.routing.parser.converter;

import rahulstech.jfx.routing.parser.ConverterException;

/**
 * The {@code NumberConverter} class converts attribute values to {@link Number} objects.
 * It extends {@link BaseAttributeValueConverter} and supports conversion from string representations
 * of numeric values, including integers and floating-point numbers.
 *
 * @author Rahul Bagchi
 * @since 1.0
 */
public class NumberConverter extends BaseAttributeValueConverter<Number> {

    private static final String NUMBER_PATTERN = "(-)?\\d+(\\.\\d+)?";

    private static NumberConverter INSTANCE;

    /**
     * Returns the singleton instance of the {@code NumberConverter}.
     *
     * @return the singleton instance of {@code NumberConverter}
     */
    public static NumberConverter getInstance() {
        if (null==INSTANCE) {
            INSTANCE = new NumberConverter();
        }
        return INSTANCE;
    }

    public NumberConverter() {}

    /** {@inheritDoc} */
    @Override
    public boolean check(String value) {
        return match(NUMBER_PATTERN,value).matches();
    }

    /**
     * Parses the given {@code String} value to an object of type {@code Number}.
     *
     * @param value the {@code String} value to parse
     * @return the parsed value of type {@code Number}
     */
    @Override
    protected Number parse(String value) {
        if (!check(value)) {
            throw new ConverterException("can not convert "+value+" to number");
        }
        return Double.valueOf(value);
    }
}
