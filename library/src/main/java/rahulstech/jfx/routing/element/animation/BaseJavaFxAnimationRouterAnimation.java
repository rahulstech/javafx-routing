package rahulstech.jfx.routing.element.animation;

import javafx.animation.Animation;
import javafx.scene.Node;
import rahulstech.jfx.routing.element.RouterAnimation;

/**
 * The {@code BaseJavaFxAnimationRouterAnimation} class is an abstract base class
 * for implementing animations in JavaFX that are used within the context of router animations.
 * It extends {@link RouterAnimation} and provides methods to create and control JavaFX animations.
 *
 * @author Rahul Bagchi
 * @since 1.0
 */
abstract class BaseJavaFxAnimationRouterAnimation extends RouterAnimation {

    private Animation animation;

    public BaseJavaFxAnimationRouterAnimation(String name) {
        super(name);
    }

    /**
     * Creates the JavaFX {@link Animation} for this router animation.
     * This method must be implemented by subclasses to define the specific animation.
     *
     * @param node the {@link Node} to be animated
     * @return the {@link Animation} instance to be used
     */
    protected abstract Animation createAnimation(Node node);

    /** {@inheritDoc} */
    @Override
    protected void animate() {
        Node node = getTarget();
        if (null==node) {
            throw new NullPointerException("target not set");
        }
        Animation animation = createAnimation(node);
        if (null==animation) {
            throw new NullPointerException("animation not created, use createAnimation(Node,Node) before calling animate()");
        }
        this.animation = animation;
        animation.statusProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue==Animation.Status.RUNNING) {
                runOnStart();
            }
            else if (newValue==Animation.Status.STOPPED) {
                runOnFinish();
            }
        });
        animation.playFromStart();
    }

    /** {@inheritDoc} */
    @Override
    public void stop() {
        if (null!=animation) {
            animation.stop();
        }
    }
}
