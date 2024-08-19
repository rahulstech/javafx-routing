package rahulstech.jfx.routing.lifecycle;

import javafx.scene.Node;
import rahulstech.jfx.routing.Router;

public abstract class SimpleLifecycleAwareController implements LifecycleAwareController {

    private Node root;

    private Router router;

    public SimpleLifecycleAwareController() {}

    @Override
    public void setRoot(Node root) {
        this.root = root;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void setRouter(Router router) {
        this.router = router;
    }

    @Override
    public Router getRouter() {
        return router;
    }

    @Override
    public void onLifecycleCreate() {}

    @Override
    public void onLifecycleInitialize() {}

    @Override
    public void onLifecycleShow() {}

    @Override
    public void onLifecycleHide() {}

    @Override
    public void onLifecycleDestroy() {}
}
