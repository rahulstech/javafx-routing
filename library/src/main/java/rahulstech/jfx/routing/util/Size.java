package rahulstech.jfx.routing.util;

@SuppressWarnings("unused")
public class Size {

    public static final String PERCENT = "%";

    public static final String PERCENT_PARENT = "%p";

    public static final String PIXEL = "px";

    public static final String DEGREE = "deg";

    public static final String RADIAN = "rad";

    private final double value;

    private final String unit;

    public Size(double value, String unit) {
        if (null==unit) {
            throw new IllegalArgumentException("size unit required");
        }
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public static boolean checkSameUnit(Size a, Size b) {
        return a.unit.equals(b.unit);
    }

    public static String unitFor(String text) {
        if (null==text) {
            return null;
        }
        String trimmed = text.trim();
        switch (trimmed) {
            case PERCENT: return PERCENT;
            case PERCENT_PARENT: return PERCENT_PARENT;
            case PIXEL: return PIXEL;
            case DEGREE: return DEGREE;
            case RADIAN: return RADIAN;
            default: return null;
        }
    }

    @Override
    public String toString() {
        return "Size{" +
                "value=" + value +
                ", unit='" + unit + '\'' +
                '}';
    }
}
