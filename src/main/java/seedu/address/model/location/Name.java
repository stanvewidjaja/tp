package seedu.address.model.location;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Location's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain alphanumeric characters, spaces, and some symbols.\n"
                    + "Symbols allowed are ampersand, at-sign, brackets, period, comma, hyphen and apostrophe.\n"
                    + "It should not be blank, and contain at least one alphanumeric character.";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "^(?=.*[A-Za-z0-9])[A-Za-z0-9 &@()\\[\\].,\\-']+$";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equals(otherName.fullName);
    }

    /**
     * Checks name equality in a case-insensitive manner.
     * Used by isSameLocation method in Location class.
     *
     * @param otherName the other {@code Name} to compare against
     * @return true if both names are equal ignoring case
     */
    public boolean isSameNameIgnoreCase(Name otherName) {
        return otherName != null
                && fullName.equalsIgnoreCase(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
