package rahulstech.jfx.routing.parser.converter;

import rahulstech.jfx.routing.parser.ConverterException;
import rahulstech.jfx.routing.util.Size;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code SizeConverter} class converts attribute values to {@link Size} objects.
 * It extends {@link BaseAttributeValueConverter} and supports conversion from string representations
 * of sizes, which may include units such as percentages, percentage relative to parent (%p),
 * pixels, degrees, and radians.
 * <p>Valid values example:
 * <ul>
 *     <li>32%</li>
 *     <li>32.5%</li>
 *     <li>0.5%</li>
 *     <li>32.5%p</li>
 *     <li>32%p</li>
 *     <li>32.5%p</li>
 *     <li>0.5%p</li>
 *     <li>56px</li>
 *     <li>56px</li>
 *     <li>56.5px</li>
 *     <li>0.56px</li>
 *     <li>56deg</li>
 *     <li>0.56deg</li>
 *     <li>.56deg</li>
 *     <li>56rad</li>
 *     <li>0.rad</li>
 *     <li>.56rad</li>
 * </ul>
 * Note: units are case insensative i.e. px, PX, pX, Px all are same.
 * <br/>
 * Note: space between the number and unit is not allowrd. For example 56 px is invalid.
 * </p>
 *
 * @author Rahul Bagchi
 * @since 1.0
 */
public class SizeConverter extends BaseAttributeValueConverter<Size> {

    private static final String SIZE_PATTERN = "(-?(\\d+|\\d*\\.\\d+))(%|%p|deg|px|rad)";

    private static SizeConverter INSTANCE;

    /**
     * Returns the singleton instance of the {@code SizeConverter}.
     *
     * @return the singleton instance of {@code SizeConverter}
     */
    public static SizeConverter getInstance() {
        if (null==INSTANCE) {
            INSTANCE = new SizeConverter();
        }
        return INSTANCE;
    }

    public SizeConverter() {}

    /**
     * Parses the given {@code String} value to an object of type {@link  Size}.
     *
     * @param value the {@code String} value to parse
     * @return the parsed value of type {@link Size}
     */
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

    /** {@inheritDoc} */
    @Override
    public boolean check(String value) {
        return match(SIZE_PATTERN,value, Pattern.CASE_INSENSITIVE).matches();
    }
}
