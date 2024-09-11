package rahulstech.jfx.routing.backstack;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BackstackTest {

    Backstack<DummyEntry> backstack;
    List<DummyEntry> entries;
    @BeforeEach
    void start() {
        backstack = new Backstack<>();
        entries = backstack.backstack;
        entries.add(0,new DummyEntry(1));
        entries.add(0,new DummyEntry(2));
        entries.add(0,new DummyEntry(3));
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
    public void testPop() {
        DummyEntry entry = backstack.popBackstackEntry();
        assertEquals(3,entry.getValue(),"incorrect entry returned");
        assertEquals(2,entries.size(),"backstack size did not reduce after pop");
    }

    @Test
    public void testPopIf() {
        Optional<DummyEntry> entry = backstack.popBackstackEntryIf(e->e.getValue()==2);
        assertNotNull(entry,"popBackstackEntryIf returned null optional");
        assertTrue(entry.isPresent(),"existing entry not popped");
        assertEquals(2,entry.get().getValue(),"incorrect entry returned");
        assertEquals(2,entries.size(),"backstack size did not reduce after pop");
    }

    @Test
    public void testPopIfNonExisting() {
        Optional<DummyEntry> entry = backstack.popBackstackEntryIf(e->e.getValue()==4);
        assertNotNull(entry,"popBackstackEntryIf returned null optional");
        assertFalse(entry.isPresent(),"non existing entry popped");
        assertEquals(3,entries.size(),"backstack size reduced after unsuccessful pop");
    }


    @Test
    public void testPeek() {
        DummyEntry entry = backstack.peekBackstackEntry();
        assertEquals(3,entry.getValue(),"incorrect entry returned");
        assertEquals(3,entries.size(),"backstack size changed after peek");
    }

    @Test
    public void testPeekFromEmptyBackstack() {
        assertThrows(NoSuchElementException.class,()->new Backstack<>().peekBackstackEntry(),"peeking from empty backstack does not throw");
    }

    @Test
    public void testPopFromEmptyBackstack() {
        assertThrows(NoSuchElementException.class,()->new Backstack<>().popBackstackEntry(),"popping from empty backstck does not throw");
    }

    @Test
    public void testBringToTopNewEntry() {
        int oldSize = backstack.size();
        DummyEntry entry = new DummyEntry(3);
        backstack.bringToTop(entry);
        int newSize = backstack.size();
        assertEquals(oldSize+1,newSize,"entry does not exits but no new entry added");
    }

    @Test
    public void testBringToTopExistingEntry() {
        int oldSize = backstack.size();
        DummyEntry entry = entries.stream().filter(e->e.getValue()==2).findFirst().get();
        backstack.bringToTop(entry);
        int newSize = backstack.size();
        assertEquals(oldSize,newSize,"entry exists but new entry added");
    }

    @Test
    void testPopUpToNonInclusive() {
        List<DummyEntry> popentries = backstack.popBackstackEntriesUpTo(entry->entry.getValue()==1,false);
        assertEquals(2,popentries.size(),"correct no of entries not popped");
        assertTrue(()->{
            for (DummyEntry e : popentries) {
                if (e.getValue()==1) {
                    return false;
                }
            }
            return true;
        },"non-inclusive entry popped");
    }

    static class DummyEntry implements BackstackEntry {

        final int value;

        public DummyEntry(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}