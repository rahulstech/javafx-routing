package rahulstech.jfx.routing.routerexecutor;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import rahulstech.jfx.routing.Router;
import rahulstech.jfx.routing.RouterContext;
import rahulstech.jfx.routing.RouterExecutor;
import rahulstech.jfx.routing.RouterOptions;
import rahulstech.jfx.routing.element.Destination;
import rahulstech.jfx.routing.element.RouterAnimation;
import rahulstech.jfx.routing.lifecycle.LifecycleAwareController;
import rahulstech.jfx.routing.transaction.SingleSceneTransaction;

import java.util.Objects;

@SuppressWarnings("unused")
public class SingleSceneScreenExecutor extends RouterExecutor {

    private SingleSceneTransaction transaction;

    public SingleSceneScreenExecutor(Router router, SingleSceneTransaction transaction) {
        super(router);
        this.transaction = transaction;
    }

    @Override
    public void show(Destination destination, RouterOptions options) {
        String enterAnimationId = options.getEnterAnimation();
        String exitAnimationId = options.getExitAnimation();
        RouterAnimation enterAnimation = getAnimation(enterAnimationId);
        RouterAnimation exitAnimation = getAnimation(exitAnimationId);

        SingleSceneTransaction.SingleSceneTarget target = createTarget(destination,options);

        transaction.begin().replace(target,enterAnimation,exitAnimation).commit();
    }

    @Override
    public void popBackstack(Destination destination, RouterOptions options) {
        String id = destination.getId();
        String enterAnimationId = options.getPopEnterAnimation();
        String exitAnimationId = options.getPopExitAnimation();
        RouterAnimation enterAnimation = getAnimation(enterAnimationId);
        RouterAnimation exitAnimation = getAnimation(exitAnimationId);
        transaction.begin().popBackstack(id,enterAnimation,exitAnimation).commit();
    }

    @Override
    public void doLifecycleShow(Destination destination, RouterOptions options) {
        String id = destination.getId();
        transaction.getBackstack()
                .findFirst(target -> id.equals(target.getTag()))
                .ifPresent(target -> transaction.doForcedShow(target));
    }

    @Override
    public void doLifecycleHide(Destination destination) {
        String id = destination.getId();
        transaction.getBackstack()
                .findFirst(target -> id.equals(target.getTag()))
                .ifPresent(target -> transaction.doForcedHide(target));
    }

    @Override
    public void hide(Destination destination, RouterOptions options) {}

    public RouterAnimation getAnimation(String id) {
        RouterContext context = getRouter().getContext();
        return Objects.requireNonNullElse(context.getAnimation(id),context.getAnimation(RouterAnimation.NO_OP));
    }

    @Override
    public void dispose() {
        super.dispose();
        transaction = null;
    }

    protected SingleSceneTransaction.SingleSceneTarget createTarget(Destination destination, RouterOptions options) {
        Class<?> controllerClass = destination.getControllerClass();
        String fxml = destination.getFXML();
        LifecycleAwareController controller;
        if (null!=fxml) {
            controller = doCreateFromFxml(fxml,controllerClass,options);
        }
        else {
            controller = doCreateFromClass(controllerClass,options);
        }
        SingleSceneTransaction.SingleSceneTarget target
                = new SingleSceneTransaction.SingleSceneTarget(destination.getId(),controller,controller.getRoot());
        target.onCreate();
        return target;
    }

    protected LifecycleAwareController doCreateFromFxml(String fxml, Class<?> controllerClass, RouterOptions options) {
        Router router = getRouter();
        FXMLLoader loader = router.getContext()
                .getFxmlLoader(fxml,controllerClass,options.getBundle(),options.getCharset());
        try {
            Node root = loader.load();
            LifecycleAwareController controller = loader.getController();
            controller.setRoot(root);
            controller.setRouter(router);
            return controller;
        }
        catch (Exception ex) {
            throw new RuntimeException("fxml load failed with exception "+ex.getMessage());
        }
    }

    protected LifecycleAwareController doCreateFromClass(Class<?> controllerClass, RouterOptions options) {
        Router router = getRouter();
        RouterContext context = router.getContext();
        LifecycleAwareController controller = (LifecycleAwareController) context.newControllerInstance(controllerClass);
        controller.setRouter(router);
        return controller;
    }
}
