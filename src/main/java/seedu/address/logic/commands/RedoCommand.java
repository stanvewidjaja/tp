package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Redoes the most recent undone undoable app change.
 */
public class RedoCommand extends Command {
    public static final String COMMAND_WORD = "redo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Reapplies the most recent undone undoable command\n"
            + "(for e.g: add, edit, delete, clear, or shortcut set/remove).\n"
            + "Parameters: none\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Redid the last undone change.";
    public static final String MESSAGE_FAILURE = "There is no change to redo.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.canRedoState()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        model.redoState();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
