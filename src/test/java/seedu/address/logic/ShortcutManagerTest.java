package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandDatabase;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class ShortcutManagerTest {
    private ShortcutManager shortcutManager;
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        shortcutManager = new ShortcutManager(model, new CommandDatabase());
    }

    @Test
    public void expandShortcut_replacesFirstTokenOnly() throws ParseException {
        shortcutManager.addShortcut("a", "add");
        assertEquals("add n/a", shortcutManager.expandShortcut("a n/a"));
    }

    @Test
    public void expandShortcut_noShortcut_returnsOriginalCommand() {
        assertEquals("list", shortcutManager.expandShortcut("list"));
        assertEquals("Add n/A", shortcutManager.expandShortcut("Add n/A"));
    }

    @Test
    public void addShortcut_reservedAlias_throwsParseException() {
        assertThrows(ParseException.class,
                ShortcutManager.MESSAGE_RESERVED_ALIAS, () ->
                shortcutManager.addShortcut("add", "list"));
    }

    @Test
    public void addShortcut_invalidTarget_throwsParseException() {
        assertThrows(ParseException.class,
                ShortcutManager.MESSAGE_INVALID_COMMAND_WORD, () ->
                shortcutManager.addShortcut("a", "unknown"));
    }

    @Test
    public void removeShortcut_missingAlias_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(ShortcutManager.MESSAGE_ALIAS_NOT_FOUND, "a"), () ->
                shortcutManager.removeShortcut("a"));
    }

    @Test
    public void formatShortcutList_success() throws ParseException {
        shortcutManager.addShortcut("a", "add");
        shortcutManager.addShortcut("e", "edit");
        shortcutManager.addShortcut("a1", "add");
        assertEquals(Map.of("a", "add", "e", "edit", "a1", "add"), model.getShortcutMap().getShortcutMappings());
        assertEquals("a -> add\n" + "a1 -> add\n" + "e -> edit", shortcutManager.formatShortcutList());
    }
}
