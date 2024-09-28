package rahulstech.jfx.routing.backstack;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.util.WaitForAsyncUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
public class BackstackCallbackTest {

    public static class DummyEntry implements BackstackEntry {}

    Backstack<DummyEntry> backstack;

    static DummyEntry entry1 = new DummyEntry();

    static DummyEntry entry2 = new DummyEntry();

    static DummyEntry entry3 = new DummyEntry();

    @BeforeEach
    void setup() {
        backstack = new Backstack<>();
        backstack.backstack.add(entry1); // backstack top
        backstack.backstack.add(entry2);
        backstack.backstack.add(entry3);
    }

    @AfterEach
    void teardown() {
        backstack.backstack.clear();
        backstack = null;
    }

    @ParameterizedTest
    @ArgumentsSource(OnBackstackTopChangesTestArguments.class)
    void onBackstackTopChanged(String name, Consumer<Backstack<DummyEntry>> consumer, boolean expectedCall, DummyEntry expectedEntry) {

        final boolean[] calls = new boolean[1];

        final DummyEntry[] entries = new DummyEntry[1];

        backstack.registerBackstackCallback(new SimpleBackstackCallback<>() {
            @Override
            public void onBackstackTopChanged(Backstack<DummyEntry> backstack, DummyEntry entry) {
                calls[0] = true;
                entries[0] = entry;
            }
        });

        consumer.accept(backstack);

        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(expectedCall, calls[0],name+": call");

        assertEquals(expectedEntry,entries[0],name+": entry");
    }

    static class OnBackstackTopChangesTestArguments implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {

            DummyEntry newEntry = new DummyEntry();

            //noinspection Convert2MethodRef
            return Stream.of(
                    Arguments.arguments("push backstack",
                            (Consumer<Backstack<DummyEntry>>)backstack -> backstack.pushBackstackEntry(newEntry), true, newEntry),

                    Arguments.arguments("pop backstack",
                            (Consumer<Backstack<DummyEntry>>)backstack -> backstack.popBackstackEntry(), true, entry2),

                    Arguments.arguments("bring to top existing but non-top entry",
                            (Consumer<Backstack<DummyEntry>>)backstack -> backstack.bringToTop(entry2), true, entry2),

                    Arguments.arguments("bring to top non-existing entry",
                            (Consumer<Backstack<DummyEntry>>)backstack -> backstack.bringToTop(newEntry), true, newEntry),

                    Arguments.arguments("bring to top existing top entry",
                            (Consumer<Backstack<DummyEntry>>)backstack -> backstack.bringToTop(entry1), false, null),

                    Arguments.arguments("pop if matched top entry",
                            (Consumer<Backstack<DummyEntry>>)backstack -> backstack.popBackstackEntryIf(e -> e == entry1), true, entry2),

                    Arguments.arguments("pop if matched non top entry",
                            (Consumer<Backstack<DummyEntry>>)backstack -> backstack.popBackstackEntryIf(e -> e == entry3), false, null),

                    Arguments.arguments("pop if unmatched",
                            (Consumer<Backstack<DummyEntry>>)backstack -> backstack.popBackstackEntryIf(e -> e == newEntry), false, null),

                    Arguments.arguments("pop upto inclusive matched",
                            (Consumer<Backstack<DummyEntry>>)backstack -> backstack.popBackstackEntriesUpTo(e -> e == entry2,true), true, entry3),

                    Arguments.arguments("pop upto non-inclusive matched",
                            (Consumer<Backstack<DummyEntry>>)backstack -> backstack.popBackstackEntriesUpTo(e -> e == entry2,false), true, entry2),

                    Arguments.arguments("pop upto emptified",
                            (Consumer<Backstack<DummyEntry>>)backstack -> backstack.popBackstackEntriesUpTo(e -> e == entry3,true), false, null),

                    Arguments.arguments("pop upto unmatched",
                            (Consumer<Backstack<DummyEntry>>)backstack -> backstack.popBackstackEntriesUpTo(e -> e == newEntry,true), false, null)

            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(OnPoppedSingleTestArguments.class)
    void onPopppedSingle(String name, Consumer<Backstack<DummyEntry>> consumer, DummyEntry expectedEntry, boolean expectedCall) {

        final DummyEntry[] entries = new DummyEntry[1];

        final boolean[] calls = new boolean[1];

        backstack.registerBackstackCallback(new SimpleBackstackCallback<>() {
            @Override
            public void onPoppedSingle(Backstack<DummyEntry> backstack, DummyEntry entry) {
                entries[0] = entry;
                calls[0] = true;
            }
        });

        consumer.accept(backstack);

        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(expectedCall,calls[0],name+": call");

        assertEquals(expectedEntry,entries[0],name+": entry");
    }

    static class OnPoppedSingleTestArguments implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            DummyEntry any = new DummyEntry();

            //noinspection Convert2MethodRef
            return Stream.of(
                    Arguments.arguments("pop backstack",
                            (Consumer<Backstack<DummyEntry>>)backstack -> backstack.popBackstackEntry(), entry1,true),

                    Arguments.arguments("remove existing entry",
                            (Consumer<Backstack<DummyEntry>>)backstack -> backstack.remove(entry3), entry3,true),

                    Arguments.arguments("remove non-existing entry",
                            (Consumer<Backstack<DummyEntry>>)backstack -> backstack.bringToTop(new DummyEntry()), null,false),

                    Arguments.arguments("pop if matched",
                            (Consumer<Backstack<DummyEntry>>)backstack -> backstack.popBackstackEntryIf(e -> e == entry3), entry3,true),

                    Arguments.arguments("pop if unmatched",
                            (Consumer<Backstack<DummyEntry>>)backstack -> backstack.popBackstackEntryIf(e -> e == any), null,false)
            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(OnPoppedMultipleTestArguments.class)
    void onPoppedMultiple(String name, Consumer<Backstack<DummyEntry>> consumer, List<DummyEntry> expectedEntries, boolean expectedCall) {

        final Object[] lists = new Object[1];

        final boolean[] calls = new boolean[1];

        backstack.registerBackstackCallback(new SimpleBackstackCallback<>() {

            @Override
            public void onPoppedMultiple(Backstack<DummyEntry> backstack, List<DummyEntry> entries) {
                lists[0] = entries;
                calls[0] = true;
            }
        });

        consumer.accept(backstack);

        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(expectedCall,calls[0],name+": call");

        assertEquals(expectedEntries,lists[0],name+": entries");
    }

    static class OnPoppedMultipleTestArguments implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            DummyEntry any = new DummyEntry();

            return Stream.of(
                    Arguments.arguments("pop upto inclusive",
                            (Consumer<Backstack<DummyEntry>>)backstack -> backstack.popBackstackEntriesUpTo(e -> e == entry2,true),
                            Arrays.asList(entry1,entry2),true),

                    Arguments.arguments("pop upto non-inclusive",
                            (Consumer<Backstack<DummyEntry>>)backstack -> backstack.popBackstackEntriesUpTo(e -> e == entry2,false),
                            Collections.singletonList(entry1),true),

                    Arguments.arguments("pop upto unknown",
                            (Consumer<Backstack<DummyEntry>>)backstack -> backstack.popBackstackEntriesUpTo(e -> e == any,true),
                            null,false)
            );
        }
    }
}
