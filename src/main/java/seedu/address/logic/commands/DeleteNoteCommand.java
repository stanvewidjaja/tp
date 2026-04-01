package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.location.dates.VisitDate;

/**
 * Deletes notes in AddressMe for a given date (backend implementation deferred).
 */
public class DeleteNoteCommand extends Command {

    public static final String COMMAND_WORD = NoteCommand.COMMAND_WORD;

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes notes by date.\n"
            + "Parameters: d-/DATE\n"
            + "Example: " + COMMAND_WORD + " d-/2026-03-24";

    public static final String MESSAGE_SUCCESS = "Delete note request received for date: %1$s";

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
