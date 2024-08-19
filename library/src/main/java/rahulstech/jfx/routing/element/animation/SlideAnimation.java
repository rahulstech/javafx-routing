package rahulstech.jfx.routing.element.animation;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import rahulstech.jfx.routing.parser.Attribute;
import rahulstech.jfx.routing.parser.AttributeSet;
import rahulstech.jfx.routing.util.Size;

@SuppressWarnings("unused")
public class SlideAnimation extends BaseJavaFxAnimationRouterAnimation {


    /**
     *  SLIDE_<operation>_<from direction>
     *  for example:
     *  SLIDE_IN_TOP => translateY  from -ve value to 0
     *  SLIDE_OUT_RIGHT => translateX from 0 to +ve value
     */

    public static final String SLIDE_IN_LEFT = "slide_in_left";

    public static final String SLIDE_IN_RIGHT = "slide_in_right";

    public static final String SLIDE_IN_TOP = "slide_in_top";

    public static final String SLIDE_IN_BOTTOM = "slide_in_bottom";

    public static final String SLIDE_OUT_LEFT = "slide_out_left";

    public static final String SLIDE_OUT_RIGHT = "slide_out_right";

    public static final String SLIDE_OUT_TOP = "slide_out_top";

    public static final String SLIDE_OUT_BOTTOM = "slide_out_bottom";

    public static SlideAnimation getSlideInLeft() {
        SlideAnimation animation = new SlideAnimation(SLIDE_IN_LEFT)
                .setFromX(new Size(-100,Size.PERCENT))
                .setToX(new Size(0,Size.PERCENT));
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    public static SlideAnimation getSlideOutLeft() {
        SlideAnimation animation = new SlideAnimation(SLIDE_OUT_RIGHT)
                .setFromX(new Size(0,Size.PERCENT))
                .setToX(new Size(-100,Size.PERCENT));
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    public static SlideAnimation getSlideInRight() {
        SlideAnimation animation = new SlideAnimation(SLIDE_IN_RIGHT)
                .setFromX(new Size(100,Size.PERCENT))
                .setToX(new Size(0,Size.PERCENT));
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    public static SlideAnimation getSlideOutRight() {
        SlideAnimation animation = new SlideAnimation(SLIDE_OUT_RIGHT)
                .setFromX(new Size(0,Size.PERCENT))
                .setToX(new Size(100,Size.PERCENT));
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    public static SlideAnimation getSlideInTop() {
        SlideAnimation animation = new SlideAnimation(SLIDE_IN_TOP)
                .setFromY(new Size(-100,Size.PERCENT))
                .setToY(new Size(0,Size.PERCENT));
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    public static SlideAnimation getSlideOutTop() {
        SlideAnimation animation = new SlideAnimation(SLIDE_OUT_TOP)
                .setFromY(new Size(0,Size.PERCENT))
                .setToY(new Size(-100,Size.PERCENT));
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    public static SlideAnimation getSlideInBottom() {
        SlideAnimation animation = new SlideAnimation(SLIDE_IN_BOTTOM)
                .setFromY(new Size(100,Size.PERCENT))
                .setToY(new Size(0,Size.PERCENT));
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

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

    public SlideAnimation(String name) {
        super(name);
    }

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

    public Size getFromX() {
        return fromX;
    }

    public SlideAnimation setFromX(Size fromX) {
        this.fromX = fromX;
        return this;
    }

    public Size getFromY() {
        return fromY;
    }

    public SlideAnimation setFromY(Size fromY) {
        this.fromY = fromY;
        return this;
    }

    public Size getToX() {
        return toX;
    }

    public SlideAnimation setToX(Size toX) {
        this.toX = toX;
        return this;
    }

    public Size getToY() {
        return toY;
    }

    public SlideAnimation setToY(Size toY) {
        this.toY = toY;
        return this;
    }

    @Override
    public void reset() {
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
