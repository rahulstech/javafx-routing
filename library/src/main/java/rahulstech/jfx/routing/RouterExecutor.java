package rahulstech.jfx.routing;

import rahulstech.jfx.routing.element.Destination;
import rahulstech.jfx.routing.util.Disposable;

/**
 * The {@code RouterExecutor} is an abstract class that provides the foundation
 * for executing routing operations within a {@link Router}. It manages
 * the display, hiding, and destruction of {@link Destination} objects
 * according to the routing rules defined in a specific implementation.
 *
 * <p>This class also handles the lifecycle events of a destination, ensuring
 * that resources are properly managed and released.</p>
 *
 * <p>Subclasses must implement the abstract methods to define the specific
 * behavior for showing, hiding, and popping the backstack of destinations.</p>
 *
 * <p>This class implements {@link Disposable} to provide a mechanism for
 * releasing resources when the executor is no longer needed.</p>
 *
 * @see Router
 * @see Destination
 * @see RouterOptions
 * @see Disposable
 * @author Rahul Bagchi
 * @since 1.0
 */
public abstract class RouterExecutor implements Disposable {

    private final Router router;

    /**
     * Creates new instance and associates the {@link Router}
     *
     * @param router the associated {@code Router}
     */
    protected RouterExecutor(Router router) {
        this.router = router;
    }

    /**
     * Returns the {@link Router} associated with this executor
     *
     * @return the {@link Router} associated with this executor.
     */
    public final Router getRouter() {
        return router;
    }

    /**
     * Displays the specified {@link Destination}
     *
     * @param destination the destination to be shown
     * @param options     the options for routing
     */
    public abstract void show(Destination destination, RouterOptions options);

    /**
     * Hides the specified {@link Destination}
     *
     * @param destination the destination to be hidden
     * @param options     the options for routing
     */
    public abstract void hide(Destination destination, RouterOptions options);

    /**
     * Removes and destroy the top destination, handled by this executor, and shows the requested destination.
     *
     * @param destination the destination to be shown next
     * @param options     the options for routing
     */
    public abstract void popBackstack(Destination destination, RouterOptions options);

    /**
     * Invokes the lifecycle "show" event for the specified {@link Destination}.
     * This method is not called during the normal lifecycle of the destination
     * i.e. during the {@link #show(Destination, RouterOptions) show}
     * or {@link #popBackstack(Destination, RouterOptions) popBackstack} . It triggers
     * lifecycle "show" event on an existing state of the destination. If no
     * state exists then nothing happens.
     *
     * @param destination the destination to trigger the lifecycle show event
     */
    public abstract void doLifecycleShow(Destination destination);

    /**
     * Invokes the lifecycle "hide" event for the specified {@link Destination}
     * This method is not called during the normal lifecycle of the destination
     * i.e. during the {@link #hide(Destination, RouterOptions) hide}
     * or {@link #popBackstack(Destination, RouterOptions) popBackstack}. It triggers
     * lifecycle "hide" event on an existing state of the destination. If no
     * state exists then nothing happens.
     *
     * @param destination the destination to trigger the lifecycle hide event
     */
    public abstract void doLifecycleHide(Destination destination);

    /**
     * Invokes the lifecycle "destroy" event for the specified {@link Destination}.
     * This method is not called during the normal lifecycle of the destination
     * i.e. during the {@link #popBackstack(Destination, RouterOptions) popBackstack}. It triggers
     * lifecycle "destroy" event on an existing state of the destination. If no
     * state exists then nothing happens.
     *
     * @param destination the destination to trigger the lifecycle destroy event
     */
    public abstract void doLifecycleDestroy(Destination destination);

    /** {@inheritDoc} */
    @Override
    public void dispose() {}
}
