package rahulstech.jfx.routing.lifecycle;

import javafx.scene.Node;
import rahulstech.jfx.routing.Router;

/**
 * A simple implementation of {@link LifecycleAwareController}
 *
 * @author Rahul Bagchi
 * @since 1.0
 */
public abstract class SimpleLifecycleAwareController implements LifecycleAwareController {

    private Node root;

    private Router router;

    /**
     * Creaets new {@code SimpleLifecycleAwareController} instnace
     */
    public SimpleLifecycleAwareController() {}

    /** {@inheritDoc} */
    @Override
    public void setRoot(Node root) {
        this.root = root;
    }

    /** {@inheritDoc} */
    @Override
    public Node getRoot() {
        return root;
    }

    /** {@inheritDoc} */
    @Override
    public void setRouter(Router router) {
        this.router = router;
    }

    /** {@inheritDoc} */
    @Override
    public Router getRouter() {
        return router;
    }

    /** {@inheritDoc} */
    @Override
    public void onLifecycleCreate() {}

    /** {@inheritDoc} */
    @Override
    public void onLifecycleInitialize() {}

    /** {@inheritDoc} */
    @Override
    public void onLifecycleShow() {}

    /** {@inheritDoc} */
    @Override
    public void onLifecycleHide() {}

    /** {@inheritDoc} */
    @Override
    public void onLifecycleDestroy() {}
}
