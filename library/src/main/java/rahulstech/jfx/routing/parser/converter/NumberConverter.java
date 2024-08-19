package rahulstech.jfx.routing.parser.converter;

import rahulstech.jfx.routing.parser.ConverterException;

public class NumberConverter extends BaseAttributeValueConverter<Number> {

    private static final String NUMBER_PATTERN = "(-)?\\d+(\\.\\d+)?";

    private static NumberConverter INSTANCE;

    public static NumberConverter getInstance() {
        if (null==INSTANCE) {
            INSTANCE = new NumberConverter();
        }
        return INSTANCE;
    }

    public NumberConverter() {}

    @Override
    public boolean check(String value) {
        return match(NUMBER_PATTERN,value).matches();
    }

    @Override
    protected Number parse(String value) {
        if (!check(value)) {
            throw new ConverterException("can not convert "+value+" to number");
        }
        return Double.valueOf(value);
    }
}
