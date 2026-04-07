package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE_ADD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE_REMOVE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSTAL_CODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_ADD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_REMOVE;
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
import seedu.address.model.location.PostalCode;
import seedu.address.model.location.dates.VisitDate;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing location in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the location identified "
            + "by the index number used in the displayed location list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_POSTAL_CODE + "POSTAL_CODE] "
            + "[" + PREFIX_DATE + "DATE]... "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "            [" + PREFIX_DATE_ADD + "DATE]... "
            + "[" + PREFIX_DATE_REMOVE + "DATE]... "
            + "[" + PREFIX_TAG_ADD + "TAG]... "
            + "[" + PREFIX_TAG_REMOVE + "TAG]...\n"
            + "Notes: INDEX must be a positive integer.\n"
            + "       t/ or d/ replaces all tags and dates respectively; "
            + "t+/t-/ and d+/d-/ adds or removes specified tag(s) and date(s) respectively.\n"
            + "       Do not mix standard and incremental forms (e.g. do not use d/ and d+/ together).\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "contact@sundowncafe.com "
            + PREFIX_DATE_ADD + "2026-01-01 "
            + PREFIX_DATE_REMOVE + "2026-02-01";

    public static final String MESSAGE_EDIT_LOCATION_SUCCESS = "Edited Location: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_LOCATION = "This location already exists in the address book.";
    public static final String MESSAGE_CANNOT_OVERRIDE_AND_MODIFY_TAGS = "Cannot combine t/ with t+/ or t-/";
    public static final String MESSAGE_CANNOT_OVERRIDE_AND_MODIFY_DATES = "Cannot combine d/ with d+/ or d-/";

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
            throw new CommandException(Messages.getInvalidLocationDisplayedIndexMessage(lastShownList.size()));
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

    @Override
    public boolean isStateMutating() {
        return true;
    }

    /**
     * Creates and returns a {@code Location} with the details of {@code locationToEdit}
     * edited with {@code editLocationDescriptor}.
     */
    private static Location createEditedLocation(Location locationToEdit,
                                                 EditLocationDescriptor editLocationDescriptor) {
        assert locationToEdit != null;

        Name updatedName = editLocationDescriptor.getName().orElse(locationToEdit.getName());

        Optional<Phone> updatedPhone = editLocationDescriptor.getPhone().isPresent()
                ? Optional.of(editLocationDescriptor.getPhone().get())
                : locationToEdit.getPhone();

        Optional<Email> updatedEmail = editLocationDescriptor.getEmail().isPresent()
                ? Optional.of(editLocationDescriptor.getEmail().get())
                : locationToEdit.getEmail();

        Optional<Address> updatedAddress = editLocationDescriptor.getAddress().isPresent()
                ? Optional.of(editLocationDescriptor.getAddress().get())
                : locationToEdit.getAddress();

        Optional<PostalCode> updatedPostalCode = editLocationDescriptor.getPostalCode().isPresent()
                ? Optional.of(editLocationDescriptor.getPostalCode().get())
                : locationToEdit.getPostalCode();

        Set<VisitDate> updatedVisitDates;
        if (editLocationDescriptor.getVisitDates().isPresent()) {
            updatedVisitDates = new HashSet<>(editLocationDescriptor.getVisitDates().get());
        } else {
            updatedVisitDates = new HashSet<>(locationToEdit.getVisitDates());
            editLocationDescriptor.getVisitDatesToAdd().ifPresent(updatedVisitDates::addAll);
            editLocationDescriptor.getVisitDatesToRemove().ifPresent(updatedVisitDates::removeAll);
        }

        Set<Tag> updatedTags;
        if (editLocationDescriptor.getTags().isPresent()) {
            updatedTags = new HashSet<>(editLocationDescriptor.getTags().get());
        } else {
            updatedTags = new HashSet<>(locationToEdit.getTags());
            editLocationDescriptor.getTagsToAdd().ifPresent(updatedTags::addAll);
            editLocationDescriptor.getTagsToRemove().ifPresent(updatedTags::removeAll);
        }

        return new Location(updatedName, updatedPhone, updatedEmail,
                updatedAddress, updatedPostalCode, updatedVisitDates, updatedTags, locationToEdit.getNotes());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

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
        private PostalCode postalCode;
        private Set<VisitDate> visitDates;
        private Set<Tag> tags;

        private Set<VisitDate> visitDatesToAdd;
        private Set<VisitDate> visitDatesToRemove;

        private Set<Tag> tagsToAdd;
        private Set<Tag> tagsToRemove;

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
            setPostalCode(toCopy.postalCode);
            setVisitDates(toCopy.visitDates);
            setVisitDatesToAdd(toCopy.visitDatesToAdd);
            setVisitDatesToRemove(toCopy.visitDatesToRemove);
            setTags(toCopy.tags);
            setTagsToAdd(toCopy.tagsToAdd);
            setTagsToRemove(toCopy.tagsToRemove);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(
                    name, phone, email, address, postalCode, visitDates, tags,
                    visitDatesToAdd, visitDatesToRemove,
                    tagsToAdd, tagsToRemove
            );
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

        public void setPostalCode(PostalCode postalCode) {
            this.postalCode = postalCode;
        }

        public Optional<PostalCode> getPostalCode() {
            return Optional.ofNullable(postalCode);
        }

        public void setVisitDates(Set<VisitDate> visitDates) {
            this.visitDates = (visitDates != null) ? new HashSet<>(visitDates) : null;
        }

        public Optional<Set<VisitDate>> getVisitDates() {
            return (visitDates != null) ? Optional.of(Collections.unmodifiableSet(visitDates)) : Optional.empty();
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        public void setVisitDatesToAdd(Set<VisitDate> visitDatesToAdd) {
            this.visitDatesToAdd = (visitDatesToAdd != null) ? new HashSet<>(visitDatesToAdd) : null;
        }

        public Optional<Set<VisitDate>> getVisitDatesToAdd() {
            return (visitDatesToAdd != null)
                    ? Optional.of(Collections.unmodifiableSet(visitDatesToAdd)) : Optional.empty();
        }

        public void setVisitDatesToRemove(Set<VisitDate> visitDatesToRemove) {
            this.visitDatesToRemove = (visitDatesToRemove != null) ? new HashSet<>(visitDatesToRemove) : null;
        }

        public Optional<Set<VisitDate>> getVisitDatesToRemove() {
            return (visitDatesToRemove != null)
                    ? Optional.of(Collections.unmodifiableSet(visitDatesToRemove)) : Optional.empty();
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        public void setTagsToAdd(Set<Tag> tagsToAdd) {
            this.tagsToAdd = (tagsToAdd != null) ? new HashSet<>(tagsToAdd) : null;
        }

        public Optional<Set<Tag>> getTagsToAdd() {
            return (tagsToAdd != null) ? Optional.of(Collections.unmodifiableSet(tagsToAdd)) : Optional.empty();
        }

        public void setTagsToRemove(Set<Tag> tagsToRemove) {
            this.tagsToRemove = (tagsToRemove != null) ? new HashSet<>(tagsToRemove) : null;
        }

        public Optional<Set<Tag>> getTagsToRemove() {
            return (tagsToRemove != null) ? Optional.of(Collections.unmodifiableSet(tagsToRemove)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof EditLocationDescriptor)) {
                return false;
            }

            EditLocationDescriptor otherEditLocationDescriptor = (EditLocationDescriptor) other;
            return Objects.equals(name, otherEditLocationDescriptor.name)
                    && Objects.equals(phone, otherEditLocationDescriptor.phone)
                    && Objects.equals(email, otherEditLocationDescriptor.email)
                    && Objects.equals(address, otherEditLocationDescriptor.address)
                    && Objects.equals(postalCode, otherEditLocationDescriptor.postalCode)
                    && Objects.equals(visitDates, otherEditLocationDescriptor.visitDates)
                    && Objects.equals(visitDatesToAdd, otherEditLocationDescriptor.visitDatesToAdd)
                    && Objects.equals(visitDatesToRemove, otherEditLocationDescriptor.visitDatesToRemove)
                    && Objects.equals(tags, otherEditLocationDescriptor.tags)
                    && Objects.equals(tagsToAdd, otherEditLocationDescriptor.tagsToAdd)
                    && Objects.equals(tagsToRemove, otherEditLocationDescriptor.tagsToRemove);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("address", address)
                    .add("postalCode", postalCode)
                    .add("visitDates", visitDates)
                    .add("visitDatesToAdd", visitDatesToAdd)
                    .add("visitDatesToRemove", visitDatesToRemove)
                    .add("tags", tags)
                    .add("tagsToAdd", tagsToAdd)
                    .add("tagsToRemove", tagsToRemove)
                    .toString();
        }
    }
}
