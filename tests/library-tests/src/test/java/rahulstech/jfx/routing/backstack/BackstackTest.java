package rahulstech.jfx.routing.backstack;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BackstackTest {

    Backstack<DummyEntry> backstack;
    ArrayList<DummyEntry> entries;
    @BeforeEach
    void start() {
        backstack = new Backstack<>();
        entries = backstack.backstack;
        entries.add(new DummyEntry(1));
        entries.add(new DummyEntry(2));
        entries.add(new DummyEntry(3));
    }

    @AfterEach
    void end() {
        if (null != entries) {
            entries.clear();
        }
    }

    @Test
    public void testPushNullEntry() {
        assertThrows(NullPointerException.class,()->backstack.pushBackstackEntry(null),"pushed null entry without exception");
    }

    @Test
    public void testPopAt() {
        DummyEntry entry = backstack.popBackstackEntry(1);

        assertNotNull(entry,"entry at index from top returns null");
        assertEquals(2,entry.getValue(),"incorrect entry returned");
        assertEquals(2,backstack.backstack.size(),"backstack size did not reduce after pop");
    }

    @Test
    public void testPeekAt() {
        DummyEntry entry = backstack.peekBackstackEntry(1);

        assertNotNull(entry,"entry at index from top returns null");
        assertEquals(2,entry.getValue(),"incorrect entry returned");
        assertEquals(3,backstack.backstack.size(),"backstack size changed after peek");
    }

    @Test
    public void testBrintToTopNewEntry() {
        int oldSize = backstack.size();
        DummyEntry entry = new DummyEntry(3);
        backstack.bringToTop(entry);
        int newSize = backstack.size();
        assertEquals(oldSize+1,newSize,"entry does not exits but no new entry added");
    }

    @Test
    public void testBrintToTopExistingEntry() {
        int oldSize = backstack.size();
        DummyEntry entry = entries.stream().filter(e->e.getValue()==2).findFirst().get();
        backstack.bringToTop(entry);
        int newSize = backstack.size();
        assertEquals(oldSize,newSize,"entry exists but new entry added");
    }

    static class DummyEntry implements BackstackEntry {

        final int value;

        public DummyEntry(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public void dispose() {}
    }
}