package rahulstech.jfx.routing.element;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.util.Duration;
import rahulstech.jfx.routing.parser.Attribute;
import rahulstech.jfx.routing.parser.AttributeSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// TODO: add interpolator and easing method and animation state
@SuppressWarnings("unused")
public abstract class RouterAnimation {

    public static final long DEFAULT_DURATION_SHORT_MILLIS = 320;

    public static final long DEFAULT_DURATION_LONG_MILLIS = 480;

    public static final long DEFAULT_DURATION_MILLIS = DEFAULT_DURATION_SHORT_MILLIS;

    public static final Duration DEFAULT_DURATION_SHORT = Duration.millis(DEFAULT_DURATION_SHORT_MILLIS);

    public static final Duration DEFAULT_DURATION_LONG = Duration.millis(DEFAULT_DURATION_SHORT_MILLIS);

    public static final Duration DEFAULT_DURATION = Duration.millis(DEFAULT_DURATION_MILLIS);

    public static final String NO_OP = "no_op";

    public static RouterAnimation getNoOpAnimation() {
        return new RouterAnimation(NO_OP) {
            @Override
            public void animate() {
                runOnStart();
                runOnFinish();
            }

            @Override
            public void reset() {}

            @Override
            public void stop() {}
        };
    }

    private String name;
    private Duration duration;
    private boolean autoReset = false;

    private List<RouterAnimationCallback> callbacks;

    private Node target;

    RouterAnimation nextAnimation;

    public RouterAnimation(String name) {
        this.name = name;
    }

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

    public static void addPendingAnimation(Node node, RouterAnimation animation) {
        Objects.requireNonNull(node, "node is null");
        Objects.requireNonNull(animation, "animation is null");
        node.getProperties().put(RouterAnimation.class,animation);
    }

    public static boolean hasPendingAnimation(Node node) {
        Objects.requireNonNull(node, "node is null");
        return node.getProperties().containsKey(RouterAnimation.class);
    }

    public static RouterAnimation getPendingAnimation(Node node) {
        Objects.requireNonNull(node, "node is null");
        return (RouterAnimation) node.getProperties().get(RouterAnimation.class);
    }

    public static RouterAnimation removePendingAnimation(Node node) {
        Objects.requireNonNull(node, "node is null");
        return (RouterAnimation) node.getProperties().remove(RouterAnimation.class);
    }

    public static void removePendingAnimation(Node node, RouterAnimation animation) {
        Objects.requireNonNull(node, "node is null");
        node.getProperties().remove(RouterAnimation.class, animation);
    }

    /********************************************************
     *                  Animation Properties                *
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

    /********************************************************
     *                  Callback Methods                    *
     ********************************************************/

    public List<RouterAnimationCallback> getCallbacks() {
        return callbacks;
    }

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

    public void removeRouterAnimationCallback(RouterAnimationCallback  callback) {
        if (null==callbacks) {
            return;
        }
        callbacks.remove(callback);
        if (callbacks.isEmpty()) {
            callbacks = null;
        }
    }

    public void removeAllRouterAnimationCallback() {
        if (null!=callbacks) {
            callbacks.clear();
            callbacks = null;
        }
    }

    /********************************************************
     *                    State Methods                     *
     ********************************************************/

    protected final void runOnStart() {
        onStart();
        if (null!=callbacks) {
            callbacks.forEach(callback -> callback.start(this));
        }
    }

    protected final void runOnFinish() {
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

    protected void onStart() {}

    protected void onFinish() {}

    public abstract void reset();

    protected abstract void animate();

    public void play() {
        Platform.runLater(this::animate);
    }

    public abstract void stop();

    void setNextAnimation(RouterAnimation next) {
        this.nextAnimation = next;
    }

    /********************************************************
     *                    Other Methods                     *
     ********************************************************/

    public void setTarget(Node target) {
        this.target = target;
    }

    public Node getTarget() {
        return target;
    }

    /********************************************************
     *                      Callbacks                       *
     ********************************************************/

    public interface RouterAnimationCallback {

        void start(RouterAnimation animation);

        void finish(RouterAnimation animation);
    }

    public static class SimpleRouterAnimationCallback implements RouterAnimationCallback {

        public SimpleRouterAnimationCallback() {}

        @Override
        public void start(RouterAnimation animation) {}

        @Override
        public void finish(RouterAnimation animation) {}
    }
}
