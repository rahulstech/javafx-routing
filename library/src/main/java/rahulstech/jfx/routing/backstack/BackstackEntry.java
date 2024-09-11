package rahulstech.jfx.routing.backstack;

import rahulstech.jfx.routing.util.Disposable;

/**
 * Represents a single entry in {@link  Backstack}
 *
 * @see Disposable
 * @author Rahul Bagchi
 * @since 1.0
 */
public interface BackstackEntry extends Disposable {

    /** {@inheritDoc} */
    @Override
    default void dispose() {}
}
