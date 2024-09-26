package rahulstech.jfx.routing.backstack;

import java.util.List;

/**
 * A simple implementation of the {@link BackstackCallback} interface with empty method bodies.
 *
 * @param <E> the type of {@link BackstackEntry} that this callback handles.
 *            Must extend the {@link BackstackEntry} class.
 * @since 2.0
 * @author Rahul Bagchi
 */
public class SimpleBackstackCallback<E extends BackstackEntry> implements BackstackCallback<E> {

    /**
     * Creates a new instance of {@code SimpleBackstackCallback}
     */
    public SimpleBackstackCallback() {}

    /** {@inheritDoc} */
    @Override
    public void onBackstackTopChanged(Backstack<E> backstack, E entry) {}

    /** {@inheritDoc} */
    @Override
    public void onPushedMultiple(Backstack<E> backstack, List<E> entries) {}

    /** {@inheritDoc} */
    @Override
    public void onPoppedMultiple(Backstack<E> backstack, List<E> entries) {}

    /** {@inheritDoc} */
    @Override
    public void onPoppedSingle(Backstack<E> backstack, E entry) {}
}
