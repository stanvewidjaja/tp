package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.location.Location;

/**
 * Finds and lists all locations in address book whose fields match the search criteria.
 * Search criteria can include names (any keyword), tags, addresses, and visit dates.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all locations whose fields match the "
            + "specified criteria (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: [NAME_KEYWORD]... [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]... [d/DATE]...\n"
            + "Notes: Name search is based on substring match of any provided keywords.\n"
            + "       Search by multiple prefixes (n/, p/, e/, a/, t/, d/) uses AND logic.\n"
            + "Example: " + COMMAND_WORD + " sky n/dome p/9123 e/info@skyarena.com t/entertainment "
            + "a/Clementi d/2024-01-15";

    private final Predicate<Location> predicate;

    public FindCommand(Predicate<Location> predicate) {
        this.predicate = requireNonNull(predicate);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredLocationList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_LOCATIONS_LISTED_OVERVIEW, model.getFilteredLocationList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
