package seedu.address.model.location;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.DateParser;

/**
 * Represents a Location's visit date in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidVisitDate(String)}
 */
public class VisitDate {

    public static final String MESSAGE_CONSTRAINTS = DateParser.MESSAGE_WRONG_DATE_FORMAT;

    private final LocalDate value;

    /**
     * Constructs a {@code VisitDate}.
     *
     * @param visitDate A valid visit date string in YYYY-MM-DD format.
     */
    public VisitDate(String visitDate) {
        requireNonNull(visitDate);
        checkArgument(isValidVisitDate(visitDate), VisitDate.MESSAGE_CONSTRAINTS);
        try {
            value = DateParser.parse(visitDate);
        } catch (IllegalValueException e) {
            // should not happen since date was checked above
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns true if a given string is a valid visit date.
     */
    public static boolean isValidVisitDate(String test) {
        requireNonNull(test);
        try {
            DateParser.parse(test);
            return true;
        } catch (IllegalValueException e) {
            return false;
        }
    }

    public LocalDate getValue() {
        return value;
    }

    public boolean isOn(LocalDate date) {
        return this.value.equals(date);
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
