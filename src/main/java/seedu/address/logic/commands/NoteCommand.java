package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.location.NoteContent;
import seedu.address.model.location.dates.VisitDate;

/**
 * Records a note in AddressMe (backend implementation deferred).
 */
public class NoteCommand extends Command {

    public static final String COMMAND_WORD = "note";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Records or deletes notes in AddressMe "
            + "(work in progress).\n"
            + "Add format: n/NOTE_CONTENT d/DATE\n"
            + "Delete format: d-/DATE\n"
            + "\nExamples:\n"
            + COMMAND_WORD + " n/Involves lots of walking. Bring extra water bottles. d/2026-03-24\n"
            + COMMAND_WORD + " d-/2026-03-24";

    public static final String MESSAGE_SUCCESS = "New note recorded: %1$s";

    private final NoteContent noteContent;
    private final VisitDate date;

    /**
     * Creates a NoteCommand with the given note text and date.
     */
    public NoteCommand(NoteContent noteContent, VisitDate date) {
        requireNonNull(noteContent);
        requireNonNull(date);
        this.noteContent = noteContent;
        this.date = date;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        model.setNote(date, noteContent);
        String detail = noteContent.toString() + " (" + date.toString() + ")";

        return new CommandResult(String.format(MESSAGE_SUCCESS, detail));
    }

    @Override
    public boolean isStateMutating() {
        return true;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof NoteCommand)) {
            return false;
        }

        NoteCommand otherCommand = (NoteCommand) other;
        return noteContent.equals(otherCommand.noteContent)
                && date.equals(otherCommand.date);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("noteContent", noteContent)
                .add("date", date)
                .toString();
    }
}
