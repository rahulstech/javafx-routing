package rahulstech.jfx.routing.parser.converter;

import rahulstech.jfx.routing.parser.Attribute;
import rahulstech.jfx.routing.parser.AttributeValueConverter;

abstract class BaseAttributeValueConverter<T> extends AttributeValueConverter {

    @Override
    public T convert(Attribute attr) {
        String value = attr.getValue();
        if (isEmptyString(value)) {
            return null;
        }
        return parse(value);
    }

    protected abstract T parse(String value);
}
