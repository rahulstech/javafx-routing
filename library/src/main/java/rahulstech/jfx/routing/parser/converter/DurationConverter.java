package rahulstech.jfx.routing.parser.converter;

import javafx.util.Duration;
import rahulstech.jfx.routing.element.RouterAnimation;
import rahulstech.jfx.routing.parser.ConverterException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationConverter extends BaseAttributeValueConverter<Duration> {

    private static final String DURATION_PATTERN = "(\\d+)(ms)|(\\d*(\\.\\d+)?)(s)|(duration_short|duration_long)";

    private static DurationConverter INSTANCE;

    public static DurationConverter getInstance() {
        if (null==INSTANCE) {
            INSTANCE = new DurationConverter();
        }
        return INSTANCE;
    }

    public DurationConverter() {}

    @Override
    public boolean check(String value) {
        return match(DURATION_PATTERN,value, Pattern.CASE_INSENSITIVE).matches();
    }

    @Override
    protected Duration parse(String value) {
        Matcher matcher = match(DURATION_PATTERN,value, Pattern.CASE_INSENSITIVE);
        if (!matcher.matches()) {
            throw new ConverterException("can not convert "+value+" to number");
        }
        if (null!=matcher.group(1)) {
            // it matches ms
            long number = Long.parseLong(matcher.group(1));
            return Duration.millis(number);
        }
        else if (null!=matcher.group(3)) {
            // it matches s
            double number = Double.parseDouble(matcher.group(3));
            return Duration.seconds(number);
        }
        else {
            String name = matcher.group(5);
            if ("duration_long".equals(name)) {
                return RouterAnimation.DEFAULT_DURATION_LONG;
            }
            else {
                return RouterAnimation.DEFAULT_DURATION_SHORT;
            }
        }
    }
}
