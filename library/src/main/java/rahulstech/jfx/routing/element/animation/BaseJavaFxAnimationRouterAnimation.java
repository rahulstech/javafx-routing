package rahulstech.jfx.routing.element.animation;

import javafx.animation.Animation;
import javafx.scene.Node;
import rahulstech.jfx.routing.element.RouterAnimation;

abstract class BaseJavaFxAnimationRouterAnimation extends RouterAnimation {

    private Animation animation;

    public BaseJavaFxAnimationRouterAnimation(String name) {
        super(name);
    }

    protected abstract Animation createAnimation(Node node);

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

    @Override
    public void stop() {
        if (null!=animation) {
            animation.stop();
        }
    }
}
