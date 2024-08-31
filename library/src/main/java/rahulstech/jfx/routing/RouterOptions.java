package rahulstech.jfx.routing;

import rahulstech.jfx.routing.util.StringUtil;

import java.nio.charset.Charset;
import java.util.ResourceBundle;

/**
 * The {@code RouterOptions} class encapsulates configuration options for routing animations
 * and additional parameters within a {@link Router}. It allows customization of enter and
 * exit animations for both forward and backward navigation (pop operations), along with
 * providing a {@link ResourceBundle} for localization and a {@link Charset} for character
 * encoding preferences.
 *
 * <p>This class provides methods to set and retrieve these options, including fallback values
 * for animation settings when none are explicitly provided.</p>
 *
 * <p>Instances of {@code RouterOptions} can be copied and applied from other instances, allowing
 * for flexible reuse of routing configurations.</p>
 *
 * @see Router
 * @author Rahul Bagchi
 * @since 1.0
 */
public class RouterOptions {

    private String enterAnimation;

    private String exitAnimation;

    private String popEnterAnimation;

    private String popExitAnimation;
    
    private ResourceBundle bundle;
    
    private Charset charset;

    /**
     * Creates a new instance of {@code RouterOptions} with default settings.
     */
    public RouterOptions() {}

    /**
     * Creates a new instance of {@code RouterOptions} by copying the settings from another instance.
     *
     * @param from the {@code RouterOptions} instance to copy settings from
     * @see #apply(RouterOptions) 
     */
    public RouterOptions(RouterOptions from) {
        apply(from);
    }

    /**
     * Copies the settings from another {@code RouterOptions} instance into this instance.
     *
     * @param from the {@code RouterOptions} instance to copy settings from
     */
    public void apply(RouterOptions from) {
        enterAnimation = from.enterAnimation;
        exitAnimation = from.exitAnimation;
        popEnterAnimation = from.popEnterAnimation;
        popExitAnimation = from.popExitAnimation;
        bundle = from.bundle;
        charset = from.charset;
    }

    /**
     * Returns the name or id of the screen enter animation
     *
     * @return the enter animation setting, or {@code null} if not set
     */
    public String getEnterAnimation() {
        return enterAnimation;
    }

    /**
     * Returns the name or id of the screen enter animation or the default animation name if not enter animation defined
     *
     * @param defaultValue the default value to return if enter animation is not set
     * @return the enter animation setting, or {@code defaultValue} if not set
     */
    public String getEnterAnimation(String defaultValue) {
        return StringUtil.isEmpty(enterAnimation) ? defaultValue : enterAnimation;
    }

    /**
     * Sets the enter animation.
     *
     * @param enterAnimation the enter animation setting
     * @return this {@code RouterOptions} instance for chaining
     */
    public RouterOptions setEnterAnimation(String enterAnimation) {
        this.enterAnimation = enterAnimation;
        return this;
    }

    /**
     * Returns the name or id of the screen exit animation
     *
     * @return the exit animation setting, or {@code null} if not set
     */
    public String getExitAnimation() {
        return exitAnimation;
    }

    /**
     * Returns the name or id of the screen exit animation or the default animation name if no screen exit animation is defined.
     *
     * @param defaultValue the default value to return if exit animation is not set
     * @return the exit animation setting, or {@code defaultValue} if not set
     */
    public String getExitAnimation(String defaultValue) {
        return StringUtil.isEmpty(exitAnimation) ? defaultValue : exitAnimation;
    }

    /**
     * Sets the exit animation.
     *
     * @param exitAnimation the exit animation setting
     * @return this {@code RouterOptions} instance for chaining
     */
    public RouterOptions setExitAnimation(String exitAnimation) {
        this.exitAnimation = exitAnimation;
        return this;
    }

    /**
     * Returns the name or id of the screen pop enter animation
     *
     * @return the pop enter animation setting, or {@code null} if not set
     */
    public String getPopEnterAnimation() {
        return popEnterAnimation;
    }

    /**
     * Returns the name or id of the screen pop enter animation or default animation name if no screen pop enter animation defined.
     *
     * @param defaultValue the default value to return if pop enter animation is not set
     * @return the pop enter animation setting, or {@code defaultValue} if not set
     */
    public String getPopEnterAnimation(String defaultValue) {
        return StringUtil.isEmpty(popEnterAnimation) ? defaultValue : popEnterAnimation;
    }

    /**
     * Sets the pop enter animation.
     *
     * @param popEnterAnimation the pop enter animation setting
     * @return this {@code RouterOptions} instance for chaining
     */
    public RouterOptions setPopEnterAnimation(String popEnterAnimation) {
        this.popEnterAnimation = popEnterAnimation;
        return this;
    }

    /**
     * Returns the name or id of the screen pop exit animation
     *
     * @return the pop exit animation setting, or {@code null} if not set
     */
    public String getPopExitAnimation() {
        return popExitAnimation;
    }

    /**
     * Returns the name or id of the screen pop exit animation or default animation name if no screen pop exit animation is defined
     *
     * @param defaultValue the default value to return if pop exit animation is not set
     * @return the pop exit animation setting, or {@code defaultValue} if not set
     */
    public String getPopExitAnimation(String defaultValue) {
        return StringUtil.isEmpty(popExitAnimation) ? defaultValue : popExitAnimation;
    }

    /**
     * Sets the pop exit animation.
     *
     * @param popExitAnimation the pop exit animation setting
     * @return this {@code RouterOptions} instance for chaining
     */
    public RouterOptions setPopExitAnimation(String popExitAnimation) {
        this.popExitAnimation = popExitAnimation;
        return this;
    }

    /**
     * Sets the {@link ResourceBundle} for this {@code RouterOptions}.
     *
     * @param bundle the resource bundle for localization
     * @return this {@code RouterOptions} instance for chaining
     */
    public RouterOptions setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
        return this;
    }

    /**
     * Return
     * @return the {@link ResourceBundle}, or {@code null} if not set
     */
    public ResourceBundle getBundle() {
        return bundle;
    }

    /**
     * Sets the {@link Charset} for character encoding in this {@code RouterOptions}.
     *
     * @param charset the character set to use
     * @return this {@code RouterOptions} instance for chaining
     */
    public RouterOptions setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    /**
     * Returns {@link Charset}
     *
     * @return the {@link Charset}, or {@code null} if not set
     */
    public Charset getCharset() {
        return charset;
    }
}
