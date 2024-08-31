package rahulstech.jfx.routing.parser;

import javafx.util.Duration;
import rahulstech.jfx.routing.RouterOptions;
import rahulstech.jfx.routing.element.RouterArgument;
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

    // common attributes

    /**
     * Set id for {@link rahulstech.jfx.routing.element.Destination Destination}, {@link rahulstech.jfx.routing.element.RouterAnimation RouterAnimation}
     * {@link rahulstech.jfx.routing.element.RouterCompoundAnimation RouterCompoundAnimation} etc. {@code id} for a perticular type must be unique in
     * {@link rahulstech.jfx.routing.Router Router}. For example: there must be only one {@code Destination} with id "screen1". Though you can use same id
     * for different type; but it's not recommended. For example: you can use id "screen1" for {@code Destination} as well as {@code RouterAnimation}
     * or anything else other than {@code Destination} is valid
     */
    public static final String ID = "id";

    /**
     * Set the name for {@link rahulstech.jfx.routing.element.RouterAnimation RouterAnimation}, {@link rahulstech.jfx.routing.element.RouterArgument RouterArgument}
     * etc.
     */
    public static final  String NAME = "name";

    /**
     * Set the type for {@link rahulstech.jfx.routing.element.RouterArgument RouterArgument}
     */
    public static final String TYPE = "type";

    // attributes for Router

    /**
     * Set the destion id to use as home destination
     */
    public static final  String HOME = "home";

    /**
     * Set the id or name of {@link rahulstech.jfx.routing.element.RouterAnimation RouterAnimation}
     * for home screen enter animation.
     */
    public static final  String HOME_ENTER_ANIMATION = "homeEnterAnimation";

    /**
     * Set the id or name of {@link rahulstech.jfx.routing.element.RouterAnimation RouterAnimation}
     * for screen default enter animation.Override this value by providing {@link rahulstech.jfx.routing.RouterOptions RouterOption}
     * with movement methods of {@link rahulstech.jfx.routing.Router Router}
     *
     *  @see rahulstech.jfx.routing.Router#moveto(String, RouterArgument, RouterOptions)
     */
    public static final  String ENTER_ANIMATION = "enterAnimation";

    /**
     * Set the id or name of {@link rahulstech.jfx.routing.element.RouterAnimation RouterAnimation}
     * for screen default exit animation.Override this value by providing {@link rahulstech.jfx.routing.RouterOptions RouterOption}
     * with movement methods of {@link rahulstech.jfx.routing.Router Router}
     *
     * @see rahulstech.jfx.routing.Router#moveto(String, RouterArgument, RouterOptions)
     */
    public static final  String EXIT_ANIMATION = "exitAnimation";

    /**
     * Set the id or name of {@link rahulstech.jfx.routing.element.RouterAnimation RouterAnimation}
     * for screen default pop enter animation. Override this value by providing {@link rahulstech.jfx.routing.RouterOptions RouterOption}
     * with movement methods of {@link rahulstech.jfx.routing.Router Router}
     *
     * @see rahulstech.jfx.routing.Router#moveto(String, RouterArgument, RouterOptions)
     */
    public static final  String POP_ENTER_ANIMATION = "popEnterAnimation";

    /**
     * Set the id or name of {@link rahulstech.jfx.routing.element.RouterAnimation RouterAnimation}
     * for screen default pop exit animation. Override this value by providing {@link rahulstech.jfx.routing.RouterOptions RouterOption}
     * with movement methods of {@link rahulstech.jfx.routing.Router Router}
     *
     * @see rahulstech.jfx.routing.Router#moveto(String, RouterArgument, RouterOptions)
     */
    public static final  String POP_EXIT_ANIMATION = "popExitAnimation";

    // attributes RouterAnimation

    /**
     * Set the duration for {@link rahulstech.jfx.routing.element.RouterAnimation RouterAnimation}
     */
    public static final  String DURATION = "duration";

    /**
     * Set auto-reset for {@link rahulstech.jfx.routing.element.RouterAnimation RouterAnimation}
     */
    public static final String AUTO_RESET = "autoReset";

    /**
     * Set starting opacity for {@link rahulstech.jfx.routing.element.animation.FadeAnimation FadeAnimation}
     */
    public static final String FROM_ALPHA = "fromAlpha";

    /**
     * Set final opacity for {@link rahulstech.jfx.routing.element.animation.FadeAnimation FadeAnimation}
     */
    public static final String TO_ALPHA = "toAlpha";

    /**
     * Set starting scale along x-axis for {@link rahulstech.jfx.routing.element.animation.ScaleAnimation ScaleAnimation}
     */
    public static final  String FROM_X_SCALE = "fromXScale";

    /**
     * Set final scale along x-axis for {@link rahulstech.jfx.routing.element.animation.ScaleAnimation ScaleAnimation}
     */
    public static final  String TO_X_SCALE = "toXScale";

    /**
     * Set starting scale along y-axis for {@link rahulstech.jfx.routing.element.animation.ScaleAnimation ScaleAnimation}
     */
    public static final  String FROM_Y_SCALE = "fromYScale";

    /**
     * Set final scale along y-axis for {@link rahulstech.jfx.routing.element.animation.ScaleAnimation ScaleAnimation}
     */
    public static final  String TO_Y_SCALE = "toYScale";

    /**
     * Set starting translation along x-axis for {@link rahulstech.jfx.routing.element.animation.SlideAnimation SlideAnimation}
     */
    public static final  String FROM_X_TRANSLATE = "fromXTranslate";

    /**
     * Set final translation along x-axis for {@link rahulstech.jfx.routing.element.animation.SlideAnimation SlideAnimation}
     */
    public static final  String TO_X_TRANSLATE = "toXTranslate";

    /**
     * Set starting translation along y-axis for {@link rahulstech.jfx.routing.element.animation.SlideAnimation SlideAnimation}
     */
    public static final  String FROM_Y_TRANSLATE = "fromYTranslate";
    /**
     * Set final translation along y-axis for {@link rahulstech.jfx.routing.element.animation.SlideAnimation SlideAnimation}
     */
    public static final  String TO_Y_TRANSLATE = "toYTranslate";

    // attribute RouterCompoundAnimation

    /**
     * Set {@link rahulstech.jfx.routing.element.RouterCompoundAnimation.PlayMode PlayMode} for
     * {@link rahulstech.jfx.routing.element.RouterCompoundAnimation RouterCompoundAnimation}
     */
    public static final String PLAY_MODE = "playMode";

    // attributes for Destination

    /**
     * Set controller full class name for {@link  rahulstech.jfx.routing.element.Destination Destination}
     */
    public static final  String CONTROLLER_CLASS = "controllerClass";

    /**
     * Set full class name of the {@link rahulstech.jfx.routing.RouterExecutor RouterExecutor} for
     * {@link  rahulstech.jfx.routing.element.Destination Destination}.
     */
    public static final  String EXECUTOR = "executor";

    /**
     * Set the fxml resource path for {@link rahulstech.jfx.routing.element.Destination Destination}
     */
    public static final  String FXML = "fxml";

    /**
     * Set the title for {@link rahulstech.jfx.routing.element.Destination Destination}.
     * This value can be used as stage title.
     */
    public static final  String TITLE = "title";

    /**
     * Set the global argument id for {@link rahulstech.jfx.routing.element.Destination Destination}.
     */
    public static final String ARGUMENTS = "arguments";

    // attribute for argument

    /**
     * Set the {@link rahulstech.jfx.routing.element.RouterArgument RouterArgument} required or optional.
     * If {@code true} then required if {@code false} or not set then optional.
     */
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
     * @param <T> desired type of the converted value
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
