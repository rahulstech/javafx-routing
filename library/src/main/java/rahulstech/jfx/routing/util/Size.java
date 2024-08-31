package rahulstech.jfx.routing.util;

/**
 * A class representing a size with a value and a unit.
 * The {@code Size} class provides a way to represent sizes using different
 * units such as percentage, pixels, degrees, and radians. It also provides
 * methods for unit checking and conversion.
 *
 * @author Rahul Bagchi
 * @since 1.0
 */
public class Size {

    /**
     * Constant representing the percentage unit ("%").
     */
    public static final String PERCENT = "%";

    /**
     * Constant representing the percentage of parent unit ("%p").
     */
    public static final String PERCENT_PARENT = "%p";

    /**
     * Constant representing the pixel unit ("px").
     */
    public static final String PIXEL = "px";

    /**
     * Constant representing the degree unit ("deg").
     */
    public static final String DEGREE = "deg";

    /**
     * Constant representing the radian unit ("rad").
     */
    public static final String RADIAN = "rad";

    private final double value;

    private final String unit;

    /**
     * Constructs a new {@code Size} object with the specified value and unit.
     *
     * @param value the numerical value of the size
     * @param unit  the unit of the size; must not be {@code null}
     * @throws IllegalArgumentException if the unit is {@code null}
     */
    public Size(double value, String unit) {
        if (null == unit) {
            throw new IllegalArgumentException("size unit required");
        }
        this.value = value;
        this.unit = unit;
    }

    /**
     * Returns the numerical value of the size.
     *
     * @return the value of the size
     */
    public double getValue() {
        return value;
    }

    /**
     * Returns the unit of the size.
     *
     * @return the unit of the size
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Checks if two {@code Size} objects have the same unit.
     *
     * @param a the first {@code Size} object to compare
     * @param b the second {@code Size} object to compare
     * @return {@code true} if both {@code Size} objects have the same unit,
     *         {@code false} otherwise
     */
    public static boolean checkSameUnit(Size a, Size b) {
        return a.unit.equals(b.unit);
    }

    /**
     * Determines the unit for a given string representation.
     * The method will return the corresponding unit constant if the input
     * string matches any of the defined units.
     *
     * @param text the string representation of the unit
     * @return the corresponding unit constant if the input matches a known unit,
     *         {@code null} otherwise
     * @throws IllegalArgumentException invalid or unknown unit representation
     */
    public static String unitFor(String text) {
        if (null == text) {
            return null;
        }
        String trimmed = text.trim();
        switch (trimmed) {
            case PERCENT: return PERCENT;
            case PERCENT_PARENT: return PERCENT_PARENT;
            case PIXEL: return PIXEL;
            case DEGREE: return DEGREE;
            case RADIAN: return RADIAN;
        }
        throw new IllegalArgumentException("'"+text+"' is not a valid unit");
    }

    @Override
    public String toString() {
        return "Size{" +
                "value=" + value +
                ", unit='" + unit + '\'' +
                '}';
    }
}
