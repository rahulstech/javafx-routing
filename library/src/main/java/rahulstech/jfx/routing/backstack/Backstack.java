package rahulstech.jfx.routing.backstack;

import javafx.application.Platform;
import rahulstech.jfx.routing.util.Disposable;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Backstack is a {@link List} based backstack with some special features.
 * Backstack let you peek and pop item from any index but item is always pushed
 * at the top of backstack. Index "0" or the first element in the backstack
 * is the last element added or the top element. There is another utility
 * method popBackstackUpTo which let you pop items from top until a particular
 * condition is met. if the condition is never met then no item is popped.
 * Backstack accepts only {@link BackstackEntry}.
 *
 * @param <E> type of backstack entry
 * @see BackstackEntry
 *
 * @author Rahul Bagchi
 * @since 1.0
 */
public class Backstack<E extends BackstackEntry> implements Disposable {

    List<E> backstack = new LinkedList<>();

    List<BackstackCallback<E>> callbacks = new LinkedList<>();

    private final WrappedBackstackCallback wrappedCallback = new WrappedBackstackCallback();

    /**
     * Creates new {@code Backstack} instance
     */
    public Backstack() {}

    /**
     * Registers a {@link BackstackCallback} to this {@code Backstack}
     *
     * @param callback the {@code BackstackCallback}
     * @since 2.0
     */
    public void registerBackstackCallback(BackstackCallback<E> callback) {
        callbacks.add(new WeakBackstackCallback(callback));
    }

    /**
     * Unregisters a {@link BackstackCallback} from this {@code Backstack}
     *
     * @param callback the {@code BackstackCallback}
     * @since 2.0
     */
    public void unregisterBackstackCallback(BackstackCallback<E> callback) {
        callbacks.remove(callback);
    }

    /**
     * Add a new entry to the top
     *
     * @param entry the new entry to add
     * @throws NullPointerException if entry is null
     */
    public void pushBackstackEntry(E entry) {
        if (null==entry) {
            throw new NullPointerException("can not add null entry to backstack");
        }
        backstack.add(0,entry);
        wrappedCallback.onBackstackTopChanged(this,entry);
    }

    /**
     * First removes the entry if exists then adds to the top.
     *
     * @param entry existing or new entry to add to the top
     * @throws NullPointerException if entry is null
     */
    public void bringToTop(E entry) {
        if (null==entry) {
            throw new NullPointerException("can not add null entry to backstack");
        }
        E top = peekBackstackEntry();
        backstack.remove(entry);
        backstack.add(0,entry);
        if (top!=entry) {
            wrappedCallback.onBackstackTopChanged(this,entry);
        }
    }

    /**
     * Returns top entry in backstack without removing
     *
     * @return get but don't remove the top entry
     * @throws NoSuchElementException if backstack is empty
     */
    public E peekBackstackEntry() {
        if (isEmpty()) {
            throw new NoSuchElementException("can not peek from empty backstack");
        }
        return backstack.get(0);
    }

    /**
     * Removes and returns the top entry of the backstack
     *
     * @return get and remove the top entry
     * @throws NoSuchElementException if backstack is empty
     */
    public E popBackstackEntry() {
        if (isEmpty()) {
            throw new NoSuchElementException("can not pop from empty backstack");
        }
        E entry = backstack.remove(0);
        wrappedCallback.onPoppedSingle(this,entry);
        if (!isEmpty()) {
            wrappedCallback.onBackstackTopChanged(this,peekBackstackEntry());
        }
        return entry;
    }


    /**
     * Pops the first entry that passes {@link Predicate#test(Object)}
     *
     * @param check non-null {@code Predicate} instance to run test for each entry
     * @return non-null {@link Optional} with popped entry or and empty Optional if nothing popped
     * @throws NullPointerException if {@code check} is null
     * @since 2.0
     */
    public Optional<E> popBackstackEntryIf(Predicate<E> check) {
        if (null==check) {
            throw new NullPointerException("null Predicate provided");
        }
        if (isEmpty()) {
            return Optional.empty();
        }
        final E top = peekBackstackEntry();
        Iterator<E> it = backstack.iterator();
        E entry = null;
        while (it.hasNext()) {
            E e = it.next();
            if (check.test(e)) {
                it.remove();
                entry = e;
                break;
            }
        }
        if (null==entry) {
            return Optional.empty();
        }
        wrappedCallback.onPoppedSingle(this,entry);
        if (!isEmpty()) {
            final E newTop = peekBackstackEntry();
            if (top != newTop) {
                wrappedCallback.onBackstackTopChanged(this, newTop);
            }
        }
        return Optional.of(entry);
    }

    /**
     * Returns entry at the index from top without removing
     *
     * @param indexFromTop the index of the target entry from top
     * @return get but don't remove at the index from the top
     * @throws IndexOutOfBoundsException if invalid index provided
     * @deprecated since 2.0.0
     */
    @Deprecated
    public E peekBackstackEntry(int indexFromTop) {
        int size = size();
        int index = size-1-indexFromTop;
        if (index<0 || index>=size) {
            throw new IndexOutOfBoundsException("popping entry at "+indexFromTop+" from a stack of size "+size);
        }
        return backstack.get(index);
    }

    /**
     * Pop the backstack entries up to a certain condition is met. Target entry
     * i.e. the entry for which the check test is true may or may not be popped.
     * Use parameter inclusive to decide popping the target entry.
     *
     * @param check {@link Predicate} to test the target entry
     * @param inclusive if {@literal true} then target entry is popped, if {@literal false} target entry is not popped
     * @return non-null {@link List} of popped entries
     */
    public List<E> popBackstackEntriesUpTo(Predicate<E> check, boolean inclusive) {
        if (isEmpty()) {
            return Collections.emptyList();
        }
        final E top = peekBackstackEntry();
        ArrayList<E> popentries = new ArrayList<>();
        boolean found = false;
        ListIterator<E> it = backstack.listIterator();
        while (it.hasNext()) {
            E e = it.next();
            popentries.add(e);
            it.remove();
            if (check.test(e)) {
                found = true;
                if (!inclusive) {
                    popentries.remove(popentries.size()-1);
                    it.add(e);
                }
                break;
            }
        }
        if (!found) {
            backstack.addAll(0,popentries);
            popentries.clear();
            return Collections.emptyList();
        }
        wrappedCallback.onPoppedMultiple(this,Collections.unmodifiableList(popentries));
        if (!isEmpty()) {
            final E newTop = peekBackstackEntry();
            if (top != newTop) {
                wrappedCallback.onBackstackTopChanged(this, newTop);
            }
        }
        return popentries;
    }

    /**
     * Removes and returns entry at given index from top
     *
     * @param indexFromTop the index of the target entry from the top
     * @return get and remove the entry at the index from the top
     * @throws IndexOutOfBoundsException if invalid index provided
     * @deprecated since 2.0.0
     */
    @Deprecated
    public E popBackstackEntry(int indexFromTop) {
        int size = size();
        int index = size-1-indexFromTop;
        if (index<0 || index>=size) {
            throw new IndexOutOfBoundsException("popping entry at "+indexFromTop+" from a stack of size "+size);
        }
        E entry = backstack.remove(index);
        if (isEmpty()) {
            backstack = null;
        }
        return entry;
    }

    /**
     * Removes the entry
     *
     * @param entry the entry to remove
     */
    public void remove(E entry) {
        if (backstack.remove(entry)) {
            wrappedCallback.onPoppedSingle(this,entry);
        }
    }

    /**
     * Checks if backstack is empty
     *
     * @return {@code true} means backstack contains no entry,
     *          {@code false} otherwise
     */
    public boolean isEmpty() {
        return size()==0;
    }

    /**
     * Returns current no. of entries in backstack
     *
     * @return no. of entities in the backstack
     */
    public int size() {
        return backstack.size();
    }

    /**
     * Removes all entries form backstack; but does not dispose
     * entries
     */
    public void clear() {
        backstack.clear();
    }

    /**
     * Applies the given consumer action to each element in the backstack,
     * starting from the last element and moving towards the first.
     *
     * @param consumer the action to be performed on each element
     * @throws NullPointerException if {@code consumer} is null
     */
    public void forEach(Consumer<E> consumer) {
        if (null==consumer) {
            throw new NullPointerException("null Consumer provided");
        }
        backstack.forEach(consumer);
    }

    /**
     * Finds the first element in the backstack that matches the given predicate.
     * The search is performed from the end of the backstack towards the beginning.
     *
     * @param predicate the condition to match elements against
     * @return an {@link Optional} containing the first element that matches the
     *         predicate, or an empty {@link Optional} if no match is found
     * @throws NullPointerException if {@code predicated} parameter is null
     */
    public Optional<E> findFirst(Predicate<E> predicate) {
        if (null==predicate) {
            throw new NullPointerException("null  Predicate provided");
        }
        for (E e : backstack) {
            if (predicate.test(e)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    /**
     * use dispose when this backstack will not be used further.
     * this method also disposes each disposable entry. therefore
     * user of this class no need to explicitly call dispose on entries.
     */
    @Override
    public void dispose() {
        if (null==backstack) {
            // it's already disposed
            return;
        }
        callbacks.clear();
        forEach(Disposable::dispose);
        clear();
        backstack = null;
        callbacks = null;
    }

    @Override
    public String toString() {
        return null==backstack ? "[]" : backstack.toString();
    }

    private class WrappedBackstackCallback implements  BackstackCallback<E> {

        @Override
        public void onBackstackTopChanged(Backstack<E> backstack, E entry) {
            Platform.runLater(() -> {
                for (BackstackCallback<E> callback : callbacks) {
                    callback.onBackstackTopChanged(backstack,entry);
                }
            });

        }

        @Override
        public void onPushedMultiple(Backstack<E> backstack, List<E> entries) {
            Platform.runLater(() -> {
                for (BackstackCallback<E> callback : callbacks) {
                    callback.onPushedMultiple(backstack,entries);
                }
            });
        }

        @Override
        public void onPoppedMultiple(Backstack<E> backstack, List<E> entries) {
            Platform.runLater(() -> {
                for (BackstackCallback<E> callback : callbacks) {
                    callback.onPoppedMultiple(backstack,entries);
                }
            });
        }

        @Override
        public void onPoppedSingle(Backstack<E> backstack, E entry) {
            Platform.runLater(() -> {
                for (BackstackCallback<E> callback : callbacks) {
                    callback.onPoppedSingle(backstack,entry);
                }
            });
        }
    }

    private class WeakBackstackCallback implements BackstackCallback<E> {

        final WeakReference<BackstackCallback<E>> wrapped;

        WeakBackstackCallback(BackstackCallback<E> callback) {
            wrapped = new WeakReference<>(callback);
        }

        public BackstackCallback<E> get() {
            return wrapped.get();
        }

        @Override
        public void onBackstackTopChanged(Backstack<E> backstack, E entry) {
            BackstackCallback<E> callback = get();
            if (null != callback) {
                callback.onBackstackTopChanged(backstack,entry);
            }
        }

        @Override
        public void onPushedMultiple(Backstack<E> backstack, List<E> entries) {
            BackstackCallback<E> callback = get();
            if (null != callback) {
                callback.onPushedMultiple(backstack,entries);
            }
        }

        @Override
        public void onPoppedMultiple(Backstack<E> backstack, List<E> entries) {
            BackstackCallback<E> callback = get();
            if (null != callback) {
                callback.onPoppedMultiple(backstack,entries);
            }
        }

        @Override
        public void onPoppedSingle(Backstack<E> backstack, E entry) {
            BackstackCallback<E> callback = get();
            if (null != callback) {
                callback.onPoppedSingle(backstack,entry);
            }
        }

        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        @Override
        public boolean equals(Object obj) {
            BackstackCallback<E> org = get();
            return Objects.equals(org,obj);
        }

        @Override
        public int hashCode() {
            BackstackCallback<E> org = get();
            if (null==org) {
                return 0;
            }
            return org.hashCode();
        }
    }
}
