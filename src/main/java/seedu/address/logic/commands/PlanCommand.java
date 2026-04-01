package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

import seedu.address.logic.parser.DateParser;
import seedu.address.model.Model;


/**
 * Lists all locations in the address book to the user.
 */
public class PlanCommand extends Command {

    public static final String COMMAND_WORD = "plan";


    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Displays the all the locations listed for a "
            + "specific date in the right window.\n"
            + "Parameters: DATE\n"
            + "Example: " + COMMAND_WORD + " 13/3/26";

    public static final String MESSAGE_DISPLAY_SUCCESS = "Displaying date: %1$s";

    public static final String MESSAGE_CLEAR_SUCCESS = "Cleared planner display.";

    private final LocalDate date;

    /**
     * @param date date to compile and display. Can be null
     */
    public PlanCommand(LocalDate date) {
        this.date = date;
    }

    /* TODO: Revisit */
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        model.updatePlannerLocationList(this.date);

        return new CommandResult(generateSuccessMessage(date != null), DateParser.dateToPrettyString(date));
    }

    /**
     * Generates a command execution success message based on whether
     * the view is to display a date or to clear
     * @param isDisplaying true if a date is loaded in
     */
    private String generateSuccessMessage(boolean isDisplaying) {
        return isDisplaying ? String.format(MESSAGE_DISPLAY_SUCCESS, DateParser.dateToPrettyString(date))
                : MESSAGE_CLEAR_SUCCESS;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PlanCommand)) {
            return false;
        }

        PlanCommand e = (PlanCommand) other;
        if (date == null) {
            return e.date == null;
        }

        return date.equals(e.date);
    }
}
