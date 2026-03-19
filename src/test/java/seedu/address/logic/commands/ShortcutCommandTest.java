package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.ShortcutManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ShortcutCommandTest {

    @Test
    public void execute_setShortcut_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();
        expectedModel.setShortcut("a", "add");

        ShortcutCommand command = new ShortcutCommand(ShortcutCommand.Action.SET, "a", "add");
        assertCommandSuccess(command, model,
                String.format(ShortcutCommand.MESSAGE_SET_SUCCESS, "a", "add"), expectedModel);
    }

    @Test
    public void execute_removeShortcut_success() {
        Model model = new ModelManager();
        model.setShortcut("a", "add");
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(model.getUserPrefs()));
        expectedModel.removeShortcut("a");

        ShortcutCommand command = new ShortcutCommand(ShortcutCommand.Action.REMOVE, "a", null);
        assertCommandSuccess(command, model,
                String.format(ShortcutCommand.MESSAGE_REMOVE_SUCCESS, "a"), expectedModel);
    }

    @Test
    public void execute_listShortcut_success() {
        Model model = new ModelManager();
        model.setShortcut("a", "add");
        model.setShortcut("e", "edit");
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(model.getUserPrefs()));

        ShortcutCommand command = new ShortcutCommand(ShortcutCommand.Action.LIST, null, null);
        assertCommandSuccess(command, model,
                "a -> add\n" + "e -> edit", expectedModel);
    }

    @Test
    public void execute_setDuplicateShortcut_throwsCommandException() {
        Model model = new ModelManager();
        model.setShortcut("a", "add");

        ShortcutCommand command = new ShortcutCommand(ShortcutCommand.Action.SET, "a", "edit");
        assertCommandFailure(command, model, String.format(ShortcutManager.MESSAGE_ALIAS_EXISTS, "a"));
    }

    @Test
    public void execute_setUnknownCommand_throwsCommandException() {
        Model model = new ModelManager();

        ShortcutCommand command = new ShortcutCommand(ShortcutCommand.Action.SET, "a", "unknown_command");
        assertCommandFailure(command, model, ShortcutManager.MESSAGE_INVALID_COMMAND_WORD);
    }
}
