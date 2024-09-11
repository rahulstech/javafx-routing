package rahulstech.jfx.routing.util;

/**
 * A convenient way to deallocate resources when the implemented class
 * is in a Disposable hierarchy. All the parent class must call the
 * {@link #dispose()} methods of all the {@link Disposable} member objects.
 */
public interface Disposable {

    /**
     * Deallocate resources, close open streams and clear collections etc.
     * Note that this method must not throw any exception. dispose() may be
     * called more than one due to its hierarchy, but you must check first
     * if it's already deallocated or not. Many Disposables in this library
     * usage a boolean flag disposed to check before de-allocation.
     */
    void dispose();
}
