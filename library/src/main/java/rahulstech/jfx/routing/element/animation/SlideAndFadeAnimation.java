package rahulstech.jfx.routing.element.animation;

import rahulstech.jfx.routing.element.RouterCompoundAnimation;

@SuppressWarnings("unused")
public class SlideAndFadeAnimation extends RouterCompoundAnimation {

    public static final String SLIDE_LEFT_FADE_IN = "slide_left_fade_in";
    public static final String SLIDE_RIGHT_FADE_IN = "slide_right_fade_in";
    public static final String SLIDE_TOP_FADE_IN = "slide_top_fade_in";
    public static final String SLIDE_BOTTOM_FADE_IN = "slide_bottom_fade_in";
    public static final String SLIDE_LEFT_FADE_OUT = "slide_left_fade_out";
    public static final String SLIDE_RIGHT_FADE_OUT = "slide_right_fade_out";
    public static final String SLIDE_TOP_FADE_OUT = "slide_top_fade_out";
    public static final String SLIDE_BOTTOM_FADE_OUT = "slide_bottom_fade_out";

    public static SlideAndFadeAnimation getSlideLeftFadeIn() {
        return new SlideAndFadeAnimation(SLIDE_LEFT_FADE_IN,
                SlideAnimation.getSlideInLeft(),FadeAnimation.getFadeIn());
    }

    public static SlideAndFadeAnimation getSlideRightFadeIn() {
        return new SlideAndFadeAnimation(SLIDE_RIGHT_FADE_IN,
                SlideAnimation.getSlideInRight(),FadeAnimation.getFadeIn());
    }

    public static SlideAndFadeAnimation getSlideTopFadeIn() {
        return new SlideAndFadeAnimation(SLIDE_TOP_FADE_IN,
                SlideAnimation.getSlideInTop(),FadeAnimation.getFadeIn());
    }

    public static SlideAndFadeAnimation getSlideBottomFadeIn() {
        return new SlideAndFadeAnimation(SLIDE_BOTTOM_FADE_IN,
                SlideAnimation.getSlideInBottom(),FadeAnimation.getFadeIn());
    }

    public static SlideAndFadeAnimation getSlideLeftFadeOut() {
        return new SlideAndFadeAnimation(SLIDE_LEFT_FADE_OUT,
                SlideAnimation.getSlideOutLeft(),FadeAnimation.getFadeOut());
    }

    public static SlideAndFadeAnimation getSlideRightFadeOut() {
        return new SlideAndFadeAnimation(SLIDE_RIGHT_FADE_OUT,
                SlideAnimation.getSlideOutRight(),FadeAnimation.getFadeOut());
    }

    public static SlideAndFadeAnimation getSlideTopFadeOut() {
        return new SlideAndFadeAnimation(SLIDE_TOP_FADE_OUT,
                SlideAnimation.getSlideOutTop(),FadeAnimation.getFadeOut());
    }

    public static SlideAndFadeAnimation getSlideBottomFadeOut() {
        return new SlideAndFadeAnimation(SLIDE_BOTTOM_FADE_OUT,
                SlideAnimation.getSlideOutBottom(),FadeAnimation.getFadeOut());
    }

    public SlideAndFadeAnimation(String name) {
        super(name);
    }

    public SlideAndFadeAnimation(String name, SlideAnimation slide, FadeAnimation fade) {
        super(name,slide,fade);
    }
}
