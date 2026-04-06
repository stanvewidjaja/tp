package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.location.dates.VisitDate;

/**
 * Deletes the global note for a given date.
 */
public class DeleteNoteCommand extends Command {

    public static final String COMMAND_WORD = NoteCommand.COMMAND_WORD;

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes notes by date.\n"
            + "Parameters: d-/DATE\n"
            + "Example: " + COMMAND_WORD + " d-/2026-03-24";

    public static final String MESSAGE_SUCCESS = "Deleted notes on %1$s";
    public static final String MESSAGE_NO_NOTES_FOUND = "No notes found for the given date.";

    private final VisitDate date;

    /**
     * Creates a DeleteNoteCommand with the given date.
     */
    public DeleteNoteCommand(VisitDate date) {
        requireNonNull(date);
        this.date = date;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.hasNote(date)) {
            throw new CommandException(MESSAGE_NO_NOTES_FOUND);
        }

        model.removeNote(date);

        return new CommandResult(String.format(MESSAGE_SUCCESS, date));
    }

    public VisitDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteNoteCommand)) {
            return false;
        }

        DeleteNoteCommand otherCommand = (DeleteNoteCommand) other;
        return date.equals(otherCommand.date);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("date", date.toDataString())
                .toString();
    }
}
