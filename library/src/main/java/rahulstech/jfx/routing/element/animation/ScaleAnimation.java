package rahulstech.jfx.routing.element.animation;

import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import rahulstech.jfx.routing.parser.Attribute;
import rahulstech.jfx.routing.parser.AttributeSet;

@SuppressWarnings("unused")
public class ScaleAnimation extends BaseJavaFxAnimationRouterAnimation {

    public static final String SCALE_UP_X = "scale_up_x";

    public static final String SCALE_UP_Y = "scale_up_y";

    public static final String SCALE_UP_XY = "scale_up_xy";

    public static final String SCALE_DOWN_X = "scale_down_x";

    public static final String SCALE_DOWN_Y = "scale_down_y";

    public static final String SCALE_DOWN_XY = "scale_down_xy";

    public static ScaleAnimation getScaleUpX() {
        ScaleAnimation animation = new ScaleAnimation(SCALE_UP_X)
                .setFromX(1).setToX(0);
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    public static ScaleAnimation getScaleUpY() {
        ScaleAnimation animation = new ScaleAnimation(SCALE_UP_Y)
                .setFromY(1).setToY(0);
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    public static ScaleAnimation getScaleDownX() {
        ScaleAnimation animation = new ScaleAnimation(SCALE_DOWN_X)
                .setFromX(0).setToX(1);
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    public static ScaleAnimation getScaleDownY() {
        ScaleAnimation animation = new ScaleAnimation(SCALE_DOWN_Y)
                .setFromY(1).setToY(0);
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    public static ScaleAnimation getScaleUpXY() {
        ScaleAnimation animation = new ScaleAnimation(SCALE_UP_XY)
                .setFromX(0).setFromY(0)
                .setToX(1).setToY(1);
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    public static ScaleAnimation getScaleDownXY() {
        ScaleAnimation animation = new ScaleAnimation(SCALE_UP_XY)
                .setFromX(1).setFromY(1)
                .setToX(0).setToY(0);
        animation.setDuration(DEFAULT_DURATION_SHORT);
        return animation;
    }

    private double fromX;
    private double fromY;
    private double toX;
    private double toY;

    public ScaleAnimation(String name) {
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

    public double getFromX() {
        return fromX;
    }

    public ScaleAnimation setFromX(double fromX) {
        this.fromX = fromX;
        return this;
    }

    public double getFromY() {
        return fromY;
    }

    public ScaleAnimation setFromY(double fromY) {
        this.fromY = fromY;
        return this;
    }

    public double getToX() {
        return toX;
    }

    public ScaleAnimation setToX(double toX) {
        this.toX = toX;
        return this;
    }

    public double getToY() {
        return toY;
    }

    public ScaleAnimation setToY(double toY) {
        this.toY = toY;
        return this;
    }

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

    @Override
    public void reset() {
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
