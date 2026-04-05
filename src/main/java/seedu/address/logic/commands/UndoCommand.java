package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Undoes the most recent successful undoable app change.
 */
public class UndoCommand extends Command {
    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Reverts the most recent successful undoable command\n"
            + "(for e.g: add, edit, delete, clear, or shortcut set/remove).\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Undid the last change.";
    public static final String MESSAGE_FAILURE = "There is no change to undo.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.canUndoState()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        model.undoState();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
