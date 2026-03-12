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
import seedu.address.model.location.VisitDate;
import seedu.address.model.tag.Tag;

/**
 * A utility class to help with building EditLocationDescriptor objects.
 */
public class EditLocationDescriptorBuilder {

    private EditLocationDescriptor descriptor;

    public EditLocationDescriptorBuilder() {
        descriptor = new EditLocationDescriptor();
    }

    public EditLocationDescriptorBuilder(EditLocationDescriptor descriptor) {
        this.descriptor = new EditLocationDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditLocationDescriptor} with fields containing {@code location}'s details
     */
    public EditLocationDescriptorBuilder(Location location) {
        descriptor = new EditLocationDescriptor();
        descriptor.setName(location.getName());
        descriptor.setPhone(location.getPhone());
        descriptor.setEmail(location.getEmail());
        descriptor.setAddress(location.getAddress());
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
     * Sets the {@code Email} of the {@code EditLocationDescriptor} that we are building.
     */
    public EditLocationDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditLocationDescriptor} that we are building.
     */
    public EditLocationDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(new Address(address));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditLocationDescriptor} that we are building.
     */
    public EditLocationDescriptorBuilder withVisitDate(String visitDate) {
        descriptor.setVisitDate(new VisitDate(visitDate));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditLocationDescriptor}
     * that we are building.
     */
    public EditLocationDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditLocationDescriptor build() {
        return descriptor;
    }
}
