package rahulstech.jfx.routing.backstack;

import rahulstech.jfx.routing.util.Disposable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 
 * @param <E>
 */
@SuppressWarnings("unused")
public class Backstack<E extends BackstackEntry> implements Disposable {

    private ArrayList<E> backstack = new ArrayList<>();

    public Backstack() {}

    public void pushBackstackEntry(E entry) {
        if (null==entry) {
            throw new NullPointerException("can not add null entry to backstack");
        }
        backstack.add(entry);
    }

    public E peekBackstackEntry() {
        return peekBackstackEntry(0);
    }

    public E popBackstackEntry() {
        return popBackstackEntry(0);
    }

    public E peekBackstackEntry(int indexFromTop) {
        int size = size();
        int index = size-1-indexFromTop;
        if (index<0 || index>=size) {
            throw new IndexOutOfBoundsException("popping entry at "+indexFromTop+" from a stack of size "+size);
        }
        return backstack.get(index);
    }

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

    public boolean isEmpty() {
        return size()==0;
    }

    public int size() {
        return null==backstack ? 0 : backstack.size();
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean remove(E entry) {
        return backstack.remove(entry);
    }

    public void clear() {
        backstack.clear();
    }

    public void forEach(Consumer<E> consumer) {
        int size = size();
        for (int i=size-1; i>=0; i--) {
            E entry = backstack.get(i);
            consumer.accept(entry);
        }
    }
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
}
