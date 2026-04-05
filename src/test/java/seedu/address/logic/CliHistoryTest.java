package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CliHistoryTest {
    private static final String EMPTY_STRING = "";

    @Test
    void emptyHistory_shouldHandlePreviousAndNext() {
        CliHistory history = new CliHistory();

        assertEquals(EMPTY_STRING, history.getPrevious());
        assertEquals(EMPTY_STRING, history.getNext());
    }

    @Test
    void emptyHistory_setPointer() {
        CliHistory history = new CliHistory();
        assertDoesNotThrow(history::setPointerMostRecent);

    }

    @Test
    void addInput_thenGetPreviousRepeatedly() {
        CliHistory history = new CliHistory();

        history.addInput("list");
        history.addInput("find");
        history.addInput("add n/John Doe");

        // Pointer starts invalid, so getPrevious() should initialize it to the back
        assertEquals("add n/John Doe", history.getPrevious());
        assertEquals("find", history.getPrevious());
        assertEquals("list", history.getPrevious());
    }

    @Test
    void getPrevious_shouldNotGoPastBeginning() {
        CliHistory history = new CliHistory();

        history.addInput("one");
        history.addInput("two");
        history.addInput("three");

        assertEquals("three", history.getPrevious());
        assertEquals("two", history.getPrevious());
        assertEquals("one", history.getPrevious());

        // Should stay at index 0 and keep returning the first entry
        assertEquals("one", history.getPrevious());
        assertEquals("one", history.getPrevious());
    }

    @Test
    void getNext_shouldMoveForwardAndStopAtEnd() {
        CliHistory history = new CliHistory();

        history.addInput("one");
        history.addInput("two");
        history.addInput("three");

        // Move pointer backward first
        assertEquals("three", history.getPrevious());
        assertEquals("two", history.getPrevious());
        assertEquals("one", history.getPrevious());

        // Then move forward
        assertEquals("two", history.getNext());
        assertEquals("three", history.getNext());

        // Should dereference pointer and keep returning empty
        assertEquals(EMPTY_STRING, history.getNext());
        assertEquals(EMPTY_STRING, history.getNext());
    }

    @Test
    void getNext_fromInvalidPointer_shouldBeEmpty() {
        CliHistory history = new CliHistory();

        history.addInput("alpha");
        history.addInput("beta");
        history.addInput("gamma");

        assertEquals(EMPTY_STRING, history.getNext());
        assertEquals(EMPTY_STRING, history.getNext());
    }

    @Test
    void singleElementHistory_getNextAndPrevious() {
        CliHistory history = new CliHistory();

        history.addInput("only");


        assertEquals(EMPTY_STRING, history.getNext());
        assertEquals(EMPTY_STRING, history.getNext());
        assertEquals("only", history.getPrevious());
        assertEquals("only", history.getPrevious());
        assertEquals(EMPTY_STRING, history.getNext());
        assertEquals(EMPTY_STRING, history.getNext());
    }

    @Test
    void alternatingPreviousAndNext_shouldTrackPointerCorrectly() {
        CliHistory history = new CliHistory();

        history.addInput("cmd1");
        history.addInput("cmd2");
        history.addInput("cmd3");

        assertEquals("cmd3", history.getPrevious());
        assertEquals("cmd2", history.getPrevious());
        assertEquals("cmd3", history.getNext());
        assertEquals("cmd2", history.getPrevious());
        assertEquals("cmd1", history.getPrevious());
        assertEquals("cmd2", history.getNext());
    }

    @Test
    void addInput_afterNavigation_shouldStillAllowNewestItemToBeReached() {
        CliHistory history = new CliHistory();

        history.addInput("a");
        history.addInput("b");

        assertEquals("b", history.getPrevious());

        history.addInput("c");
        // adding input derefs pointer, so should reset
        assertEquals("c", history.getPrevious());
        assertEquals("b", history.getPrevious());
    }

    @Test
    void addInput_thenResetPointer() {
        CliHistory history = new CliHistory();

        history.addInput("a");
        history.addInput("b");

        history.setPointerMostRecent();
        assertEquals("a", history.getPrevious());
        assertEquals("b", history.getNext());

        history.setPointerMostRecent();
        assertEquals("", history.getNext());
    }
}
