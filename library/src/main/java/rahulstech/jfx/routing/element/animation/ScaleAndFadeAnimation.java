package rahulstech.jfx.routing.element.animation;

import rahulstech.jfx.routing.element.RouterAnimation;
import rahulstech.jfx.routing.element.RouterCompoundAnimation;

@SuppressWarnings("unused")
public class ScaleAndFadeAnimation extends RouterCompoundAnimation {

    public static final String SCALE_UP_XY_FADE_IN = "scale_up_xy_fade_in";

    public static final String SCALE_DOWN_XY_FADE_OUT = "scale_down_xy_fade_out";

    public static RouterAnimation getScaleUpXYFadeIn() {
        return new ScaleAndFadeAnimation(SCALE_UP_XY_FADE_IN,
                ScaleAnimation.getScaleUpXY(),FadeAnimation.getFadeIn());
    }

    public static RouterAnimation getScaleDownXYFadeOut() {
        return new ScaleAndFadeAnimation(SCALE_DOWN_XY_FADE_OUT,
                ScaleAnimation.getScaleDownXY(),FadeAnimation.getFadeOut());
    }

    public ScaleAndFadeAnimation(String name) {
        super(name);
    }

    public ScaleAndFadeAnimation(String name, ScaleAnimation scale, FadeAnimation fade) {
        super(name,scale,fade);
    }

    @Override
    public void addChild(RouterAnimation animation) {
        if (!(animation instanceof ScaleAnimation) && !(animation instanceof FadeAnimation)) {
            throw new IllegalArgumentException("only ScaleAnimation and FadeAnimation are allowed for ScaleAndFadeAnimation");
        }
        super.addChild(animation);
    }
}
