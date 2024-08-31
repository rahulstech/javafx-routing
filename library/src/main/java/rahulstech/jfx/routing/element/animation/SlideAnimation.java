package rahulstech.jfx.routing.element.animation;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import rahulstech.jfx.routing.parser.Attribute;
import rahulstech.jfx.routing.parser.AttributeSet;
import rahulstech.jfx.routing.util.Size;

/**
 * The {@code SlideAnimation} class is a {@link rahulstech.jfx.routing.element.RouterAnimation RouterAnimation}
 * provides predefined slide animations for JavaFX Nodes along x and y axises only.
 * It supports sliding the node in and out from various directions (left, right, top, bottom).
 * The sliding effect is achieved by translating the node along the X or Y axis.
 * <p>
 * Each animation is defined by a starting and an ending translate value, specified in either
 * absolute units (pixels) or relative units (percentage of the node's or node parent's dimension).
 * </p>
 *
 * <p>
 * Predefined animations:
 * <ul>
 *     <li>{@link #getSlideInLeft()}</li>
 *     <li>{@link #getSlideOutLeft()}</li>
 *     <li>{@link #getSlideInRight()}</li>
 *     <li>{@link #getSlideOutRight()}</li>
 *     <li>{@link #getSlideInTop()}</li>
 *     <li>{@link #getSlideOutTop()}</li>
 *     <li>{@link #getSlideInBottom()}</li>
 *     <li>{@link #getSlideOutBottom()}</li>
 * </ul>
 *
 * <p>XML attributes for {@code ScaleAnimations} are
 * <ul>
 *     <li>{@code fromXTranslate}: starting translate value along x axis</li>
 *     <li>{@code fromXTranslate}: final translate value along x axis</li>
 *     <li>{@code fromYTranslate}: starting translate value along y axisr</li>
 *    <li>{@code fromYTranslate}: final translate value along y axis</li>
 * </ul>
 *
 * <p>
 *  Note: units for from and to values must be same along same axis. For example:
 *  {@code fromXTranslate="2px"} and {@code toXTranslate="100%"} is invalid.
 * </p>
 * @author Rahul Bagchi
 * @since 1.0
 * @see rahulstech.jfx.routing.element.RouterAnimation RouterAnimation
 */
public class SlideAnimation extends BaseJavaFxAnimationRouterAnimation {


    /**
     * Slide the node in from the left.
     */
    public static final String SLIDE_IN_LEFT = "slide_in_left";

    /**
     * Slide the node in from the right.
     */
    public static final String SLIDE_IN_RIGHT = "slide_in_right";

    /**
     * Slide the node in from the top.
     */
    public static final String SLIDE_IN_TOP = "slide_in_top";

    /**
     * Slide the node in from the bottom.
     */
    public static final String SLIDE_IN_BOTTOM = "slide_in_bottom";

    /**
     * Slide the node out to the left.
     */
    public static final String SLIDE_OUT_LEFT = "slide_out_left";

    /**
     * Slide the node out to the right.
     */
    public static final String SLIDE_OUT_RIGHT = "slide_out_right";

    /**
     * Slide the node out to the top.
     */
    public static final String SLIDE_OUT_TOP = "slide_out_top";

    /**
     * Slide the node out to the bottom.
     */
    public static final String SLIDE_OUT_BOTTOM = "slide_out_bottom";

    /**
     * Predefined slide animation where the node slides in from the left.
     *
     * @return A {@link SlideAnimation} configured to slide in from the left.
     */
    public static SlideAnimation getSlideInLeft() {
        SlideAnimation animation = new SlideAnimation(SLIDE_IN_LEFT)
                .setFromX(new Size(-100,Size.PERCENT))
                .setToX(new Size(0,Size.PERCENT));
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    /**
     * Predefined slide animation where the node slides out to the left.
     *
     * @return A {@link SlideAnimation} configured to slide out to the left.
     */
    public static SlideAnimation getSlideOutLeft() {
        SlideAnimation animation = new SlideAnimation(SLIDE_OUT_RIGHT)
                .setFromX(new Size(0,Size.PERCENT))
                .setToX(new Size(-100,Size.PERCENT));
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    /**
     * Predefined slide animation where the node slides in from the right.
     *
     * @return A {@link SlideAnimation} configured to slide in from the right.
     */
    public static SlideAnimation getSlideInRight() {
        SlideAnimation animation = new SlideAnimation(SLIDE_IN_RIGHT)
                .setFromX(new Size(100,Size.PERCENT))
                .setToX(new Size(0,Size.PERCENT));
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    /**
     * Predefined slide animation where the node slides out to the right.
     *
     * @return A {@link SlideAnimation} configured to slide out to the right.
     */
    public static SlideAnimation getSlideOutRight() {
        SlideAnimation animation = new SlideAnimation(SLIDE_OUT_RIGHT)
                .setFromX(new Size(0,Size.PERCENT))
                .setToX(new Size(100,Size.PERCENT));
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    /**
     * Predefined slide animation where the node slides in from the top.
     *
     * @return A {@link SlideAnimation} configured to slide in from the top.
     */
    public static SlideAnimation getSlideInTop() {
        SlideAnimation animation = new SlideAnimation(SLIDE_IN_TOP)
                .setFromY(new Size(-100,Size.PERCENT))
                .setToY(new Size(0,Size.PERCENT));
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    /**
     * Predefined slide animation where the node slides out to the top.
     *
     * @return A {@link SlideAnimation} configured to slide out to the top.
     */
    public static SlideAnimation getSlideOutTop() {
        SlideAnimation animation = new SlideAnimation(SLIDE_OUT_TOP)
                .setFromY(new Size(0,Size.PERCENT))
                .setToY(new Size(-100,Size.PERCENT));
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    /**
     * Predefined slide animation where the node slides in from the bottom.
     *
     * @return A {@link SlideAnimation} configured to slide in from the bottom.
     */
    public static SlideAnimation getSlideInBottom() {
        SlideAnimation animation = new SlideAnimation(SLIDE_IN_BOTTOM)
                .setFromY(new Size(100,Size.PERCENT))
                .setToY(new Size(0,Size.PERCENT));
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    /**
     * Predefined slide animation where the node slides out to the bottom.
     *
     * @return A {@link SlideAnimation} configured to slide out to the bottom.
     */
    public static SlideAnimation getSlideOutBottom() {
        SlideAnimation animation = new SlideAnimation(SLIDE_OUT_BOTTOM)
                .setFromY(new Size(0,Size.PERCENT))
                .setToY(new Size(100,Size.PERCENT));
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    private Size fromX;
    private Size fromY;
    private Size toX;
    private Size toY;

    /**
     * Constructs a {@code SlideAnimation} with the specified name.
     *
     * @param name the name of the animation
     */
    public SlideAnimation(String name) {
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
                    case Attribute.FROM_X_TRANSLATE: {
                        setFromX(attr.getAsSize());
                    }
                    break;
                    case Attribute.TO_X_TRANSLATE: {
                        setToX(attr.getAsSize());
                    }
                    break;
                    case Attribute.FROM_Y_TRANSLATE: {
                       setFromY(attr.getAsSize());
                    }
                    break;
                    case Attribute.TO_Y_TRANSLATE: {
                        setToY(attr.getAsSize());
                    }
                    break;
                }
            }
        }
    }

    /**
     * Returns the starting X translation value.
     *
     * @return the starting X translation value
     */
    public Size getFromX() {
        return fromX;
    }

    /**
     * Sets the starting X translation value.
     *
     * @param fromX the starting X translation value
     * @return the {@code SlideAnimation} instance for method chaining
     */
    public SlideAnimation setFromX(Size fromX) {
        this.fromX = fromX;
        return this;
    }

    /**
     * Returns the starting Y translation value.
     *
     * @return the starting Y translation value
     */
    public Size getFromY() {
        return fromY;
    }

    /**
     * Sets the starting Y translation value.
     *
     * @param fromY the starting Y translation value
     * @return the {@code SlideAnimation} instance for method chaining
     */
    public SlideAnimation setFromY(Size fromY) {
        this.fromY = fromY;
        return this;
    }

    /**
     * Returns the ending X translation value.
     *
     * @return the ending X translation value
     */
    public Size getToX() {
        return toX;
    }

    /**
     * Sets the ending X translation value.
     *
     * @param toX the ending X translation value
     * @return the {@code SlideAnimation} instance for method chaining
     */
    public SlideAnimation setToX(Size toX) {
        this.toX = toX;
        return this;
    }

    /**
     * Returns the ending Y translation value.
     *
     * @return the ending Y translation value
     */
    public Size getToY() {
        return toY;
    }

    /**
     * Sets the ending Y translation value.
     *
     * @param toY the ending Y translation value
     * @return the {@code SlideAnimation} instance for method chaining
     */
    public SlideAnimation setToY(Size toY) {
        this.toY = toY;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public void doReset() {
        Node node = getTarget();
        switch (getName()) {
            case SLIDE_IN_LEFT:
            case SLIDE_IN_RIGHT:
            case SLIDE_OUT_LEFT:
            case SLIDE_OUT_RIGHT: {
                double _valueFrom = calculateValue(fromX,node,true);
                node.setTranslateX(_valueFrom);
            }
            break;
            case SLIDE_IN_TOP:
            case SLIDE_IN_BOTTOM:
            case SLIDE_OUT_TOP:
            case SLIDE_OUT_BOTTOM: {
                double _valueFrom = calculateValue(fromY,node,false);
                node.setTranslateY(_valueFrom);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    protected Animation createAnimation(Node node) {
        TranslateTransition animation = new TranslateTransition();
        animation.setNode(node);
        animation.setDuration(getDuration());
        switch (getName()) {
            case SLIDE_IN_LEFT:
            case SLIDE_IN_RIGHT:
            case SLIDE_OUT_LEFT:
            case SLIDE_OUT_RIGHT: {
                checkSameUnitsOrThrow(fromX,toX);
                double valueFrom = calculateValue(fromX,node,true);
                double valueTo = calculateValue(toX,node,true);
                animation.setFromX(valueFrom);
                animation.setToX(valueTo);
            }
            break;
            case SLIDE_IN_TOP:
            case SLIDE_IN_BOTTOM:
            case SLIDE_OUT_TOP:
            case SLIDE_OUT_BOTTOM: {
                checkSameUnitsOrThrow(fromY,toY);
                double valueFrom = calculateValue(fromY,node,true);
                double valueTo = calculateValue(toY,node,true);
                animation.setFromY(valueFrom);
                animation.setToY(valueTo);
            }
        }
        return animation;
    }

    private void checkSameUnitsOrThrow(Size size1, Size size2) {
        if (!Size.checkSameUnit(size1,size2)) {
            throw new IllegalArgumentException("size units mismatch");
        }
    }

    private double calculateValue(Size value, Node target, boolean horizontal) {
        String unit = value.getUnit();
        double _value;
        if (Size.PERCENT.equals(unit)) {
            double multiplier = value.getValue()/100;
            double dimension = getDimension(target,horizontal);
            _value = multiplier * dimension;
        }
        else {
            _value = value.getValue();
        }
        return _value;
    }

    private double getDimension(Node target, boolean horizontal) {
        Bounds boundInParent = target.getBoundsInParent();
        return horizontal ? boundInParent.getWidth() : boundInParent.getHeight();
    }
}
