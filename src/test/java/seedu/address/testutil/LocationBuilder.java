package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.location.Address;
import seedu.address.model.location.Email;
import seedu.address.model.location.Location;
import seedu.address.model.location.Name;
import seedu.address.model.location.Phone;
import seedu.address.model.location.VisitDate;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Location objects.
 */
public class LocationBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_VISIT_DATE = "2026-03-12";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private VisitDate visitDate;
    private Set<Tag> tags;

    /**
     * Creates a {@code LocationBuilder} with the default details.
     */
    public LocationBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        visitDate = new VisitDate(DEFAULT_VISIT_DATE);
        tags = new HashSet<>();
    }

    /**
     * Initializes the LocationBuilder with the data of {@code locationToCopy}.
     */
    public LocationBuilder(Location locationToCopy) {
        name = locationToCopy.getName();
        phone = locationToCopy.getPhone();
        email = locationToCopy.getEmail();
        address = locationToCopy.getAddress();
        visitDate = locationToCopy.getVisitDate();
        tags = new HashSet<>(locationToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Location} that we are building.
     */
    public LocationBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Location} that we are building.
     */
    public LocationBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Location} that we are building.
     */
    public LocationBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Location} that we are building.
     */
    public LocationBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Location} that we are building.
     */
    public LocationBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code VisitDate} of the {@code Location} that we are building.
     */
    public LocationBuilder withVisitDate(String visitDate) {
        this.visitDate = new VisitDate(visitDate);
        return this;
    }

    public Location build() {
        return new Location(name, phone, email, address, visitDate, tags);
    }

}
