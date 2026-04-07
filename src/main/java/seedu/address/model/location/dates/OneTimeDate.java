package seedu.address.model.location.dates;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import seedu.address.logic.parser.DateParser;

/**
 * Represents a {@code VisitDate} that occurs only once
 */
public class OneTimeDate extends VisitDate {

    public static final String MESSAGE_CONSTRAINTS = DateParser.MESSAGE_WRONG_DATE_FORMAT;
    private static final DateTimeFormatter PRETTY_FORMAT = DateTimeFormatter.ofPattern("d MMM yy", Locale.ENGLISH);
    private static final DateTimeFormatter SORT_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.ENGLISH);

    private final LocalDate date;

    /**
     * Constructs a {@code VisitDate}.
     *
     * @param visitDate A valid localdate object
     */
    public OneTimeDate(LocalDate visitDate) {
        requireNonNull(visitDate);
        this.date = visitDate;
    }

    @Override
    public boolean isOn(LocalDate date) {
        requireNonNull(date);
        return this.date.equals(date);
    }

    @Override
    public String toDataString() {
        return date.toString();
    }

    @Override
    public String toSortString() {
        return "z" + date.format(SORT_FORMAT);
    }

    @Override
    public String toString() {
        return date.format(PRETTY_FORMAT);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof OneTimeDate)) {
            return false;
        }

        OneTimeDate otherDate = (OneTimeDate) other;
        return date.equals(otherDate.date);
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }
}
