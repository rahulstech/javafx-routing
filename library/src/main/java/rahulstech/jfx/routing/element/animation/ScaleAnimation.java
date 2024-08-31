package rahulstech.jfx.routing.element.animation;

import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import rahulstech.jfx.routing.parser.Attribute;
import rahulstech.jfx.routing.parser.AttributeSet;

/**
 * {@code ScaleAnimation} is a {@link rahulstech.jfx.routing.element.RouterAnimation RouterAnimation} that provides scaling animations
 * for JavaFX nodes.
 *
 * <p>This class supports various scaling effects, such as scaling up or down in the X or Y direction, or both.</p>
 *
 * <p>Predefined animations:</p>
 * <ul>
 *     <li>{@link #getScaleUpX()}</li>
 *     <li>{@link #getScaleUpY()}</li>
 *     <li>{@link #getScaleDownX()}</li>
 *     <li>{@link #getScaleDownY()}</li>
 *     <li>{@link #getScaleUpXY()}</li>
 *     <li>{@link #getScaleDownXY()}</li>
 * </ul>
 *
 * <p>XML attributes for {@code ScaleAnimations} are
 * <ul>
 *     <li>{@code fromXScale}: starting scale value along x axis. valid values 0 (zero) or any positive floating point number</li>
 *     <li>{@code toXScale}: final scale value along x axis. valid values 0 (zero) or any positive floating point number</li>
 *     <li>{@code fromYScale}: starting scale value along y axis. valid values 0 (zero) or any positive floating point number</li>
 *    <li>{@code toYScale}: final scale value along y axis. valid values 0 (zero) or any positive floating point number</li>
 * </ul>
 *
 * @author Rahul Bagchi
 * @since 1.0
 * @see rahulstech.jfx.routing.element.RouterAnimation RouterAnimation
 */
public class ScaleAnimation extends BaseJavaFxAnimationRouterAnimation {

    /**
     * Animation name for scaling up along the X axis.
     */
    public static final String SCALE_UP_X = "scale_up_x";

    /**
     * Animation name for scaling up along the Y axis.
     */
    public static final String SCALE_UP_Y = "scale_up_y";

    /**
     * Animation name for scaling up along both X and Y axes.
     */
    public static final String SCALE_UP_XY = "scale_up_xy";

    /**
     * Animation name for scaling down along the X axis.
     */
    public static final String SCALE_DOWN_X = "scale_down_x";

    /**
     * Animation name for scaling down along the Y axis.
     */
    public static final String SCALE_DOWN_Y = "scale_down_y";

    /**
     * Animation name for scaling down along both X and Y axes.
     */
    public static final String SCALE_DOWN_XY = "scale_down_xy";

    /**
     * Returns a predefined {@code ScaleAnimation} that scales up along the X axis.
     *
     * @return a predefined {@code ScaleAnimation} that scales up along the X axis.
     */
    public static ScaleAnimation getScaleUpX() {
        ScaleAnimation animation = new ScaleAnimation(SCALE_UP_X)
                .setFromX(1).setToX(0);
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    /**
     * Returns a predefined {@code ScaleAnimation} that scales up along the Y axis.
     *
     * @return a predefined {@code ScaleAnimation} that scales up along the Y axis.
     */
    public static ScaleAnimation getScaleUpY() {
        ScaleAnimation animation = new ScaleAnimation(SCALE_UP_Y)
                .setFromY(1).setToY(0);
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    /**
     * Returns a predefined {@code ScaleAnimation} that scales down along the X axis.
     *
     * @return a predefined {@code ScaleAnimation} that scales down along the X axis.
     */
    public static ScaleAnimation getScaleDownX() {
        ScaleAnimation animation = new ScaleAnimation(SCALE_DOWN_X)
                .setFromX(0).setToX(1);
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    /**
     * Returns a predefined {@code ScaleAnimation} that scales down along the Y axis.
     *
     * @return a predefined {@code ScaleAnimation} that scales down along the Y axis.
     */
    public static ScaleAnimation getScaleDownY() {
        ScaleAnimation animation = new ScaleAnimation(SCALE_DOWN_Y)
                .setFromY(1).setToY(0);
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    /**
     * Returns a predefined {@code ScaleAnimation} that scales up along both X and Y axes.
     *
     * @return a predefined {@code ScaleAnimation} that scales up along both X and Y axes.
     */
    public static ScaleAnimation getScaleUpXY() {
        ScaleAnimation animation = new ScaleAnimation(SCALE_UP_XY)
                .setFromX(0).setFromY(0)
                .setToX(1).setToY(1);
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    /**
     * Returns a predefined {@code ScaleAnimation} that scales down along both X and Y axes.
     *
     * @return a predefined {@code ScaleAnimation} that scales down along both X and Y axes.
     */
    public static ScaleAnimation getScaleDownXY() {
        ScaleAnimation animation = new ScaleAnimation(SCALE_DOWN_XY)
                .setFromX(1).setFromY(1)
                .setToX(0).setToY(0);
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    private double fromX;
    private double fromY;
    private double toX;
    private double toY;

    /**
     * Creates new {@code ScaleAnimation} instance with name
     *
     * @param name animation name
     */
    public ScaleAnimation(String name) {
        super(name);
    }

    /** {@inheritDoc} */
    @Override
    public void initialize(AttributeSet attrs) {
        super.initialize(attrs);
        if (attrs.beginIteration()) {
            while (attrs.iterateNext()) {
                Attribute attr = attrs.getNext();
                if (!attr.isDefaultNamespace()) {
                    continue;
                }
                switch (attr.getName()) {
                    case Attribute.FROM_X_SCALE: {
                        setFromX(attr.getAsDouble());
                    }
                    break;
                    case Attribute.TO_X_SCALE: {
                        setToX(attr.getAsDouble());
                    }
                    break;
                    case Attribute.FROM_Y_SCALE: {
                        setFromY(attr.getAsDouble());
                    }
                    break;
                    case Attribute.TO_Y_SCALE: {
                        setToY(attr.getAsDouble());
                    }
                    break;
                }
            }
        }
    }

    /**
     * Returns starting scale along x-axis value
     *
     * @return non negative {@code double} value as scale factor
     */
    public double getFromX() {
        return fromX;
    }

    /**
     * Sets stating scale along x-axis value
     *
     * @param fromX non negative {@code double} value
     * @return this {@code ScaleAnimation} instance
     */
    public ScaleAnimation setFromX(double fromX) {
        this.fromX = fromX;
        return this;
    }

    /**
     * Returns starting scale along y-axis value
     *
     * @return non negative {@code double} value as scale factor
     */
    public double getFromY() {
        return fromY;
    }

    /**
     * Sets stating scale along y-axis value
     *
     * @param fromY non negative {@code double} value
     * @return this {@code ScaleAnimation} instance
     */
    public ScaleAnimation setFromY(double fromY) {
        this.fromY = fromY;
        return this;
    }

    /**
     * Returns final scale along x-axis value
     *
     * @return non negative {@code double} value as scale factor
     */
    public double getToX() {
        return toX;
    }

    /**
     * Sets final scale along x-axis value
     *
     * @param toX non negative {@code double} value
     * @return this {@code ScaleAnimation} instance
     */
    public ScaleAnimation setToX(double toX) {
        this.toX = toX;
        return this;
    }

    /**
     * Returns final scale along y-axis value
     *
     * @return non negative {@code double} value as scale factor
     */
    public double getToY() {
        return toY;
    }

    /**
     * Sets final scale along y-axis value
     *
     * @param toY non negative {@code double} value
     * @return this {@code ScaleAnimation} instance
     */
    public ScaleAnimation setToY(double toY) {
        this.toY = toY;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    protected Animation createAnimation(Node node) {
        ScaleTransition animation = new ScaleTransition();
        animation.setNode(node);
        animation.setDuration(getDuration());
        switch (getName()) {
            case SCALE_UP_X:
            case SCALE_DOWN_X: {
                animation.setFromX(fromX);
                animation.setToX(toX);
            }
            break;
            case SCALE_UP_Y:
            case SCALE_DOWN_Y:{
                animation.setFromY(fromY);
                animation.setToY(toY);
            }
            break;
            case SCALE_UP_XY:
            case SCALE_DOWN_XY: {
                animation.setFromX(fromX);
                animation.setFromY(fromY);
                animation.setToX(toX);
                animation.setToY(toY);
            }
        }
        return animation;
    }

    /** {@inheritDoc} */
    @Override
    public void doReset() {
        Node node = getTarget();
        switch (getName()) {
            case SCALE_UP_X:
            case SCALE_DOWN_X:{
                node.setScaleX(fromX);
            }
            break;
            case SCALE_UP_Y:
            case SCALE_DOWN_Y:{
                node.setScaleY(fromY);
            }
            break;
            case SCALE_UP_XY:
            case SCALE_DOWN_XY: {
                node.setScaleX(fromX);
                node.setScaleY(fromY);
            }
        }
    }
}
