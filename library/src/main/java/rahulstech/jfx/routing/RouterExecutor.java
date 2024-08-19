package rahulstech.jfx.routing;

import rahulstech.jfx.routing.element.Destination;
import rahulstech.jfx.routing.util.Disposable;

@SuppressWarnings("unused")
public abstract class RouterExecutor implements Disposable {

    private final Router router;

    protected RouterExecutor(Router router) {
        this.router = router;
    }

    public final Router getRouter() {
        return router;
    }

    public abstract void show(Destination destination, RouterOptions options);

    public abstract void hide(Destination destination, RouterOptions options);

    public abstract void popBackstack(Destination destination, RouterOptions options);

    public abstract void doLifecycleShow(Destination destination, RouterOptions options);

    public abstract void doLifecycleHide(Destination destination);

    @Override
    public void dispose() {}
}
