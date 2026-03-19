package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditCommand.EditLocationDescriptor;
import seedu.address.model.location.Address;
import seedu.address.model.location.Email;
import seedu.address.model.location.Location;
import seedu.address.model.location.Name;
import seedu.address.model.location.Phone;
import seedu.address.model.location.PostalCode;
import seedu.address.model.location.VisitDate;
import seedu.address.model.tag.Tag;

/**
 * A utility class to help with building EditLocationDescriptor objects.
 */
public class EditLocationDescriptorBuilder {

    private EditLocationDescriptor descriptor;

    /**
     * Creates a new {@code EditLocationDescriptorBuilder} with an empty descriptor.
     */
    public EditLocationDescriptorBuilder() {
        descriptor = new EditLocationDescriptor();
    }

    /**
     * Creates a new {@code EditLocationDescriptorBuilder} with an empty descriptor.
     */
    public EditLocationDescriptorBuilder(EditLocationDescriptor descriptor) {
        this.descriptor = new EditLocationDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditLocationDescriptor} with fields containing {@code location}'s details
     */
    public EditLocationDescriptorBuilder(Location location) {
        descriptor = new EditLocationDescriptor();
        descriptor.setName(location.getName());
        location.getPhone().ifPresent(descriptor::setPhone);
        location.getEmail().ifPresent(descriptor::setEmail);
        location.getAddress().ifPresent(descriptor::setAddress);
        location.getPostalCode().ifPresent(descriptor::setPostalCode);
        descriptor.setVisitDates(location.getVisitDates());
        descriptor.setTags(location.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code EditLocationDescriptor} that we are building.
     */
    public EditLocationDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditLocationDescriptor} that we are building.
     */
    public EditLocationDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditLocationDescriptor} that we are building.
     */
    public EditLocationDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditLocationDescriptor} that we are building.
     */
    public EditLocationDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(new Address(address));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditLocationDescriptor} that we are building.
     */
    public EditLocationDescriptorBuilder withPostalCode(String postalCode) {
        descriptor.setPostalCode(new PostalCode(postalCode));
        return this;
    }

    /**
     * Sets visit dates (override)
     */
    public EditLocationDescriptorBuilder withVisitDates(String... visitDates) {
        Set<VisitDate> visitDateSet =
                Stream.of(visitDates).map(VisitDate::new).collect(Collectors.toSet());
        descriptor.setVisitDates(visitDateSet);
        return this;
    }

    /**
     * Convenience method (keep for compatibility)
     */
    public EditLocationDescriptorBuilder withVisitDate(String visitDate) {
        return withVisitDates(visitDate);
    }

    /**
     * Sets the {@code Tag}s of the {@code EditLocationDescriptor} that we are building.
     */
    public EditLocationDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet =
                Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    /**
     * Sets the visit dates to add to the {@code EditLocationDescriptor} that we are building.
     */
    public EditLocationDescriptorBuilder withVisitDatesToAdd(String... visitDates) {
        Set<VisitDate> visitDateSet =
                Stream.of(visitDates).map(VisitDate::new).collect(Collectors.toSet());
        descriptor.setVisitDatesToAdd(visitDateSet);
        return this;
    }

    /**
     * Sets the visit dates to remove from the {@code EditLocationDescriptor} that we are building.
     */
    public EditLocationDescriptorBuilder withVisitDatesToRemove(String... visitDates) {
        Set<VisitDate> visitDateSet =
                Stream.of(visitDates).map(VisitDate::new).collect(Collectors.toSet());
        descriptor.setVisitDatesToRemove(visitDateSet);
        return this;
    }

    /**
     * Sets the visit dates to remove from the {@code EditLocationDescriptor} that we are building.
     */
    public EditLocationDescriptorBuilder withTagsToAdd(String... tags) {
        Set<Tag> tagSet =
                Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTagsToAdd(tagSet);
        return this;
    }

    /**
     * Sets the tags to remove from the {@code EditLocationDescriptor} that we are building.
     */
    public EditLocationDescriptorBuilder withTagsToRemove(String... tags) {
        Set<Tag> tagSet =
                Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTagsToRemove(tagSet);
        return this;
    }

    /**
     * Returns the built {@code EditLocationDescriptor}.
     */
    public EditLocationDescriptor build() {
        return descriptor;
    }
}
