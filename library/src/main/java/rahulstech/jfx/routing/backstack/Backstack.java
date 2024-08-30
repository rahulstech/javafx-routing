package rahulstech.jfx.routing.backstack;

import rahulstech.jfx.routing.util.Disposable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Backstack is a {@link List} based backstack with some special features.
 * Backstack let you peek and pop item from any inde but item is always pushed
 * at the top of backstack. Index "0" or the first element in the backstack
 * is the last element added or the top element. There is another utility
 * method popBackstackUpTo which let you pop items from top untill a perticular
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

    private ArrayList<E> backstack = new ArrayList<>();

    public Backstack() {}

    /**
     * Add a new entry at the top
     *
     * @param entry the new entry to add
     * @throws NullPointerException if entry is null
     */
    public void pushBackstackEntry(E entry) {
        if (null==entry) {
            throw new NullPointerException("can not add null entry to backstack");
        }
        backstack.add(entry);
    }

    /**
     * @return get but don't remove the top entry
     */
    public E peekBackstackEntry() {
        return peekBackstackEntry(0);
    }

    /**
     * @return get and remove the top entry
     */
    public E popBackstackEntry() {
        return popBackstackEntry(0);
    }

    /**
     * @param indexFromTop the index of the target entry from top
     * @return get but don't remove at the index from the top
     * @throws IndexOutOfBoundsException if invalid index provided
     */
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
     * @param inclusive if {@literal true} then target entry is popped, if {@literal false} target entry is not poppped
     * @return non-null {@link List} of popped entries
     */
    public List<E> popBackstackEntriesUpTo(Predicate<E> check, boolean inclusive) {
        int size = size();
        int target = -1;
        for (int i=size-1; i>=0; i--) {
            E entry = backstack.get(i);
            if (check.test(entry)) {
                target = i;
                break;
            }
        }
        if (target==-1) {
            return Collections.emptyList();
        }
        ArrayList<E> popentries = new ArrayList<>();
        int start = inclusive ? target : target+1;
        for (int i=size-1; i>=start; i--) {
            popentries.add(popBackstackEntry());
        }
        return popentries;
    }

    /**
     * @param indexFromTop the index of the target entry from the top
     * @return get and remove the entry at the index from the top
     * @throws IndexOutOfBoundsException if invalid index provided
     */
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
        backstack.remove(entry);
    }

    /**
     * @return {@code true} menas backstack contains no entry,
     *          {@code false} otherwise
     */
    public boolean isEmpty() {
        return size()==0;
    }

    /**
     * @return no. of elntries in the backstack
     */
    public int size() {
        return null==backstack ? 0 : backstack.size();
    }

    /**
     * Removes all enetries form backstack; but does not dispose
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
     */
    public void forEach(Consumer<E> consumer) {
        int size = size();
        for (int i=size-1; i>=0; i--) {
            E entry = backstack.get(i);
            consumer.accept(entry);
        }
    }

    /**
     * Finds the first element in the backstack that matches the given predicate.
     * The search is performed from the end of the backstack towards the beginning.
     *
     * @param predicate the condition to match elements against
     * @return an {@link Optional} containing the first element that matches the
     *         predicate, or an empty {@link Optional} if no match is found
     */
    public Optional<E> findFirst(Predicate<E> predicate) {
        int size = size();
        for (int i=size-1; i>=0; i--) {
            E entry = backstack.get(i);
            if (predicate.test(entry)) {
                return Optional.of(entry);
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
        forEach(Disposable::dispose);
        clear();
        backstack = null;
    }

    @Override
    public String toString() {
        return null==backstack ? "[]" : backstack.toString();
    }
}
