package rahulstech.jfx.routing.backstack;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BackstackCallbackTest {

    @Test
    void onBackstackTopChanged() {
        Backstack<Entry> backstack = new Backstack<>();

        Entry target = new Entry();

        Object[] results = new Object[2];

        backstack.registerBackstackCallback(new SimpleBackstackCallback<>(){
            @Override
            public void onBackstackTopChanged(Backstack<Entry> backstack, Entry entry) {
                results[0] = true;
                results[1] = entry;
            }
        });

        backstack.pushBackstackEntry(target);

        assertTrue((boolean) results[0],"callback onBackstackTopChanged not called");
        assertEquals(target, results[1],"incorrect entry pushed");
    }

    @Test
    void onPoppedMultiple() {

        Backstack<Entry> backstack = new Backstack<>();

        Entry target = new Entry();

        backstack.backstack.addAll(Arrays.asList(new Entry(),target));

        boolean[] triggered = new boolean[1];

        backstack.registerBackstackCallback(new SimpleBackstackCallback<>(){
            @Override
            public void onPoppedMultiple(Backstack<Entry> backstack, List<Entry> entries) {
                triggered[0] = true;
            }
        });

        backstack.popBackstackEntriesUpTo(entry -> entry==target,true);

        assertTrue(triggered[0]);
    }

    @Test
    void onPoppedSingle() {
        Backstack<Entry> backstack = new Backstack<>();

        Entry target = new Entry();
        backstack.backstack.add(target);

        Object[] results = new Object[2];

        backstack.registerBackstackCallback(new SimpleBackstackCallback<>(){
            @Override
            public void onPoppedSingle(Backstack<Entry> backstack, Entry entry) {
                results[0] = true;
                results[1] = entry;
            }
        });

        backstack.popBackstackEntry();

        assertTrue((boolean) results[0], "callback onPoppedSingle not called");
        assertEquals(target,results[1],"incorrect entry popped");
    }

    @Test
    void onPoppedSingle_remove() {
        Backstack<Entry> backstack = new Backstack<>();

        backstack.backstack.add(new Entry());

        Object[] results = new Object[1];

        backstack.registerBackstackCallback(new SimpleBackstackCallback<>(){
            @Override
            public void onPoppedSingle(Backstack<Entry> backstack, Entry entry) {
                results[0] = true;
            }
        });

        backstack.remove(new Entry());

        assertFalse((boolean) results[0], "callback onPoppedSingle called for non existing entry remove");
    }

    static class Entry implements BackstackEntry {}
}