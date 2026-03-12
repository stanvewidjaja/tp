package seedu.address.model.location;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Represents a Location's visit date in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidVisitDate(String)}
 */
public class VisitDate {

    public static final String MESSAGE_CONSTRAINTS =
            "Visit dates should be in the format YYYY-MM-DD, and it should be a valid date";

    private final LocalDate value;

    /**
     * Constructs a {@code VisitDate}.
     *
     * @param visitDate A valid visit date string in YYYY-MM-DD format.
     */
    public VisitDate(String visitDate) {
        requireNonNull(visitDate);
        checkArgument(isValidVisitDate(visitDate), MESSAGE_CONSTRAINTS);
        value = LocalDate.parse(visitDate);
    }

    /**
     * Returns true if a given string is a valid visit date.
     */
    public static boolean isValidVisitDate(String test) {
        requireNonNull(test);
        try {
            LocalDate.parse(test);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public LocalDate getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof VisitDate)) {
            return false;
        }

        VisitDate otherVisitDate = (VisitDate) other;
        return value.equals(otherVisitDate.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
