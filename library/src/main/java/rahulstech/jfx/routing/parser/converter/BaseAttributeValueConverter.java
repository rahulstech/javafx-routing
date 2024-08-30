package rahulstech.jfx.routing.parser.converter;

import rahulstech.jfx.routing.parser.Attribute;
import rahulstech.jfx.routing.parser.AttributeValueConverter;

/**
 * The {@code BaseAttributeValueConverter} class provides a base implementation for converting attribute values
 * from {@link Attribute} objects to a specific type {@code T}. This class extends {@link AttributeValueConverter}
 * and provides a framework for concrete converters to implement their specific parsing logic.
 *
 * @param <T> the type of the value that will be converted
 *
 * @author Rahul Bagchi
 * @since 1.0
 */
abstract class BaseAttributeValueConverter<T> extends AttributeValueConverter {

    /** {@inheritDoc} */
    @Override
    public T convert(Attribute attr) {
        String value = attr.getValue();
        if (isEmptyString(value)) {
            return null;
        }
        return parse(value);
    }

    /**
     * Parses the given {@code String} value to an object of type {@code T}.
     *
     * <p>This method must be implemented by subclasses to define the specific parsing logic for converting
     * the string value to the desired type.</p>
     *
     * @param value the {@code String} value to parse
     * @return the parsed value of type {@code T}
     */
    protected abstract T parse(String value);
}
