package rahulstech.jfx.routing.parser.converter;

import rahulstech.jfx.routing.parser.ConverterException;
import rahulstech.jfx.routing.util.Size;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SizeConverter extends BaseAttributeValueConverter<Size> {

    private static final String SIZE_PATTERN = "(-?(\\d+|\\d*\\.\\d+))(%|%p|deg|px|rad)";

    private static SizeConverter INSTANCE;

    public static SizeConverter getInstance() {
        if (null==INSTANCE) {
            INSTANCE = new SizeConverter();
        }
        return INSTANCE;
    }

    @Override
    protected Size parse(String value) {
        Matcher matcher = match(SIZE_PATTERN,value,Pattern.CASE_INSENSITIVE);
        if (!matcher.matches()) {
            throw new ConverterException("can not convert '"+value+"' to Size");
        }
        double number = Double.parseDouble(matcher.group(1));
        String unit = Size.unitFor(matcher.group(3));
        return new Size(number,unit);
    }

    @Override
    public boolean check(String value) {
        return match(SIZE_PATTERN,value, Pattern.CASE_INSENSITIVE).matches();
    }
}
