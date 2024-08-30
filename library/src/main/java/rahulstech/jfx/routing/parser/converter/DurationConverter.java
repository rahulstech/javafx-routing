package rahulstech.jfx.routing.parser.converter;

import javafx.util.Duration;
import rahulstech.jfx.routing.element.RouterAnimation;
import rahulstech.jfx.routing.parser.ConverterException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code DurationConverter} class converts attribute values to {@link Duration} objects.
 * It extends {@link BaseAttributeValueConverter} and supports conversion from various formats of duration strings.
 * This includes durations specified in milliseconds, seconds, or named constants like "duration_short" and "duration_long".
 *
 * <p>Valid duration values are:</p>
 * <ul>
 *     <li>250ms</li>
 *     <li>2s</li>
 *     <li>.2s</li>
 *     <li>0.3s</li>
 *     <li>duration_short</li>
 *     <li>duration_long</li>
 * </ul>
 * Note: units and constant values are case insensative i.e. ms, MS, mS, Ms are all same
 * <p>Invalid duration values are:</p>
 *  <ul>
 *      <li>.2ms</li>
 *      <li>0.2ms</li>
 *  </ul>
 *
 * @author Rahul Bagchi
 * @since 1.0
 */
public class DurationConverter extends BaseAttributeValueConverter<Duration> {

    private static final String DURATION_PATTERN = "(\\d+)(ms)|(\\d*(\\.\\d+)?)(s)|(duration_short|duration_long)";

    private static DurationConverter INSTANCE;

    /**
     * Returns the singleton instance of the {@code DurationConverter}.
     *
     * @return the singleton instance of {@code DurationConverter}
     */
    public static DurationConverter getInstance() {
        if (null==INSTANCE) {
            INSTANCE = new DurationConverter();
        }
        return INSTANCE;
    }

    public DurationConverter() {}

    /** {@inheritDoc} */
    @Override
    public boolean check(String value) {
        return match(DURATION_PATTERN,value, Pattern.CASE_INSENSITIVE).matches();
    }

    /**
     * Parses the given {@code String} value to an object of type {@code Duration}.
     *
     * @param value the {@code String} value to parse
     * @return the parsed value of type {@code Duration}
     */
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
