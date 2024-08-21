package rahulstech.jfx.singlescenedemo;

import javafx.scene.Node;
import rahulstech.jfx.routing.Router;
import rahulstech.jfx.routing.RouterOptions;
import rahulstech.jfx.routing.element.Destination;
import rahulstech.jfx.routing.routerexecutor.SingleSceneScreenExecutor;
import rahulstech.jfx.routing.transaction.SingleSceneTransaction;

public class DemoRouterExecutor extends SingleSceneScreenExecutor {

    public DemoRouterExecutor(Router router, SingleSceneTransaction transaction) {
        super(router, transaction);
    }

    @Override
    protected SingleSceneTransaction.SingleSceneTarget createTarget(Destination destination, RouterOptions options) {
        DemoCustomLifecycleController controller = new DemoCustomLifecycleController();
        controller.setRouter(getRouter());
        DemoTarget target = new DemoTarget(destination.getId(),controller);
        target.onCreate();
        return target;
    }

    public static class DemoTarget extends SingleSceneTransaction.SingleSceneTarget {

        public DemoTarget(String tag, DemoCustomLifecycleController controller) {
            super(tag, controller);
        }

        @Override
        public DemoCustomLifecycleController getController() {
            return (DemoCustomLifecycleController) super.getController();
        }

        @Override
        public Node getNode() {
            return getController().getRootNode();
        }

        @Override
        public void onShow() {
            super.onShow();
            getController().onShow();
        }

        @Override
        public void onHide() {
            super.onHide();
            getController().onHide();
        }
    }
}
