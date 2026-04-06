package seedu.address.testutil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.location.Address;
import seedu.address.model.location.Email;
import seedu.address.model.location.Location;
import seedu.address.model.location.Name;
import seedu.address.model.location.Phone;
import seedu.address.model.location.PostalCode;
import seedu.address.model.location.dates.VisitDate;
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
    public static final String DEFAULT_POSTAL_CODE = "640123";
    public static final String DEFAULT_VISIT_DATE = "2026-03-12";

    private Name name;
    private Optional<Phone> phone;
    private Optional<Email> email;
    private Optional<Address> address;
    private Optional<PostalCode> postalCode;
    private Set<VisitDate> visitDates;
    private Set<Tag> tags;
    private Map<VisitDate, String> notes;

    /**
     * Creates a {@code LocationBuilder} with default details.
     */
    public LocationBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = Optional.of(new Phone(DEFAULT_PHONE));
        email = Optional.of(new Email(DEFAULT_EMAIL));
        address = Optional.of(new Address(DEFAULT_ADDRESS));
        postalCode = Optional.of(new PostalCode(DEFAULT_POSTAL_CODE));
        visitDates = SampleDataUtil.getVisitDateSet(DEFAULT_VISIT_DATE);
        tags = new HashSet<>();
        notes = new HashMap<>();
    }

    /**
     * Initializes the LocationBuilder with the data of {@code locationToCopy}.
     */
    public LocationBuilder(Location locationToCopy) {
        name = locationToCopy.getName();
        phone = locationToCopy.getPhone();
        email = locationToCopy.getEmail();
        address = locationToCopy.getAddress();
        postalCode = locationToCopy.getPostalCode();
        visitDates = new HashSet<>(locationToCopy.getVisitDates());
        tags = new HashSet<>(locationToCopy.getTags());
        notes = new HashMap<>(locationToCopy.getNotes());
    }

    /**
     * Sets the {@code Name} of the {@code Location} that we are building.
     */
    public LocationBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code Name} of the {@code Location} that we are building and the rest to empty optionals.
     */
    public LocationBuilder withOnlyName(String name) {
        this.name = new Name(name);
        phone = Optional.empty();
        email = Optional.empty();
        address = Optional.empty();
        postalCode = Optional.empty();
        visitDates = new HashSet<>();
        tags = new HashSet<>();
        notes = new HashMap<>();
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and sets it to the {@code Location} that we are building.
     */
    public LocationBuilder withTags(String... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Location} that we are building.
     */
    public LocationBuilder withAddress(String address) {
        this.address = Optional.of(new Address(address));
        return this;
    }

    /**
     * Removes the {@code Address} of the {@code Location} that we are building.
     */
    public LocationBuilder withoutAddress() {
        this.address = Optional.empty();
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Location} that we are building.
     */
    public LocationBuilder withPhone(String phone) {
        this.phone = Optional.of(new Phone(phone));
        return this;
    }

    /**
     * Removes the {@code Phone} of the {@code Location} that we are building.
     */
    public LocationBuilder withoutPhone() {
        this.phone = Optional.empty();
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Location} that we are building.
     */
    public LocationBuilder withEmail(String email) {
        this.email = Optional.of(new Email(email));
        return this;
    }

    /**
     * Removes the {@code Email} of the {@code Location} that we are building.
     */
    public LocationBuilder withoutEmail() {
        this.email = Optional.empty();
        return this;
    }

    /**
     * Sets the {@code PostalCode} of the {@code Location} that we are building.
     */
    public LocationBuilder withPostalCode(String postalCode) {
        this.postalCode = Optional.of(new PostalCode(postalCode));
        return this;
    }

    /**
     * Removes the {@code PostalCode} of the {@code Location} that we are building.
     */
    public LocationBuilder withoutPostalCode() {
        this.postalCode = Optional.empty();
        return this;
    }

    /**
     * Sets the {@code VisitDates} of the {@code Location} that we are building.
     */
    public LocationBuilder withVisitDates(String... visitDates) {
        this.visitDates = SampleDataUtil.getVisitDateSet(visitDates);
        return this;
    }

    /**
     * Sets a single {@code VisitDate} of the {@code Location} that we are building.
     */
    public LocationBuilder withVisitDate(String visitDate) {
        return withVisitDates(visitDate);
    }

    /**
     * Removes all {@code VisitDate}s of the {@code Location} that we are building.
     */
    public LocationBuilder withoutVisitDates() {
        this.visitDates = new HashSet<>();
        return this;
    }

    /**
     * Sets a note for the given {@code VisitDate} of the {@code Location} that we are building.
     */
    public LocationBuilder withNote(String visitDate, String note) {
        try {
            this.notes.put(VisitDate.of(visitDate), note);
        } catch (IllegalValueException e) {
            throw new IllegalArgumentException("Invalid visit date: " + visitDate, e);
        }
        return this;
    }

    public Location build() {
        return new Location(name, phone, email, address, postalCode, visitDates, tags, notes);
    }
}
