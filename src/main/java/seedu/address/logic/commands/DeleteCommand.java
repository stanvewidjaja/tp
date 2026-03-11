package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.location.Location;

/**
 * Deletes a location identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the location(s) identified by the index number(s) used in the displayed location list.\n"
            + "Parameters: INDEX [MORE_INDEXES]... (indices must be positive integers)\n"
            + "Example: " + COMMAND_WORD + " 1 2 3";

    public static final String MESSAGE_DELETE_LOCATION_SUCCESS = "Deleted Location: %1$s";
    public static final String MESSAGE_DELETE_LOCATIONS_SUCCESS = "Deleted Locations:\n%1$s";
    public static final String MESSAGE_DUPLICATE_INDEX = "Duplicate indices are not allowed.";

    private final List<Index> targetIndexes;

    public DeleteCommand(List<Index> targetIndexes) {
        requireNonNull(targetIndexes);
        if (targetIndexes.isEmpty()) {
            throw new IllegalArgumentException("At least one index must be provided.");
        }
        this.targetIndexes = List.copyOf(targetIndexes);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Location> lastShownList = model.getFilteredLocationList();
        Set<Integer> uniqueIndexes = new HashSet<>();
        List<Location> locationsToDelete = new ArrayList<>();

        for (Index targetIndex : targetIndexes) {
            if (!uniqueIndexes.add(targetIndex.getZeroBased())) {
                throw new CommandException(MESSAGE_DUPLICATE_INDEX);
            }

            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_LOCATION_DISPLAYED_INDEX);
            }

            locationsToDelete.add(lastShownList.get(targetIndex.getZeroBased()));
        }

        for (Location locationToDelete : locationsToDelete) {
            model.deleteLocation(locationToDelete);
        }

        if (locationsToDelete.size() == 1) {
            return new CommandResult(String.format(MESSAGE_DELETE_LOCATION_SUCCESS,
                    Messages.format(locationsToDelete.get(0))));
        }

        String deletedLocations = locationsToDelete.stream()
                .map(Messages::format)
                .collect(Collectors.joining("\n"));
        return new CommandResult(String.format(MESSAGE_DELETE_LOCATIONS_SUCCESS, deletedLocations));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndexes.equals(otherDeleteCommand.targetIndexes);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndexes", targetIndexes)
                .toString();
    }
}
