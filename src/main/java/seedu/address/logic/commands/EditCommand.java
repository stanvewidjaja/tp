package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_LOCATIONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.location.Address;
import seedu.address.model.location.Email;
import seedu.address.model.location.Location;
import seedu.address.model.location.Name;
import seedu.address.model.location.Phone;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing location in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the location identified "
            + "by the index number used in the displayed location list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_LOCATION_SUCCESS = "Edited Location: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_LOCATION = "This location already exists in the address book.";

    private final Index index;
    private final EditLocationDescriptor editLocationDescriptor;

    /**
     * @param index of the location in the filtered location list to edit
     * @param editLocationDescriptor details to edit the location with
     */
    public EditCommand(Index index, EditLocationDescriptor editLocationDescriptor) {
        requireNonNull(index);
        requireNonNull(editLocationDescriptor);

        this.index = index;
        this.editLocationDescriptor = new EditLocationDescriptor(editLocationDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Location> lastShownList = model.getFilteredLocationList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_LOCATION_DISPLAYED_INDEX);
        }

        Location locationToEdit = lastShownList.get(index.getZeroBased());
        Location editedLocation = createEditedLocation(locationToEdit, editLocationDescriptor);

        if (!locationToEdit.isSameLocation(editedLocation) && model.hasLocation(editedLocation)) {
            throw new CommandException(MESSAGE_DUPLICATE_LOCATION);
        }

        model.setLocation(locationToEdit, editedLocation);
        model.updateFilteredLocationList(PREDICATE_SHOW_ALL_LOCATIONS);
        return new CommandResult(String.format(MESSAGE_EDIT_LOCATION_SUCCESS, Messages.format(editedLocation)));
    }

    /**
     * Creates and returns a {@code Location} with the details of {@code locationToEdit}
     * edited with {@code editLocationDescriptor}.
     */
    private static Location createEditedLocation(Location locationToEdit,
                                                 EditLocationDescriptor editLocationDescriptor) {
        assert locationToEdit != null;

        Name updatedName = editLocationDescriptor.getName().orElse(locationToEdit.getName());
        Phone updatedPhone = editLocationDescriptor.getPhone().orElse(locationToEdit.getPhone());
        Email updatedEmail = editLocationDescriptor.getEmail().orElse(locationToEdit.getEmail());
        Address updatedAddress = editLocationDescriptor.getAddress().orElse(locationToEdit.getAddress());
        Set<Tag> updatedTags = editLocationDescriptor.getTags().orElse(locationToEdit.getTags());

        return new Location(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editLocationDescriptor.equals(otherEditCommand.editLocationDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editLocationDescriptor", editLocationDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the location with. Each non-empty field value will replace the
     * corresponding field value of the location.
     */
    public static class EditLocationDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> tags;

        public EditLocationDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditLocationDescriptor(EditLocationDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditLocationDescriptor)) {
                return false;
            }

            EditLocationDescriptor otherEditLocationDescriptor = (EditLocationDescriptor) other;
            return Objects.equals(name, otherEditLocationDescriptor.name)
                    && Objects.equals(phone, otherEditLocationDescriptor.phone)
                    && Objects.equals(email, otherEditLocationDescriptor.email)
                    && Objects.equals(address, otherEditLocationDescriptor.address)
                    && Objects.equals(tags, otherEditLocationDescriptor.tags);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("address", address)
                    .add("tags", tags)
                    .toString();
        }
    }
}
