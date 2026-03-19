package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
