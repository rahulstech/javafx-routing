package rahulstech.jfx.routing.element;

import javafx.scene.Node;
import javafx.util.Duration;
import rahulstech.jfx.routing.parser.Attribute;
import rahulstech.jfx.routing.parser.AttributeSet;

import java.util.*;

/**
 *
 */
public class RouterCompoundAnimation extends RouterAnimation {

    private List<RouterAnimation> children = new ArrayList<>();

    private PlayMode mode;

    public RouterCompoundAnimation() {
        this("RouterCompoundAnimation");
    }

    public RouterCompoundAnimation(RouterAnimation... animations) {
        this("RouterCompoundAnimation",animations);
    }

    public RouterCompoundAnimation(String name) {
        this(name,PlayMode.PARALLEL);
    }

    public RouterCompoundAnimation(String name, RouterAnimation... animations) {
        this(name,PlayMode.PARALLEL,animations);
    }

    public RouterCompoundAnimation(String name, PlayMode mode, RouterAnimation... animations) {
        super(name);
        setMode(mode);
        addChildren(animations);
        setDuration(DEFAULT_DURATION_SHORT);
    }

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

    public RouterAnimation getChildByName(String name) {
        return children.stream().filter(child->child.getName().equals(name)).findFirst().orElse(null);
    }

    public void addChild(RouterAnimation animation) {
        if (null==children) {
            children = new ArrayList<>();
        }
        children.add(animation);
    }

    public void addChildren(RouterAnimation... animations) {
        if (null==children) {
            children = new ArrayList<>();
        }
        if (animations.length > 0) {
            Arrays.stream(animations).forEach(this::addChild);
        }
    }

    public void addChildren(Collection<RouterAnimation> animations) {
        if (null!=animations && !animations.isEmpty()) {
            animations.forEach(this::addChild);
        }
    }

    public PlayMode getMode() {
        return mode;
    }

    public void setMode(PlayMode mode) {
        this.mode = mode;
    }

    @Override
    public void setDuration(Duration duration) {
        super.setDuration(duration);
        children.forEach(animation -> animation.setDuration(duration));
    }

    @Override
    public void setAutoReset(boolean autoReset) {
        super.setAutoReset(autoReset);
        children.forEach(child->child.setAutoReset(autoReset));
    }

    @Override
    public void setTarget(Node target) {
        super.setTarget(target);
        children.forEach(animation->animation.setTarget(target));
    }

    @Override
    public void reset() {
        children.forEach(RouterAnimation::reset);
    }

    @Override
    protected void doReset() {}

    @Override
    public void stop() {
        children.forEach(RouterAnimation::stop);
    }

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

    void animateParallel(RouterAnimation[] animations) {
        for (RouterAnimation animation : animations) {
            animation.animate();
        }
    }

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
     *
     */
    public enum PlayMode {
        SEQUENTIAL,
        PARALLEL,
    }
}
