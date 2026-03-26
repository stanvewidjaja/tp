package seedu.address.model.location;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Objects;

/**
 * Represents a PostalCode in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPostalCode(String)}
 */
public class PostalCode {

    /**
     * Message shown when postal code constraints are violated.
     */
    public static final String MESSAGE_CONSTRAINTS =
            "Postal code should be alphanumeric.";

    /**
     * The postal code value.
     */
    public final String value;

    /**
     * Constructs a {@code PostalCode}.
     *
     * @param postalCode A valid postal code.
     */
    public PostalCode(String postalCode) {
        requireNonNull(postalCode);
        checkArgument(isValidPostalCode(postalCode), MESSAGE_CONSTRAINTS);
        value = postalCode;
    }

    /**
     * Returns true if a given string is a valid postal code.
     *
     * @param test The string to test.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidPostalCode(String test) {
        return test.matches("[a-zA-Z0-9]+");
    }

    /**
     * Returns the string representation of the postal code.
     *
     * @return postal code value.
     */
    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PostalCode)) {
            return false;
        }

        PostalCode otherPostalCode = (PostalCode) other;
        return value.equals(otherPostalCode.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
