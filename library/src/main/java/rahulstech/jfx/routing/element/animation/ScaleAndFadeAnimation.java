package rahulstech.jfx.routing.element.animation;

import rahulstech.jfx.routing.element.RouterAnimation;
import rahulstech.jfx.routing.element.RouterCompoundAnimation;

/**
 * {@code ScaleAndFadeAnimation} is a specialized {@link RouterCompoundAnimation} that combines
 * {@link ScaleAnimation} and {@link FadeAnimation} into a single animation.
 *
 * <p>This class provides predefined animations for scaling and fading effects, such as scaling up
 * while fading in, or scaling down while fading out. It ensures that only {@link ScaleAnimation}
 * and {@link FadeAnimation} instances can be added as child animations.</p>
 *
 * <p>
 * Predefined animations:
 * <ul>
 *     <li>{@link #getScaleUpXYFadeIn()} ()}</li>
 *     <li>{@link #getScaleDownXYFadeOut()} ()}</li>
 * </ul>
 * </p>
 *
 * @author Rahul Bagchi
 * @since 1.0
 * @see RouterCompoundAnimation
 * @see ScaleAnimation
 * @see FadeAnimation
 */
public class ScaleAndFadeAnimation extends RouterCompoundAnimation {

    public static final String SCALE_UP_XY_FADE_IN = "scale_up_xy_fade_in";

    public static final String SCALE_DOWN_XY_FADE_OUT = "scale_down_xy_fade_out";

    /**
     * Creates a predefined {@code ScaleAndFadeAnimation} for scaling up while fading in.
     *
     * @return a new {@code ScaleAndFadeAnimation} instance
     */
    public static RouterAnimation getScaleUpXYFadeIn() {
        return new ScaleAndFadeAnimation(SCALE_UP_XY_FADE_IN,
                ScaleAnimation.getScaleUpXY(), FadeAnimation.getFadeIn());
    }

    /**
     * Creates a predefined {@code ScaleAndFadeAnimation} for scaling down while fading out.
     *
     * @return a new {@code ScaleAndFadeAnimation} instance
     */
    public static RouterAnimation getScaleDownXYFadeOut() {
        return new ScaleAndFadeAnimation(SCALE_DOWN_XY_FADE_OUT,
                ScaleAnimation.getScaleDownXY(), FadeAnimation.getFadeOut());
    }

    /**
     * Creates a new {@code ScaleAndFadeAnimation} with the specified name.
     *
     * @param name the name of the animation
     */
    public ScaleAndFadeAnimation(String name) {
        super(name);
    }

    /**
     * Creates a new {@code ScaleAndFadeAnimation} with the specified name, scale animation, and fade animation.
     *
     * @param name the name of the animation
     * @param scale the {@link ScaleAnimation} to be added as a child animation
     * @param fade the {@link FadeAnimation} to be added as a child animation
     */
    public ScaleAndFadeAnimation(String name, ScaleAnimation scale, FadeAnimation fade) {
        super(name, scale, fade);
    }

    /**
     * Adds a child animation to this {@code ScaleAndFadeAnimation}.
     * <p>Only {@link ScaleAnimation} and {@link FadeAnimation} instances are allowed as child animations.</p>
     *
     * @param animation the {@link RouterAnimation} to be added
     * @throws IllegalArgumentException if the provided animation is neither a {@link ScaleAnimation} nor a {@link FadeAnimation}
     */
    @Override
    public void addChild(RouterAnimation animation) {
        if (!(animation instanceof ScaleAnimation) && !(animation instanceof FadeAnimation)) {
            throw new IllegalArgumentException("only ScaleAnimation and FadeAnimation are allowed for ScaleAndFadeAnimation");
        }
        super.addChild(animation);
    }
}
