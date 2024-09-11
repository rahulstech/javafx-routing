package rahulstech.jfx.routing;

import rahulstech.jfx.routing.util.StringUtil;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

    private static final String KEY_ENTER_ANIMATION = "rahulstech.jfx.routeroptions.ENTER_ANIMATION";

    private static final String KEY_EXIT_ANIMATION = "rahulstech.jfx.routeroptions.EXIT_ANIMATION";

    private static final String KEY_POP_ENTER_ANIMATION = "rahulstech.jfx.routeroptions.POP_ENTER_ANIMATION";

    private static final String KEY_POP_EXIT_ANIMATION = "rahulstech.jfx.routeroptions.POP_EXIT_ANIMATION";

    private static final String KEY_RESOURCE_BUNDLE = "rahulstech.jfx.routeroptions.RESOURCE_BUNDLE";

    private static final String KEY_CHARSET = "rahulstech.jfx.routeroptions.CHARSET";

    private static final String KEY_POP_BACKSTACK = "rahulstech.jfx.routeroptions.POP_BACKSTACK";


    final Map<String,Object> map = new HashMap<>();

    /**
     * Creates a new instance of {@code RouterOptions} with default settings.
     */
    public RouterOptions() {
        this(null);
    }

    /**
     * Creates a new instance of {@code RouterOptions} by copying the settings from another instance.
     *
     * @param from the {@code RouterOptions} instance to copy settings from
     * @see #apply(RouterOptions) 
     */
    public RouterOptions(RouterOptions from) {
        if (null!=from) {
            apply(from);
        }
    }

    /**
     * Copies the settings from another {@code RouterOptions} instance into this instance.
     *
     * @param from the {@code RouterOptions} instance to copy settings from
     */
    public void apply(RouterOptions from) {
        if (null==from) {
            throw new NullPointerException("source RouterOptions is null");
        }
        this.map.putAll(from.map);
    }

    /**
     * Adds a new key-value pair
     *
     * @param key the key
     * @param value the value
     * @return this {@code RouterOptions} instance
     * @since 2.0
     */
    public RouterOptions add(String key, Object value) {
        if (StringUtil.isEmpty(key)) {
            throw new IllegalArgumentException("key is empty");
        }
        map.put(key,value);
        return this;
    }

    /**
     * Return the value associated to the given key
     *
     * @param key the key
     * @return the value associated to the key or {@code null}
     * @param <T> the desired return type of value
     * @throws ClassCastException if value can not be cast to return type
     * @since 2.0
     */
    public <T> T get(String key) {
        return get(key,null);
    }

    /**
     * Returns the value associated to the key or the provided defaultValue if key does not exist
     *
     * @param key the key
     * @param defaultValue the value to return if key does not exist
     * @return the value associated to the key or the default value
     * @param <T> the desired return type of value
     * @throws ClassCastException if value can not be cast to return type
     * @since 2.0
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, T defaultValue) {
        return (T) map.getOrDefault(key,defaultValue);
    }

    /**
     * Returns the value associated to the key as {@link Optional}
     *
     * @param key the key
     * @return non-null {@code Optional} containing the value or an empty Optional
     * @param <T> the desired return type of the value
     * @throws ClassCastException if value can not be cast to return type
     * @since 2.0
     */
    public <T> Optional<T> getOptional(String key) {
        return Optional.ofNullable(get(key));
    }

    /**
     * Remove the value associated to the key and return the value
     *
     * @param key the key
     * @return the value associated to the key or {@code null}
     * @since 2.0
     */
    public Object remove(String key) {
        return map.remove(key);
    }

    /**
     * Removes all key-value pairs
     * @since 2.0
     */
    public void clear() {
        map.clear();
    }

    /**
     * Returns the name or id of the screen enter animation
     *
     * @return the enter animation setting, or {@code null} if not set
     */
    public String getEnterAnimation() {
        return get(KEY_ENTER_ANIMATION);
    }

    /**
     * Returns the name or id of the screen enter animation or the default animation name if not enter animation defined
     *
     * @param defaultValue the default value to return if enter animation is not set
     * @return the enter animation setting, or {@code defaultValue} if not set
     */
    public String getEnterAnimation(String defaultValue) {
        return get(KEY_ENTER_ANIMATION,defaultValue);
    }

    /**
     * Sets the enter animation.
     *
     * @param enterAnimation the enter animation setting
     * @return this {@code RouterOptions} instance for chaining
     */
    public RouterOptions setEnterAnimation(String enterAnimation) {
        return add(KEY_ENTER_ANIMATION,enterAnimation);
    }

    /**
     * Returns the name or id of the screen exit animation
     *
     * @return the exit animation setting, or {@code null} if not set
     */
    public String getExitAnimation() {
        return get(KEY_EXIT_ANIMATION);
    }

    /**
     * Returns the name or id of the screen exit animation or the default animation name if no screen exit animation is defined.
     *
     * @param defaultValue the default value to return if exit animation is not set
     * @return the exit animation setting, or {@code defaultValue} if not set
     */
    public String getExitAnimation(String defaultValue) {
        return get(KEY_EXIT_ANIMATION,defaultValue);
    }

    /**
     * Sets the exit animation.
     *
     * @param exitAnimation the exit animation setting
     * @return this {@code RouterOptions} instance for chaining
     */
    public RouterOptions setExitAnimation(String exitAnimation) {
        return add(KEY_EXIT_ANIMATION,exitAnimation);
    }

    /**
     * Returns the name or id of the screen pop enter animation
     *
     * @return the pop enter animation setting, or {@code null} if not set
     */
    public String getPopEnterAnimation() {
        return get(KEY_POP_ENTER_ANIMATION);
    }

    /**
     * Returns the name or id of the screen pop enter animation or default animation name if no screen pop enter animation defined.
     *
     * @param defaultValue the default value to return if pop enter animation is not set
     * @return the pop enter animation setting, or {@code defaultValue} if not set
     */
    public String getPopEnterAnimation(String defaultValue) {
        return get(KEY_POP_ENTER_ANIMATION,defaultValue);
    }

    /**
     * Sets the pop enter animation.
     *
     * @param popEnterAnimation the pop enter animation setting
     * @return this {@code RouterOptions} instance for chaining
     */
    public RouterOptions setPopEnterAnimation(String popEnterAnimation) {
        return add(KEY_POP_ENTER_ANIMATION,popEnterAnimation);
    }

    /**
     * Returns the name or id of the screen pop exit animation
     *
     * @return the pop exit animation setting, or {@code null} if not set
     */
    public String getPopExitAnimation() {
        return get(KEY_POP_EXIT_ANIMATION);
    }

    /**
     * Returns the name or id of the screen pop exit animation or default animation name if no screen pop exit animation is defined
     *
     * @param defaultValue the default value to return if pop exit animation is not set
     * @return the pop exit animation setting, or {@code defaultValue} if not set
     */
    public String getPopExitAnimation(String defaultValue) {
        return get(KEY_POP_EXIT_ANIMATION,defaultValue);
    }

    /**
     * Sets the pop exit animation.
     *
     * @param popExitAnimation the pop exit animation setting
     * @return this {@code RouterOptions} instance for chaining
     */
    public RouterOptions setPopExitAnimation(String popExitAnimation) {
        return add(KEY_POP_EXIT_ANIMATION,popExitAnimation);
    }

    /**
     * Sets the {@link ResourceBundle} for this {@code RouterOptions}.
     *
     * @param bundle the resource bundle for localization
     * @return this {@code RouterOptions} instance for chaining
     */
    public RouterOptions setBundle(ResourceBundle bundle) {
        return add(KEY_RESOURCE_BUNDLE,bundle);
    }

    /**
     * Return
     * @return the {@link ResourceBundle}, or {@code null} if not set
     */
    public ResourceBundle getBundle() {
        return get(KEY_RESOURCE_BUNDLE);
    }

    /**
     * Sets the {@link Charset} for character encoding in this {@code RouterOptions}.
     *
     * @param charset the character set to use
     * @return this {@code RouterOptions} instance for chaining
     */
    public RouterOptions setCharset(Charset charset) {
        return add(KEY_CHARSET,charset);
    }

    /**
     * Returns {@link Charset}
     *
     * @return the {@link Charset}, or {@code null} if not set
     */
    public Charset getCharset() {
        return get(KEY_CHARSET);
    }

    /**
     * Sets whether it is a popping operation
     *
     * @param popping {@code true} means popping, {@code false} otherwise
     * @return the current {@code RouterOptions} instance
     */
    public RouterOptions setPopBackstack(boolean popping) {
        return add(KEY_POP_BACKSTACK,popping);
    }

    /**
     * Returns whether it is a popping operation. Default value if {@code false} means not popping.
     *
     * @return {@code true} means popping, {@code false} otherwise
     */
    public boolean getPopBackStack() {
        return get(KEY_POP_BACKSTACK,false);
    }
}
