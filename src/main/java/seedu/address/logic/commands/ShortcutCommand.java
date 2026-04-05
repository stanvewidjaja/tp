package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.logic.ShortcutManager;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;

/**
 * Manages user-defined command shortcuts.
 */
public class ShortcutCommand extends Command {
    public static final String COMMAND_WORD = "shortcut";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Manages command shortcuts.\n"
            + "Parameters: set ALIAS COMMAND_WORD | remove ALIAS | list\n"
            + "Example: " + COMMAND_WORD + " set r redo | "
            + COMMAND_WORD + " remove r | "
            + COMMAND_WORD + " list";

    public static final String MESSAGE_SET_SUCCESS = "Shortcut set: %1$s -> %2$s";
    public static final String MESSAGE_REMOVE_SUCCESS = "Shortcut removed: %1$s";

    private final Action action;
    private final String alias;
    private final String commandWord;

    /**
     * Creates a shortcut command.
     */
    public ShortcutCommand(Action action, String alias, String commandWord) {
        requireNonNull(action);
        this.action = action;
        this.alias = alias;
        this.commandWord = commandWord;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        ShortcutManager shortcutManager = new ShortcutManager(model, new CommandDatabase());

        try {
            switch (action) {
            case SET:
                shortcutManager.addShortcut(alias, commandWord);
                return new CommandResult(String.format(MESSAGE_SET_SUCCESS,
                        alias.trim().toLowerCase(), commandWord.trim().toLowerCase()));

            case REMOVE:
                shortcutManager.removeShortcut(alias);
                return new CommandResult(String.format(MESSAGE_REMOVE_SUCCESS, alias.trim().toLowerCase()));

            case LIST:
                return new CommandResult(shortcutManager.formatShortcutList());

            default:
                throw new AssertionError("Unknown shortcut action: " + action);
            }
        } catch (ParseException e) {
            throw new CommandException(e.getMessage(), e);
        }
    }

    @Override
    public boolean isStateMutating() {
        return action == Action.SET || action == Action.REMOVE;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ShortcutCommand)) {
            return false;
        }

        ShortcutCommand otherCommand = (ShortcutCommand) other;
        return action == otherCommand.action
                && Objects.equals(alias, otherCommand.alias)
                && Objects.equals(commandWord, otherCommand.commandWord);
    }

    /**
     * Supported shortcut actions.
     */
    public enum Action {
        SET,
        REMOVE,
        LIST
    }
}
