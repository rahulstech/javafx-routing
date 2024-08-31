package rahulstech.jfx.routing.element.animation;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;
import rahulstech.jfx.routing.parser.Attribute;
import rahulstech.jfx.routing.parser.AttributeSet;

/**
 * The {@code FadeAnimation} class is an implementation of {@link rahulstech.jfx.routing.element.RouterAnimation RouterAnimation}
 * that performs fade-in and fade-out animations using JavaFX's {@link FadeTransition}.
 *
 * <p>This animation class allows for creating fade-in and fade-out effects on a {@link Node}.</p>
 * <p>
 * Predefined animations:
 * <ul>
 *     <li>{@link #getFadeOut()} ()}</li>
 *     <li>{@link #getFadeIn()}</li>
 * </ul>
 * <p>XML attributes from {@code FadeAnimation} are
 * <ul>
 *     <li>{@code fromAlpha}: starting opacity. valid values are any floating point number between 0 and 1</li>
 *     <li>{@code toAlpha}: final opacity. valid values are any floating point number between 0 and 1</li>
 * </ul>
 *
 * @author Rahul Bagchi
 * @since 1.0
 * @see rahulstech.jfx.routing.element.RouterAnimation RouterAnimation
 */
public class FadeAnimation extends BaseJavaFxAnimationRouterAnimation {

    /**
     * The constant representing a fade-in animation.
     */
    public static final String FADE_IN = "fade_in";

    /**
     * The constant representing a fade-out animation.
     */
    public static final String FADE_OUT = "fade_out";

    /**
     * Creates a {@code FadeAnimation} instance configured to perform a fade-in effect.
     *
     * @return a {@code FadeAnimation} instance for fading in
     */
    public static FadeAnimation getFadeIn() {
        return new FadeAnimation(FADE_IN,
                0.0, 1.0,
                DEFAULT_DURATION_SHORT);
    }

    /**
     * Creates a {@code FadeAnimation} instance configured to perform a fade-out effect.
     *
     * @return a {@code FadeAnimation} instance for fading out
     */
    public static FadeAnimation getFadeOut() {
        return new FadeAnimation(FADE_OUT,
                1.0, 0.0,
                DEFAULT_DURATION_SHORT);
    }

    private double fromAlpha;
    private double toAlpha;

    /**
     * Constructs a new {@code FadeAnimation} with the specified name.
     *
     * @param name the name of this animation
     */
    public FadeAnimation(String name) {
        super(name);
    }

    /**
     * Constructs a new {@code FadeAnimation} with the specified parameters.
     *
     * @param name      the name of this animation
     * @param fromAlpha the starting alpha value (opacity)
     * @param toAlpha   the ending alpha value (opacity)
     * @param duration  the duration of the animation
     */
    public FadeAnimation(String name, double fromAlpha, double toAlpha, Duration duration) {
        super(name);
        this.fromAlpha = fromAlpha;
        this.toAlpha = toAlpha;
        setDuration(duration);
    }

    /** {@inheritDoc} */
    @Override
    public void initialize(AttributeSet attrs) {
        super.initialize(attrs);
        if (attrs.beginIteration()) {
            while (attrs.iterateNext()) {
                Attribute attr = attrs.getNext();
                String name = attr.getName();
                if (Attribute.FROM_ALPHA.equals(name)) {
                    fromAlpha = attr.getAsDouble();
                }
                else if (Attribute.TO_ALPHA.equals(name)) {
                    toAlpha = attr.getAsDouble();
                }
            }
        }
    }

    /**
     * Gets the starting alpha value of the animation.
     *
     * @return the starting alpha value
     */
    public double getFromAlpha() {
        return fromAlpha;
    }

    /**
     * Sets the starting alpha value of the animation.
     *
     * @param fromAlpha the starting alpha value
     */
    public void setFromAlpha(double fromAlpha) {
        this.fromAlpha = fromAlpha;
    }

    /**
     * Gets the ending alpha value of the animation.
     *
     * @return the ending alpha value
     */
    public double getToAlpha() {
        return toAlpha;
    }

    /**
     * Sets the ending alpha value of the animation.
     *
     * @param toAlpha the ending alpha value
     */
    public void setToAlpha(double toAlpha) {
        this.toAlpha = toAlpha;
    }

    /** {@inheritDoc} */
    @Override
    protected Animation createAnimation(Node node) {
        FadeTransition animation = new FadeTransition();
        animation.setNode(node);
        animation.setFromValue(fromAlpha);
        animation.setToValue(toAlpha);
        animation.setDuration(getDuration());
        return animation;
    }

    /** {@inheritDoc} */
    @Override
    public void doReset() {
        Node node = getTarget();
        node.setOpacity(fromAlpha);
    }
}
