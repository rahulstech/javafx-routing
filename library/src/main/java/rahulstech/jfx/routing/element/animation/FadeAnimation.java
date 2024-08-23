package rahulstech.jfx.routing.element.animation;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;
import rahulstech.jfx.routing.parser.Attribute;
import rahulstech.jfx.routing.parser.AttributeSet;

@SuppressWarnings("unused")
public class FadeAnimation extends BaseJavaFxAnimationRouterAnimation {

    public static final String FADE_IN = "fade_in";

    public static final String FADE_OUT = "fade_out";

    public static FadeAnimation getFadeIn() {
        return new FadeAnimation(FADE_IN,
                0.0,1.0,
                DEFAULT_DURATION_SHORT);
    }

    public static FadeAnimation getFadeOut() {
        return new FadeAnimation(FADE_OUT,
                1.0,0.0,
                DEFAULT_DURATION_SHORT);
    }

    private double fromAlpha;
    private double toAlpha;

    public FadeAnimation(String name) {
        super(name);
    }

    public FadeAnimation(String name, double fromAlpha, double toAlpha, Duration duration) {
        super(name);
        this.fromAlpha = fromAlpha;
        this.toAlpha = toAlpha;
        setDuration(duration);
    }

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

    public double getFromAlpha() {
        return fromAlpha;
    }

    public void setFromAlpha(double fromAlpha) {
        this.fromAlpha = fromAlpha;
    }

    public double getToAlpha() {
        return toAlpha;
    }

    public void setToAlpha(double toAlpha) {
        this.toAlpha = toAlpha;
    }

    @Override
    protected Animation createAnimation(Node node) {
        FadeTransition animation = new FadeTransition();
        animation.setNode(node);
        animation.setFromValue(fromAlpha);
        animation.setToValue(toAlpha);
        animation.setDuration(getDuration());
        return animation;
    }

    @Override
    public void doReset() {
        Node node = getTarget();
        node.setOpacity(fromAlpha);
    }
}
