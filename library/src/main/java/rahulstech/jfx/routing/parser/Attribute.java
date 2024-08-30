package rahulstech.jfx.routing.parser;

import javafx.util.Duration;
import rahulstech.jfx.routing.parser.converter.*;
import rahulstech.jfx.routing.util.Size;

import java.util.Objects;

/**
 * The {@code Attribute} class represents an XML attribute in the context of a routing configuration.
 * It provides methods to retrieve the attribute's value in various formats, such as {@code int}, {@code long},
 * {@code float}, {@code double}, {@link Duration}, {@link Size}, and {@link Class}.
 * The class also supports checking the type of the attribute's value and offers several common constants
 * for attribute names and types.
 *
 * @author Rahul Bagchi
 * @since 1.0
 */
public class Attribute {

    public static final int TYPE_CLASS = 1;

    public static final int TYPE_DURATION = 2;

    public static final int TYPE_NUMBER = 3;

    public static final int TYPE_SIZE = 5;

    public static final int TYPE_STRING = 6;

    public static final int TYPE_BOOLEAN = 7;

    // common attributes
    public static final String ID = "id";
    public static final  String NAME = "name";
    public static final String TYPE = "type";

    // attributes for Router
    public static final  String HOME = "home";
    public static final  String HOME_ENTER_ANIMATION = "homeEnterAnimation";
    public static final  String ENTER_ANIMATION = "enterAnimation";
    public static final  String EXIT_ANIMATION = "exitAnimation";
    public static final  String POP_ENTER_ANIMATION = "popEnterAnimation";
    public static final  String POP_EXIT_ANIMATION = "popExitAnimation";

    // attributes RouterAnimation
    public static final  String DURATION = "duration";
    public static final String AUTO_RESET = "autoReset";
    public static final String FROM_ALPHA = "fromAlpha";
    public static final String TO_ALPHA = "toAlpha";
    public static final  String FROM_X_SCALE = "fromXScale";
    public static final  String TO_X_SCALE = "toXScale";
    public static final  String FROM_Y_SCALE = "fromYScale";
    public static final  String TO_Y_SCALE = "toYScale";
    public static final  String FROM_X_TRANSLATE = "fromXTranslate";
    public static final  String TO_X_TRANSLATE = "toXTranslate";
    public static final  String FROM_Y_TRANSLATE = "fromYTranslate";
    public static final  String TO_Y_TRANSLATE = "toYTranslate";
    public static final  String FROM_X_ROTATION = "fromXRotation";
    public static final  String TO_X_ROTATION = "toXRotation";
    public static final  String FROM_Y_ROTATION = "fromYRotation";
    public static final  String TO_Y_ROTATION = "toYRotation";

    // attribute RouterCompoundAnimation
    public static final String PLAY_MODE = "playMode";

    // attributes for Destination
    public static final  String CONTROLLER_CLASS = "controllerClass";
    public static final  String EXECUTOR = "executor";
    public static final  String FXML = "fxml";
    public static final  String TITLE = "title";
    public static final String ARGUMENTS = "arguments";

    // attribute for argument
    public static final String REQUIRED = "required";

    private final String namespace;

    private final String name;

    private final String value;

    /**
     * Created an {@code Attribute} with the specified name and value, using the default namespace.
     *
     * @param name  the name of the attribute
     * @param value the value of the attribute
     */
    public Attribute(String name, String value) {
        this(RouterXmlParser.DEFAULT_NAMESPACE,name,value);
    }

    /**
     * Creates an {@code Attribute} with the specified namespace, name, and value.
     *
     * @param namespace the namespace of the attribute
     * @param name      the name of the attribute
     * @param value     the value of the attribute
     */
    public Attribute(String namespace, String name, String value) {
        this.namespace = namespace;
        this.name = name;
        this.value = value;
    }

    /**
     * Returns the namespace of the attribute.
     *
     * @return the namespace of the attribute
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Checks if the attribute uses the default namespace.
     *
     * @return {@code true} if the attribute uses the default namespace, {@code false} otherwise
     */
    public boolean isDefaultNamespace() {
        return RouterXmlParser.DEFAULT_NAMESPACE.equals(namespace);
    }

    /**
     * Returns the name of the attribute.
     *
     * @return the name of the attribute
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the value of the attribute.
     *
     * @return the value of the attribute
     */
    public String getValue() {
        return value;
    }

    /**
     * Converts the value of this {@code Attribute} using the provided {@link AttributeValueConverter}.
     * <p>
     * This method uses the specified {@code converter} to convert the value of this {@code Attribute}
     * to the desired type.
     * </p>
     *
     * @param converter the {@link AttributeValueConverter} used to convert the value of this {@code Attribute}
     * @return the converted value of this {@code Attribute}
     * @throws ClassCastException if the conversion fails due to a type mismatch
     */
    @SuppressWarnings("unchecked")
    public <T> T getConvertedValue(AttributeValueConverter converter) {
        return (T) converter.convert(this);
    }

    /**
     * Converts the attribute value to an {@code int}.
     *
     * @return the attribute value as an {@code int}
     * @throws ConverterException if the value cannot be converted to a number
     */
    public int getAsInt() {
        return NumberConverter.getInstance().convert(this).intValue();
    }

    /**
     * Converts the attribute value to a {@code long}.
     *
     * @return the attribute value as a {@code long}
     * @throws ConverterException if the value cannot be converted to a number
     */
    public long getAsLong() {
        return NumberConverter.getInstance().convert(this).longValue();
    }

    /**
     * Converts the attribute value to a {@code float}.
     *
     * @return the attribute value as a {@code float}
     * @throws ConverterException if the value cannot be converted to a number
     */
    public float getAsFloat() {
        return NumberConverter.getInstance().convert(this).floatValue();
    }

    /**
     * Converts the attribute value to a {@code double}.
     *
     * @return the attribute value as a {@code double}
     * @throws ConverterException if the value cannot be converted to a number
     */
    public double getAsDouble() {
        return NumberConverter.getInstance().convert(this).doubleValue();
    }

    /**
     * Converts the attribute value to a {@link Duration}.
     *
     * @return the attribute value as a {@link Duration}
     * @throws ConverterException if the value cannot be converted to a {@link Duration}
     */
    public Duration getAsDuration() {
        return DurationConverter.getInstance().convert(this);
    }

    /**
     * Converts the attribute value to a {@link Size}.
     *
     * @return the attribute value as a {@link Size}
     * @throws ConverterException if the value cannot be converted to a {@link Size}
     */
    public Size getAsSize() {
        return SizeConverter.getInstance().convert(this);
    }

    /**
     * Converts the attribute value to a {@link Class}.
     *
     * @param <T> the type of the class
     * @return the attribute value as a {@link Class}
     * @throws ConverterException if the value cannot be converted to a {@link Class}
     */
    @SuppressWarnings("unchecked")
    public <T> Class<T> getAsClass() {
        return (Class<T>) ClassConverter.getInstance().convert(this);
    }

    /**
     * Converts the attribute value to a {@code boolean}.
     *
     * @return the attribute value as a {@code boolean}
     * @throws ConverterException if the value cannot be converted to a {@code boolean}
     */
    public boolean getAsBoolean() {
        return BooleanConverter.getInstance().convert(this);
    }

    /**
     * Determines the type of the attribute value based on its format.
     *
     * @return the type of the attribute value
     */
    public int getType() {
        int type;
        if (ClassConverter.getInstance().check(value)) {
            type = TYPE_CLASS;
        }
        else if (DurationConverter.getInstance().check(value)) {
            type = TYPE_DURATION;
        }
        else if (NumberConverter.getInstance().check(value)) {
            type = TYPE_NUMBER;
        }
        else if (SizeConverter.getInstance().check(value)) {
            type = TYPE_SIZE;
        }
        else if (BooleanConverter.getInstance().check(value)) {
            type = TYPE_BOOLEAN;
        }
        else {
            type = TYPE_STRING;
        }
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attribute)) return false;
        Attribute attribute = (Attribute) o;
        return Objects.equals(namespace, attribute.namespace) && Objects.equals(name, attribute.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, name, value);
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "namespace='" + namespace + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
