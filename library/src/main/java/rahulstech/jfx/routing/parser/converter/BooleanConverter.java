package rahulstech.jfx.routing.parser.converter;

import rahulstech.jfx.routing.parser.ConverterException;

import java.util.regex.Pattern;

public class BooleanConverter extends BaseAttributeValueConverter<Boolean> {

    private static BooleanConverter INSTANCE;

    public static BooleanConverter getInstance() {
        if (null==INSTANCE) {
            INSTANCE = new BooleanConverter();
        }
        return INSTANCE;
    }

    @Override
    protected Boolean parse(String value) {
        if (!check(value)) {
            throw new ConverterException("can not convert '"+value+"' to boolean");
        }
        return Boolean.parseBoolean(value);
    }

    @Override
    public boolean check(String value) {
        return match("(true|false)",value, Pattern.CASE_INSENSITIVE).matches();
    }
}
