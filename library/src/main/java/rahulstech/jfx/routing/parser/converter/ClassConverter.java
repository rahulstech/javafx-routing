package rahulstech.jfx.routing.parser.converter;

import rahulstech.jfx.routing.parser.ConverterException;

/**
 * The {@code ClassConverter} class is a singleton converter that converts a {@code String} representing
 * a fully qualified class name into a {@code Class<?>} object. It also provides a method to check
 * whether a given string can be resolved to a valid class.
 *
 * @author Rahul Bagchi
 * @since 1.0
 */
public class ClassConverter extends BaseAttributeValueConverter<Class<?>> {

    private static ClassConverter INSTANCE;

    /**
     * Returns the singleton instance of the {@code ClassConverter}.
     *
     * @return the singleton instance of {@code ClassConverter}
     */
    public static ClassConverter getInstance() {
        if (null==INSTANCE) {
            INSTANCE = new ClassConverter();
        }
        return INSTANCE;
    }

    public ClassConverter() {}

    /** {@inheritDoc} */
    @Override
    public boolean check(String value) {
        try {
            Class.forName(value);
            return true;
        }
        catch (ClassNotFoundException ignore) {}
        return false;
    }

    /**
     * Parses the given {@code String} value to an object of type {@code Class}.
     *
     * @param value the {@code String} value to parse
     * @return the parsed value of type {@code Class}
     */
    @Override
    protected Class<?> parse(String value) {
        try {
            return Class.forName(value);
        }
        catch (ClassNotFoundException ex) {
            throw new ConverterException("can not convert '"+value+"' to Class");
        }
    }
}
