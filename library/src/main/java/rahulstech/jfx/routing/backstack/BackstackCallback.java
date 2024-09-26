package rahulstech.jfx.routing.backstack;

import java.util.List;

/**
 * This interface defines callback methods for handling changes in a {@link Backstack}.
 * Implementers of this interface will be notified when the top entry of the
 * {@link Backstack} changes, and when multiple entries are pushed or popped.
 *
 * @param <E> the type of {@link BackstackEntry} that this callback handles.
 *            Must extend the {@link BackstackEntry} class.
 */
public interface BackstackCallback<E extends BackstackEntry> {

    /**
     * Called when the top entry of the {@link Backstack} changes.
     * This method will be invoked whenever a new {@link BackstackEntry} becomes
     * the top entry, which could happen as a result of pushing or popping entries.
     *
     * @param backstack the affected {@link Backstack} instance.
     *                  Cannot be {@code null}.
     * @param entry     the new top entry of the {@link Backstack}.
     *                  Cannot be {@code null}.
     */
    void onBackstackTopChanged(Backstack<E> backstack, E entry);

    /**
     * Called when multiple entries are pushed onto the {@link Backstack}.
     * This method will be invoked after the entries are pushed, with the full list
     * of entries that were added.
     *
     * @param backstack the affected {@link Backstack} instance.
     *                  Cannot be {@code null}.
     * @param entries   the list of {@link BackstackEntry} instances that were pushed
     *                  onto the backstack.
     *                  Cannot be {@code null} or empty.
     */
    void onPushedMultiple(Backstack<E> backstack, List<E> entries);

    /**
     * Called when multiple entries are popped from the {@link Backstack}.
     * This method will be invoked after the entries are removed from the backstack,
     * with the full list of entries that were popped.
     *
     * @param backstack the affected {@link Backstack} instance.
     *                  Cannot be {@code null}.
     * @param entries   the list of {@link BackstackEntry} instances that were popped
     *                  from the backstack.
     *                  Cannot be {@code null} or empty.
     */
    void onPoppedMultiple(Backstack<E> backstack, List<E> entries);

    /**
     * Called when a single entry is popped from the {@link Backstack}.
     * This method will be invoked after a single entry is removed, and will provide
     * the entry that was popped.
     *
     * This could occur due to explicit user actions or programmatically,
     * such as when the {@link Backstack#popBackstackEntry()} or
     * {@link Backstack#remove(BackstackEntry)} methods are called.
     *
     * @param backstack the affected {@link Backstack} instance.
     *                  Cannot be {@code null}.
     * @param entry     the {@link BackstackEntry} that was removed.
     *                  Cannot be {@code null}.
     *
     * @see Backstack#popBackstackEntry()
     * @see Backstack#remove(BackstackEntry)
     */
    void onPoppedSingle(Backstack<E> backstack, E entry);
}
