package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandDatabaseTest {

    private CommandDatabase commandDatabase;

    @BeforeEach
    void setUp() {
        commandDatabase = new CommandDatabase();
    }

    // Test exact matches
    @Test
    void testExactMatch() {
        assertEquals("add", commandDatabase.completePrefix("add"));
        assertEquals("find", commandDatabase.completePrefix("find"));
        assertEquals("list", commandDatabase.completePrefix("list"));
    }

    // Test single prefix matches
    @Test
    void testSinglePrefixMatch() {
        assertEquals("add", commandDatabase.completePrefix("a"));
        assertEquals("clear", commandDatabase.completePrefix("cl"));
        assertEquals("delete", commandDatabase.completePrefix("del"));
        assertEquals("help", commandDatabase.completePrefix("he"));
        assertEquals("shortcut", commandDatabase.completePrefix("sh"));
        assertEquals("theme", commandDatabase.completePrefix("th"));
    }

    // Test multiple prefix matches (longest common prefix)
    // TODO: update when commands have longer common prefix
    @Test
    void testMultipleMatches() {
        assertEquals("e", commandDatabase.completePrefix("e"));
    }

    // Test no matches
    @Test
    void testNoMatch() {
        assertEquals("x", commandDatabase.completePrefix("x"));
        assertEquals("zzz", commandDatabase.completePrefix("zzz"));
    }

    // Test empty prefix (should match all commands)
    @Test
    void testEmptyPrefix() {
        assertEquals("", commandDatabase.completePrefix(""));
    }

    // Test full command as prefix
    @Test
    void testFullCommandPrefix() {
        assertEquals("exit", commandDatabase.completePrefix("exit"));
    }

    // Test longer than command
    @Test
    void testPrefixLongerThanCommand() {
        assertEquals("adding", commandDatabase.completePrefix("adding"));
    }

    // Test case sensitivity
    @Test
    void testCaseSensitivity() {
        assertEquals("add", commandDatabase.completePrefix("Add"));
    }

    @Test
    void getHelpOverview_containsCommandsAndFooter() {
        String helpOverview = commandDatabase.getHelpOverview();

        assertTrue(helpOverview.startsWith("Available commands:"));
        commandDatabase.getCommandWords().forEach(commandWord ->
                assertTrue(helpOverview.contains(commandWord + ":")));
        assertTrue(helpOverview.contains("help COMMAND_WORD"));
    }

    @Test
    void getDetailedHelp_knownCommand_returnsUsage() {
        assertEquals(AddCommand.MESSAGE_USAGE,
                commandDatabase.getDetailedHelp(AddCommand.COMMAND_WORD).orElseThrow());
    }

    @Test
    void getDetailedHelp_noteCommand_returnsUsage() {
        assertEquals(NoteCommand.MESSAGE_USAGE,
                commandDatabase.getDetailedHelp(NoteCommand.COMMAND_WORD).orElseThrow());
    }

    @Test
    void getDetailedHelp_unknownCommand_returnsEmpty() {
        assertTrue(commandDatabase.getDetailedHelp("unknown").isEmpty());
    }

    @Test
    void getDetailedHelp_null_returnsEmpty() {
        assertTrue(commandDatabase.getDetailedHelp(null).isEmpty());
    }

    @Test
    void isKnownCommand_note_returnsTrue() {
        assertTrue(commandDatabase.isKnownCommand("note"));
    }

    @Test
    void isKnownCommand_upperCaseNote_returnsTrue() {
        assertTrue(commandDatabase.isKnownCommand("NOTE"));
    }

    @Test
    void completePrefix_note_returnsNote() {
        assertEquals("note", commandDatabase.completePrefix("no"));
    }
}
