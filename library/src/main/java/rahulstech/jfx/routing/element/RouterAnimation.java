package rahulstech.jfx.routing.element;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.util.Duration;
import rahulstech.jfx.routing.parser.Attribute;
import rahulstech.jfx.routing.parser.AttributeSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * RouterAnimation is an abstraction layer. It gives you freedom to
 * use animation library of your choice and implement the required
 * functionalities in your animation library specific way.
 *
 * <h5>Define animation in router configuration </h5>
 *
 * <p>each "animation" tag at least provide "id" and "name" attributes.
 * "id" value must be unique among all the animations defined in the current
 * router configuration xml. "name"  is the name of the animation which is returned
 * by {@link RouterAnimation#getName()}
 * </p>
 *
 * <pre> {@code
 *  <animation id="enter_animation"
 *             name="slide_in_left"/>
 * }
 * </pre>
 *
 * <p>optionally you can set the animation specific from and to values and the duration.
 * valid durations are: 280ms 360ms 1500ms 5s 0.3s 1.2s etc. there are two constant
 * available for duration: duration_long and duration_short. these are same as
 * {@link RouterAnimation#DEFAULT_DURATION_LONG} and {@link RouterAnimation#DEFAULT_DURATION_SHORT}
 * respectively. if any attributes other than "id" and "name" is not set then default values
 * of that RouterAnimation implementation is used.
 * </p>
 *
 * <pre>{@code
 *  <animation id="exit_animation"
 *              name="slide_out_right"
 *              fromX="0%"
 *              toX="100%"
 *              autoReset="true"
 *              duration="280ms"/>
 * }
 * </pre>
 *
 * <h5>How to get animation</h5>
 * <p>to get an animation by name or id use
 * {@link rahulstech.jfx.routing.RouterContext#getAnimation(String) getAnimation(String)}
 * of {@link rahulstech.jfx.routing.RouterContext RouterContext}
 * </p>
 * <pre>{@code
 * RouterContext context = ...;
 * RouterAnimation animation = context.getAnimation(...);
 * }
 * </pre>
 */
// TODO: add interpolator and easing method
public abstract class RouterAnimation {

    /**
     * duration millis for animations that finishes quickly.
     * use short duration when the target is going through limited changes
     * or changes are clearly visible even in shorter duration.
     * for example: scale animation
     */
    public static final long DEFAULT_DURATION_SHORT_MILLIS = 320;

    /**
     * duration millis for animation that takes longer time to finish.
     * use long duration when the target is going through multiple changes
     * or changes are clearly visible only duration is longer.
     * for example: fade animation
     */
    public static final long DEFAULT_DURATION_LONG_MILLIS = 480;

    /**
     * default animation duration to use if no duration is set explicitly
     */
    public static final long DEFAULT_DURATION_MILLIS = DEFAULT_DURATION_SHORT_MILLIS;

    /**
     * a {@link Duration} instance for {@link RouterAnimation#DEFAULT_DURATION_SHORT}
     */
    public static final Duration DEFAULT_DURATION_SHORT = Duration.millis(DEFAULT_DURATION_SHORT_MILLIS);

    /**
     * a {@link Duration} instance for {@link #DEFAULT_DURATION_LONG}
     */
    public static final Duration DEFAULT_DURATION_LONG = Duration.millis(DEFAULT_DURATION_SHORT_MILLIS);

    /**
     * a {@link Duration} instance for {@link #DEFAULT_DURATION_MILLIS}
     */
    public static final Duration DEFAULT_DURATION = Duration.millis(DEFAULT_DURATION_MILLIS);

    /**
     * an animation that performs no visual changes, good for setting as default
     * animation
     */
    public static final String NO_OP = "no_op";

    public static RouterAnimation getNoOpAnimation() {
        return new RouterAnimation(NO_OP) {
            @Override
            public void animate() {
                runOnStart();
                runOnFinish();
            }

            @Override
            public void doReset() {}

            @Override
            public void stop() {}
        };
    }

    /**
     * unique name of the animation
     */
    private String name;

    /**
     * total duration of the animation
     */
    private Duration duration;

    /**
     * specify weather automatically to set initial value on
     * animation ends
     */
    private boolean autoReset = false;

    private List<RouterAnimationCallback> callbacks;

    private Node target;

    RouterAnimation nextAnimation;

    public RouterAnimation(String name) {
        this.name = name;
    }

    /**
     * apply values from router configuration xml. all RouterAnimation
     * implementation must implement this method if and only if they are
     * handling custom attribute. implementation of this method must call
     * the super method.
     *
     * @param attrs {@link AttributeSet} to apply
     */
    public void initialize(AttributeSet attrs) {
        if (attrs.isEmpty()) {
            return;
        }
        name = attrs.get(Attribute.NAME).getValue();
        Duration duration = attrs.getOrDefault(Attribute.DURATION,DEFAULT_DURATION_MILLIS+"ms").getAsDuration();
        boolean autoReset = attrs.getOrDefault(Attribute.AUTO_RESET,"false").getAsBoolean();
        setDuration(duration);
        setAutoReset(autoReset);
    }

    /********************************************************
     *                    Static Methods                    *
     ********************************************************/

    /**
     * add a reference to the animation currently running on the node.
     * you need to stop the previous animation, if available, before
     * applying new animation on a node. unfortunately javafx does not
     * provide any built in method to get the currently running animation.
     *
     * @param node the target {@link Node}
     * @param animation the {@link RouterAnimation} currently running
     * @throws NullPointerException if node or animation is null
     */
    public static void addPendingAnimation(Node node, RouterAnimation animation) {
        Objects.requireNonNull(node, "node is null");
        Objects.requireNonNull(animation, "animation is null");
        node.getProperties().put(RouterAnimation.class,animation);
    }

    /**
     * checks weather any pending RouterAnimation is available for the node
     *
     * @param node the target {@link Node}
     * @return {@literal true} if any pending animation found, {@literal false} otherwise
     * @throws NullPointerException if node is null
     */
    public static boolean hasPendingAnimation(Node node) {
        Objects.requireNonNull(node, "node is null");
        return node.getProperties().containsKey(RouterAnimation.class);
    }

    /**
     * get the pending animation from the node
     *
     * @param node the target node {@link Node}
     * @return instance of {@link RouterAnimation} if available or null
     * @throws NullPointerException if node is null
     */
    public static RouterAnimation getPendingAnimation(Node node) {
        Objects.requireNonNull(node, "node is null");
        return (RouterAnimation) node.getProperties().get(RouterAnimation.class);
    }

    /**
     * remove the pending animation and return the instance.
     *
     * @param node the target {@link Node}
     * @return instance of {@link RouterAnimation} if found or null
     * @throws NullPointerException if node is null
     */
    public static RouterAnimation removePendingAnimation(Node node) {
        Objects.requireNonNull(node, "node is null");
        return (RouterAnimation) node.getProperties().remove(RouterAnimation.class);
    }

    /**
     * remove the pending animation if and only if the pending animation is
     * the same instance as given
     *
     * @param node the target {@link Node}
     * @param animation the {@link RouterAnimation} instance to check
     * @throws NullPointerException if node is null
     */
    public static void removePendingAnimation(Node node, RouterAnimation animation) {
        Objects.requireNonNull(node, "node is null");
        node.getProperties().remove(RouterAnimation.class, animation);
    }

    /********************************************************
     *                  Public Methods                      *
     ********************************************************/

    public String getName() {
        return name;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setAutoReset(boolean autoReset) {
        this.autoReset = autoReset;
    }

    public boolean isAutoRest() {
        return autoReset;
    }

    public void setTarget(Node target) {
        this.target = target;
    }

    public Node getTarget() {
        return target;
    }

    private final ObjectProperty<State> state = new SimpleObjectProperty<>(State.INITIALIZED);

    public final ReadOnlyProperty<State> stateProperty() {
        return state;
    }

    public State getState() {
        return state.get();
    }

    private void setState(State value) {
        state.setValue(value);
    }

    /********************************************************
     *                  Callback Methods                    *
     ********************************************************/

    public List<RouterAnimationCallback> getCallbacks() {
        return callbacks;
    }

    /**
     * add a new callback
     *
     * @param callback an instance of {@link RouterAnimationCallback}
     * @throws NullPointerException if callback is null
     * @throws IllegalArgumentException if callback is already added
     */
    public void addRouterAnimationCallback(RouterAnimationCallback callback) {
        if (null==callback) {
            throw new NullPointerException("callback is null");
        }
        if (null==callbacks) {
            callbacks = new ArrayList<>();
        }
        if (callbacks.contains(callback)) {
            throw new IllegalArgumentException("callback already added");
        }
        callbacks.add(callback);
    }

    /**
     * remove an existing callback
     *
     * @param callback instance of  {@link RouterAnimationCallback}
     */
    public void removeRouterAnimationCallback(RouterAnimationCallback  callback) {
        if (null==callbacks) {
            return;
        }
        callbacks.remove(callback);
        if (callbacks.isEmpty()) {
            callbacks = null;
        }
    }

    /**
     * remove all attached callbacks
     */
    public void removeAllRouterAnimationCallback() {
        if (null!=callbacks) {
            callbacks.clear();
            callbacks = null;
        }
    }

    /********************************************************
     *                    State Methods                     *
     ********************************************************/

    /**
     * called when the animation starts or starts after reset.
     * if your animations however provides pause and restart facility,
     * this method must not be called on restart.
     */
    protected final void runOnStart() {
        if (getState().ordinal()>=State.STARTED.ordinal()) {
            return;
        }
        setState(State.STARTED);
        onStart();
        if (null!=callbacks) {
            callbacks.forEach(callback -> callback.start(this));
        }
    }

    /**
     * called when the animation finishes. if your animation provides pause
     * and restart facility, this method must not be called on pause.
     */
    protected final void runOnFinish() {
        if (getState().ordinal()>=State.FINISHED.ordinal()) {
            return;
        }
        setState(State.FINISHED);
        onFinish();
        if (null!=callbacks) {
            callbacks.forEach(callback -> callback.finish(this));
        }
        if (autoReset) {
            reset();
        }
        if (null!= nextAnimation) {
            nextAnimation.animate();
        }
    }

    /**
     * do something as soon as the animation starts
     */
    protected void onStart() {}

    /**
     * do something as soon as the animation finishes
     */
    protected void onFinish() {}

    /**
     * use this method to reset the animation. you must
     * call the {@link #stop()} before calling reset otherwise
     * animations will not reset properly.
     */
    public void reset() {
        doReset();
        setState(State.INITIALIZED);
    }

    /**
     * implement this method to set animation initial values
     * to the target
     */
    protected abstract void doReset();

    /**
     * implement the animation logic, start the animation from the beginning,
     * call the {@link #runOnStart()} as soon as the animation starts
     * and call {@link #runOnFinish()} as soon as the animation finishes.
     */
    protected abstract void animate();

    /**
     * start the animation from the beginning
     */
    public void play() {
        Platform.runLater(this::animate);
    }

    /**
     * stop the animation at the current position.
     */
    public abstract void stop();

    void setNextAnimation(RouterAnimation next) {
        this.nextAnimation = next;
    }

    /********************************************************
     *                      Subclasses                      *
     ********************************************************/

    /**
     * State provides the information of the animation current status.
     */
    public enum State {

        /**
         * the animation has not started yet
         */
        INITIALIZED,
        /**
         * the animation is currently running
         */
        STARTED,

        /**
         * the animation has finished
         */
        FINISHED
    }

    /**
     * A callback to get notified on animation state change
     */
    public interface RouterAnimationCallback {

        void start(RouterAnimation animation);

        void finish(RouterAnimation animation);
    }

    /**
     * A simple implementation of {@link RouterAnimationCallback}
     */
    public static class SimpleRouterAnimationCallback implements RouterAnimationCallback {

        public SimpleRouterAnimationCallback() {}

        @Override
        public void start(RouterAnimation animation) {}

        @Override
        public void finish(RouterAnimation animation) {}
    }
}
