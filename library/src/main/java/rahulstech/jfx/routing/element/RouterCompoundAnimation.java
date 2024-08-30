package rahulstech.jfx.routing.element;

import javafx.scene.Node;
import javafx.util.Duration;
import rahulstech.jfx.routing.parser.Attribute;
import rahulstech.jfx.routing.parser.AttributeSet;

import java.util.*;

/**
 * The {@code RouterCompoundAnimation} class is an implementation of {@link RouterAnimation}
 * that combines multiple {@link RouterAnimation} instances into a single compound animation.
 *
 * <p>This class supports both parallel and sequential play modes for its child animations.
 * In sequential mode each child animation will be played one after another in the same sequence
 * they are added to this compound animation. In parallet mode all the children animation will be
 * played together. Default {@code PlayMode} is {@link PlayMode#PARALLEL PARALLEL}.
 * </p>
 *
 * <p>To set playmode in router configuration xml file use {@code playMode} attribute in {@code compound-animation}
 * element. {@code playMode} attribute takes either of the values "PARALLEL" or "SEQUENTIAL".
 * </p>
 *
 * @see PlayMode PlayMode
 *
 * @since 1.0
 * @author Rahul Bagchi
 */
public class RouterCompoundAnimation extends RouterAnimation {

    private List<RouterAnimation> children = new ArrayList<>();

    private PlayMode mode;

    /**
     * Creates a new {@code RouterCompoundAnimation} with the default name and the specified child animations.
     *
     * @param animations the child animations to be added
     */
    public RouterCompoundAnimation(RouterAnimation... animations) {
        this("RouterCompoundAnimation", animations);
    }

    /**
     * Creates a new {@code RouterCompoundAnimation} with the specified name and child animations.
     *
     * @param name        the name of the animation
     * @param animations  the child animations to be added
     */
    public RouterCompoundAnimation(String name, RouterAnimation... animations) {
        this(name,PlayMode.PARALLEL,animations);
    }

    /**
     * Creates a new {@code RouterCompoundAnimation} with the specified name, play mode, and child animations.
     *
     * @param name        the name of the animation
     * @param mode        the play mode of the animation
     * @param animations  the child animations to be added
     */
    public RouterCompoundAnimation(String name, PlayMode mode, RouterAnimation... animations) {
        super(name);
        setMode(mode);
        addChildren(animations);
        setDuration(DEFAULT_DURATION_SHORT);
    }

    /** {@inheritDoc} */
    @Override
    public void initialize(AttributeSet attrs) {
        if (attrs.isEmpty()) {
            return;
        }

        children.forEach(child->{
            String name = child.getName();
            AttributeSet subset = attrs.getAttributeSetWithPrefix(name);
            child.initialize(subset);
        });

        super.initialize(attrs);

        String mode = attrs.getOrDefault(Attribute.PLAY_MODE,PlayMode.PARALLEL.name()).getValue();
        setMode(PlayMode.valueOf(mode));
    }

    public Collection<RouterAnimation> getChildren() {
        return Collections.unmodifiableCollection(children);
    }

    /**
     * Gets the collection of child animations.
     *
     * @return an unmodifiable collection of child animations
     */
    public RouterAnimation getChildByName(String name) {
        return children.stream().filter(child->child.getName().equals(name)).findFirst().orElse(null);
    }

    /**
     * Adds a child animation to this compound animation.
     *
     * @param animation the child animation to be added
     */
    public void addChild(RouterAnimation animation) {
        if (null==children) {
            children = new ArrayList<>();
        }
        children.add(animation);
    }

    /**
     * Adds multiple child animations to this compound animation.
     *
     * @param animations the child animations to be added
     */
    public void addChildren(RouterAnimation... animations) {
        if (null==children) {
            children = new ArrayList<>();
        }
        if (animations.length > 0) {
            Arrays.stream(animations).forEach(this::addChild);
        }
    }

    /**
     * Adds a collection of child animations to this compound animation.
     *
     * @param animations the collection of child animations to be added
     */
    public void addChildren(Collection<RouterAnimation> animations) {
        if (null!=animations && !animations.isEmpty()) {
            animations.forEach(this::addChild);
        }
    }

    /**
     * Gets the current play mode of this compound animation.
     *
     * @return the play mode
     */
    public PlayMode getMode() {
        return mode;
    }

    /**
     * Sets the play mode for this compound animation.
     *
     * @param mode the play mode to be set
     */
    public void setMode(PlayMode mode) {
        this.mode = mode;
    }

    /**
     * Sets the duration for this compound animation and its child animations.
     *
     * @param duration the duration to be set
     */
    @Override
    public void setDuration(Duration duration) {
        super.setDuration(duration);
        children.forEach(animation -> animation.setDuration(duration));
    }

    /**
     * Sets whether the animation should be automatically reset after completion.
     * Each of the children animations auto-reset value will be set to this value.
     *
     * @param autoReset {@code true} to enable auto-reset, {@code false} otherwise
     */
    @Override
    public void setAutoReset(boolean autoReset) {
        super.setAutoReset(autoReset);
        children.forEach(child->child.setAutoReset(autoReset));
    }

    /**
     * Sets the target {@link Node} for this compound animation and its child animations.
     *
     * @param target the target {@link Node}
     */
    @Override
    public void setTarget(Node target) {
        super.setTarget(target);
        children.forEach(animation->animation.setTarget(target));
    }

    /**
     * Resets this compound animation and all of its child animations.
     */
    @Override
    public void reset() {
        children.forEach(RouterAnimation::reset);
    }

    /** {@inheritDoc} */
    @Override
    protected void doReset() {}

    /** {@inheritDoc} */
    @Override
    public void stop() {
        children.forEach(RouterAnimation::stop);
    }

    /** {@inheritDoc} */
    @Override
    protected void animate() {
        Collection<RouterAnimation> children = this.children;
        if (children.isEmpty()) {
            runOnStart();
            runOnFinish();
            return;
        }
        RouterAnimation[] animations = children.toArray(new RouterAnimation[0]);
        PlayMode mode = getMode();
        RouterAnimation first = animations[0];
        RouterAnimation last = animations[animations.length - 1];
        first.addRouterAnimationCallback(new SimpleRouterAnimationCallback() {
            @Override
            public void start(RouterAnimation animation) {
                runOnStart();
            }
        });
        last.addRouterAnimationCallback(new SimpleRouterAnimationCallback() {
            @Override
            public void finish(RouterAnimation animation) {
                runOnFinish();
            }
        });
        if (mode == PlayMode.SEQUENTIAL) {
            animateSequential(animations);
        } else {
            animateParallel(animations);
        }
    }

    /**
     * Starts all child animations in parallel.
     *
     * @param animations the child animations to be played
     */
    void animateParallel(RouterAnimation[] animations) {
        for (RouterAnimation animation : animations) {
            animation.animate();
        }
    }

    /**
     * Starts all child animations sequentially.
     *
     * @param animations the child animations to be played
     */
    void animateSequential(RouterAnimation[] animations) {
        int size = animations.length;
        for (int i=0; i<size; i++) {
            RouterAnimation animation = animations[i];
            int nextIndex = i+1;
            if (nextIndex < size) {
                RouterAnimation next = animations[nextIndex];
                animation.setNextAnimation(next);
            }
        }
        RouterAnimation first = animations[0];
        first.animate();
    }

    /**
     * The {@code PlayMode} enum defines the possible play modes for a compound animation.
     */
    public enum PlayMode {
        /**
         * Play child animations one after another.
         */
        SEQUENTIAL,

        /**
         * Play all child animations simultaneously.
         */
        PARALLEL,
    }
}
