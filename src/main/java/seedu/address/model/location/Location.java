package seedu.address.model.location;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.location.dates.VisitDate;
import seedu.address.model.tag.Tag;

/**
 * Represents a Location in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Location {

    // Identity fields
    private final Name name;
    private final Optional<Phone> phone;
    private final Optional<Email> email;
    private final Optional<PostalCode> postalCode;

    // Data fields
    private final Optional<Address> address;
    private final Set<VisitDate> visitDates = new HashSet<>();
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Creates a Location, optional fields may be empty.
     */
    public Location(Name name, Optional<Phone> phone, Optional<Email> email,
                    Optional<Address> address, Optional<PostalCode> postalCode,
                    Set<VisitDate> visitDates, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, postalCode, visitDates, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.postalCode = postalCode;
        this.visitDates.addAll(visitDates);
        this.tags.addAll(tags);
    }

    public Name getName() {
        return name;
    }

    public Optional<Phone> getPhone() {
        return phone;
    }

    public String getPhoneString() {
        return phone.map(Phone::toString).orElse("-");
    }

    public Optional<Email> getEmail() {
        return email;
    }

    public String getEmailString() {
        return email.map(Email::toString).orElse("-");
    }

    public Optional<PostalCode> getPostalCode() {
        return postalCode;
    }

    public String getPostalString() {
        return postalCode.map(PostalCode::toString).orElse("-");
    }

    public Optional<Address> getAddress() {
        return address;
    }

    public String getAddressString() {
        return address.map(Address::toString).orElse("-");
    }

    /**
     * Returns an immutable visit date set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<VisitDate> getVisitDates() {
        return Collections.unmodifiableSet(visitDates);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both locations have the same name.
     * This defines a weaker notion of equality between two locations.
     */
    public boolean isSameLocation(Location otherLocation) {
        if (otherLocation == this) {
            return true;
        }

        return otherLocation != null
                && otherLocation.getName().equals(getName());
    }

    /**
     * Returns true if location is tagged with a specific date
     */
    public boolean occursOn(LocalDate date) {
        return this.visitDates.stream().anyMatch(vd -> vd.isOn(date));
    }

    /**
     * Returns true if both locations have the same identity and data fields.
     * This defines a stronger notion of equality between two locations.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Location)) {
            return false;
        }

        Location otherLocation = (Location) other;
        return name.equals(otherLocation.name)
                && phone.equals(otherLocation.phone)
                && email.equals(otherLocation.email)
                && address.equals(otherLocation.address)
                && postalCode.equals(otherLocation.postalCode)
                && visitDates.equals(otherLocation.visitDates)
                && tags.equals(otherLocation.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email, address, postalCode, visitDates, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", getPhoneString())
                .add("email", getEmailString())
                .add("address", getAddressString())
                .add("postalCode", getPostalString())
                .add("visitDates", visitDates)
                .add("tags", tags)
                .toString();
    }
}
