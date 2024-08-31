package rahulstech.jfx.routing.element.animation;

import rahulstech.jfx.routing.element.RouterAnimation;
import rahulstech.jfx.routing.element.RouterCompoundAnimation;

/**
 * The {@code SlideAndFadeAnimation} class combines slide and fade animations
 * to create a compound animation effect on JavaFX Nodes. This class provides
 * predefined animations for sliding a node in or out from various directions
 * while simultaneously applying a fade effect (in or out).
 *
 * <p>
 * Predefined animations:
 * <ul>
 *     <li>{@link #getSlideLeftFadeIn()}</li>
 *     <li>{@link #getSlideRightFadeIn()}</li>
 *     <li>{@link #getSlideTopFadeIn()}</li>
 *     <li>{@link #getSlideBottomFadeIn()}</li>
 *     <li>{@link #getSlideLeftFadeOut()}</li>
 *     <li>{@link #getSlideRightFadeOut()}</li>
 *     <li>{@link #getSlideTopFadeOut()}</li>
 *     <li>{@link #getSlideBottomFadeOut()}</li>
 * </ul>
 *
 * @author Rahul Bagchi
 * @since 1.0
 * @see RouterCompoundAnimation
 */
public class SlideAndFadeAnimation extends RouterCompoundAnimation {

    /**
     * Slide the node to the left while fading in.
     */
    public static final String SLIDE_LEFT_FADE_IN = "slide_left_fade_in";

    /**
     * Slide the node to the right while fading in.
     */
    public static final String SLIDE_RIGHT_FADE_IN = "slide_right_fade_in";

    /**
     * Slide the node to the top while fading in.
     */
    public static final String SLIDE_TOP_FADE_IN = "slide_top_fade_in";

    /**
     * Slide the node to the bottom while fading in.
     */
    public static final String SLIDE_BOTTOM_FADE_IN = "slide_bottom_fade_in";

    /**
     * Slide the node to the left while fading out.
     */
    public static final String SLIDE_LEFT_FADE_OUT = "slide_left_fade_out";

    /**
     * Slide the node to the right while fading out.
     */
    public static final String SLIDE_RIGHT_FADE_OUT = "slide_right_fade_out";

    /**
     * Slide the node to the top while fading out.
     */
    public static final String SLIDE_TOP_FADE_OUT = "slide_top_fade_out";

    /**
     * Slide the node to the bottom while fading out.
     */
    public static final String SLIDE_BOTTOM_FADE_OUT = "slide_bottom_fade_out";

    /**
     * Creates a {@code SlideAndFadeAnimation} that slides the node from left in canvas
     * while fading in.
     *
     * @return a {@code SlideAndFadeAnimation} instance
     */
    public static SlideAndFadeAnimation getSlideLeftFadeIn() {
        return new SlideAndFadeAnimation(SLIDE_LEFT_FADE_IN,
                SlideAnimation.getSlideInLeft(),FadeAnimation.getFadeIn());
    }

    /**
     * Creates a {@code SlideAndFadeAnimation} that slides the node from right in canvas
     * while fading in.
     *
     * @return a {@code SlideAndFadeAnimation} instance
     */
    public static SlideAndFadeAnimation getSlideRightFadeIn() {
        return new SlideAndFadeAnimation(SLIDE_RIGHT_FADE_IN,
                SlideAnimation.getSlideInRight(),FadeAnimation.getFadeIn());
    }

    /**
     * Creates a {@code SlideAndFadeAnimation} that slides the node from top in canvas
     * while fading in.
     *
     * @return a {@code SlideAndFadeAnimation} instance
     */
    public static SlideAndFadeAnimation getSlideTopFadeIn() {
        return new SlideAndFadeAnimation(SLIDE_TOP_FADE_IN,
                SlideAnimation.getSlideInTop(),FadeAnimation.getFadeIn());
    }

    /**
     * Creates a {@code SlideAndFadeAnimation} that slides the node from bottom in canvas
     * while fading in.
     *
     * @return a {@code SlideAndFadeAnimation} instance
     */
    public static SlideAndFadeAnimation getSlideBottomFadeIn() {
        return new SlideAndFadeAnimation(SLIDE_BOTTOM_FADE_IN,
                SlideAnimation.getSlideInBottom(),FadeAnimation.getFadeIn());
    }

    /**
     * Creates a {@code SlideAndFadeAnimation} that slides the node towards left off canvas
     * while fading out.
     *
     * @return a {@code SlideAndFadeAnimation} instance
     */
    public static SlideAndFadeAnimation getSlideLeftFadeOut() {
        return new SlideAndFadeAnimation(SLIDE_LEFT_FADE_OUT,
                SlideAnimation.getSlideOutLeft(),FadeAnimation.getFadeOut());
    }

    /**
     * Creates a {@code SlideAndFadeAnimation} that slides the node towards right off canvas
     * while fading out.
     *
     * @return a {@code SlideAndFadeAnimation} instance
     */
    public static SlideAndFadeAnimation getSlideRightFadeOut() {
        return new SlideAndFadeAnimation(SLIDE_RIGHT_FADE_OUT,
                SlideAnimation.getSlideOutRight(),FadeAnimation.getFadeOut());
    }

    /**
     * Creates a {@code SlideAndFadeAnimation} that slides the node towards top off canvas
     * while fading out.
     *
     * @return a {@code SlideAndFadeAnimation} instance
     */
    public static SlideAndFadeAnimation getSlideTopFadeOut() {
        return new SlideAndFadeAnimation(SLIDE_TOP_FADE_OUT,
                SlideAnimation.getSlideOutTop(),FadeAnimation.getFadeOut());
    }

    /**
     * Creates a {@code SlideAndFadeAnimation} that slides the node towards bottom off canvas
     * while fading out.
     *
     * @return a {@code SlideAndFadeAnimation} instance
     */
    public static SlideAndFadeAnimation getSlideBottomFadeOut() {
        return new SlideAndFadeAnimation(SLIDE_BOTTOM_FADE_OUT,
                SlideAnimation.getSlideOutBottom(),FadeAnimation.getFadeOut());
    }

    /**
     * Constructs a {@code SlideAndFadeAnimation} with the specified name
     *
     * @param name  the name of the compound animation
     */
    public SlideAndFadeAnimation(String name) {
        super(name);
    }

    /**
     * Constructs a {@code SlideAndFadeAnimation} with the specified name,
     * combining a slide animation and a fade animation.
     *
     * @param name  the name of the compound animation
     * @param slide the slide animation to be combined
     * @param fade  the fade animation to be combined
     */
    public SlideAndFadeAnimation(String name, SlideAnimation slide, FadeAnimation fade) {
        super(name,slide,fade);
    }

    /**
     * Adds a child animation to this {@code SlideAndFadeAnimation}.
     * <p>Only {@link SlideAnimation} and {@link FadeAnimation} instances are allowed as child animations.</p>
     *
     * @param animation the {@link RouterAnimation} to be added
     * @throws IllegalArgumentException if the provided animation is neither a {@link SlideAnimation} nor a {@link FadeAnimation}
     */
    @Override
    public void addChild(RouterAnimation animation) {
        if (!(animation instanceof SlideAnimation) && !(animation instanceof FadeAnimation)) {
            throw new IllegalArgumentException("only SlideAnimation and FadeAnimation are allowed for SlideAndFadeAnimation");
        }
        super.addChild(animation);
    }
}
