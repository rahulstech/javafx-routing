package rahulstech.jfx.routing;

import rahulstech.jfx.routing.backstack.Backstack;
import rahulstech.jfx.routing.backstack.BackstackEntry;
import rahulstech.jfx.routing.util.Disposable;
import rahulstech.jfx.routing.util.StringUtil;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * The {@code Transaction} class is an abstract base class that manages a set of operations
 * and a backstack of {@link Target} objects, which represent specific state of a destinations or
 * screens in an application. It provides mechanisms for queuing and executing operations,
 * managing the lifecycle of targets i.e. screens and handling disposal of resources.
 *
 * <p>Subclasses must implement methods to forcefully show, hide, and destroy targets.
 * This class also ensures that resources are properly disposed of when no longer needed.</p>
 *
 * @see Backstack
 * @see Target
 * @see Disposable
 * @author Rahul Bagchi
 * @since 1.0
 */
public abstract class Transaction implements Disposable {

    private Backstack<Target> backstack = new Backstack<>();

    private ArrayDeque<Runnable> operations = new ArrayDeque<>();

    private boolean disposed = false;

    /**
     * Enqueues an operation to be executed later as part of the transaction.
     *
     * @param operation the operation to be enqueued
     */
    protected void enqueueOperation(Runnable operation) {
        operations.add(operation);
    }

    /**
     * Returns {@link Queue} of {@link Runnable} of operations
     *
     * @return the queue of operations to be executed.
     */
    protected Queue<Runnable> getOperationsQueue() {
        return operations;
    }

    /**
     * Executes all pending operations in the queue. This method will run all operations
     * sequentially until the queue is empty.
     *
     * @return {@code true} if any operations were executed, {@code false} otherwise
     */
    protected boolean executePendingOperations() {
        if (operations.isEmpty()) {
            return false;
        }
        while (!operations.isEmpty()) {
            operations.pop().run();
        }
        return true;
    }

    /**
     * Returns {@link Backstack} of {@link Target}s handled by this transaction
     *
     * @return the backstack that holds the {@link Target} objects managed by this transaction.
     */
    public Backstack<Target> getBackstack() {
        return backstack;
    }

    /**
     * Forcefully shows the specified {@link Target}. This method should be implemented by
     * subclasses to define the specific behavior for showing a target.
     *
     * @param target the target to be shown
     */
    public abstract void doForcedShow(Target target);

    /**
     * Forcefully hides the specified {@link Target}. This method should be implemented by
     * subclasses to define the specific behavior for hiding a target.
     *
     * @param target the target to be hidden
     */
    public abstract void doForcedHide(Target target);

    /**
     * Forcefully destroys the specified {@link Target}. This method should be implemented by
     * subclasses to define the specific behavior for destroying a target.
     *
     * @param target the target to be destroyed
     */
    public abstract void doForcedDestroy(Target target);

    /** {@inheritDoc} */
    @Override
    public void dispose() {
        if (disposed) {
            // it's already disposed
            return;
        }
        operations.clear();
        operations = null;
        backstack.forEach(Target::onDestroy);
        backstack.dispose();
        backstack = null;
        disposed = true;
    }

    /*****************************************************************
     *                         Sub Classes                           *
     *****************************************************************/

    /**
     * The {@code Target} class represents the state of a destination or screen within a {@link Transaction}.
     * It provides lifecycle hooks that can be overridden by subclasses to handle creation,
     * showing, hiding, and destruction events.
     *
     * <p>Each target is identified by a unique tag, which must be provided at construction.</p>
     */
    public abstract static class Target implements BackstackEntry {

        private final String tag;

        public Target(String tag) {
            if (StringUtil.isEmpty(tag)) {
                throw new IllegalArgumentException("tag can not be empty");
            }
            this.tag = tag;
        }

        /**
         * Returns unique tag associated with the {@link Target} in backstack
         *
         * @return the tag identifying this target. typically the {@code id}
         *          of the {@link rahulstech.jfx.routing.element.Destination Destination}
         *          it represents
         */
        public String getTag() {
            return tag;
        }

        /**
         * Called when the target is created. Subclasses can override this method
         * to perform creation logic.
         */
        public void onCreate() {}

        /**
         * Called before the target is shown. Subclasses can override this method
         * to perform any necessary preparation before showing.
         */
        public void onBeforeShow() {}

        /**
         * Called when the target is shown. Subclasses can override this method
         * to perform actions when the target becomes visible.
         */
        public void onShow() {}

        /**
         * Called when the target is hidden. Subclasses can override this method
         * to perform actions when the target is no longer visible.
         */
        public void onHide() {}

        /**
         * Called when the target is destroyed. Subclasses can override this method
         * to release resources associated with the target.
         */
        public void onDestroy() {}

        /** {@inheritDoc} */
        @Override
        public void dispose() {}
    }
}
