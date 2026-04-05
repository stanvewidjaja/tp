package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSTAL_CODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.location.Location;

/**
 * Adds a location to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a location to AddressMe.\n"
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_POSTAL_CODE + "POSTAL_CODE] "
            + "[" + PREFIX_DATE + "DATE] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "McDonalds Bugis "
            + PREFIX_PHONE + "67773777 "
            + PREFIX_EMAIL + "customercare@sg.mcd.com "
            + PREFIX_ADDRESS + "113 Aljunied Ave 2 "
            + PREFIX_POSTAL_CODE + "380113 "
            + PREFIX_DATE + "2026-02-19 "
            + PREFIX_TAG + "restaurant "
            + PREFIX_TAG + "fastfood";

    public static final String MESSAGE_SUCCESS = "New location added: %1$s";
    public static final String MESSAGE_DUPLICATE_LOCATION = "This location already exists in AddressMe.\n"
            + "Use the edit command to update the existing entry instead.";

    private final Location toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Location}
     */
    public AddCommand(Location location) {
        requireNonNull(location);
        toAdd = location;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasLocation(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_LOCATION);
        }

        model.addLocation(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
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

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
