package rahulstech.jfx.routing.parser.converter;

import rahulstech.jfx.routing.parser.ConverterException;

public class ClassConverter extends BaseAttributeValueConverter<Class<?>> {

    private static ClassConverter INSTANCE;

    public static ClassConverter getInstance() {
        if (null==INSTANCE) {
            INSTANCE = new ClassConverter();
        }
        return INSTANCE;
    }

    public ClassConverter() {}

    @Override
    public boolean check(String value) {
        try {
            Class.forName(value);
            return true;
        }
        catch (ClassNotFoundException ignore) {}
        return false;
    }

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
