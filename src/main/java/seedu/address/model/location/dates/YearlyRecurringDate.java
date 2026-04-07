package seedu.address.model.location.dates;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import seedu.address.logic.parser.DateParser;

/**
 * Represents a {@code VisitDate} that occurs once a year on a specific day of a month
 */
public class YearlyRecurringDate extends VisitDate {
    public static final String MESSAGE_CONSTRAINTS = DateParser.MESSAGE_WRONG_DATE_FORMAT;

    private static final DateTimeFormatter PRETTY_FORMAT = DateTimeFormatter.ofPattern("dd MMM", Locale.ENGLISH);
    private static final DateTimeFormatter DATA_FORMAT = DateTimeFormatter.ofPattern("dd/MM", Locale.ENGLISH);
    private static final DateTimeFormatter SORT_FORMAT = DateTimeFormatter.ofPattern("MM/dd", Locale.ENGLISH);
    private final MonthDay date;

    /**
     * Constructs a {@code WeeklyRecurringDate}.
     * @param md A valid DayOfWeek
     */
    public YearlyRecurringDate(MonthDay md) {
        requireNonNull(md);
        this.date = md;
    }

    @Override
    public boolean isOn(LocalDate date) {
        return MonthDay.from(date).equals(this.date);
    }

    @Override
    public String toDataString() {
        return "e-" + date.format(DATA_FORMAT);
    }

    @Override
    public String toSortString() {
        return "eb-" + date.format(SORT_FORMAT);
    }

    @Override
    public String toString() {
        return "Every " + date.format(PRETTY_FORMAT);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof YearlyRecurringDate)) {
            return false;
        }

        YearlyRecurringDate otherDate = (YearlyRecurringDate) other;
        return date.equals(otherDate.date);
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }
}
