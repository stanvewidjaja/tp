package seedu.address.model.location.dates;

import static java.util.Objects.requireNonNull;

import java.time.DayOfWeek;
import java.time.LocalDate;

import seedu.address.logic.parser.DateParser;


/**
 * Represents a {@code VisitDate} that occurs once a week on a specific day
 */
public class WeeklyRecurringDate extends VisitDate {

    public static final String MESSAGE_CONSTRAINTS = DateParser.MESSAGE_WRONG_DATE_FORMAT;

    private final DayOfWeek day;

    /**
     * Constructs a {@code WeeklyRecurringDate}.
     * @param dow A valid DayOfWeek
     */
    public WeeklyRecurringDate(DayOfWeek dow) {
        requireNonNull(dow);
        this.day = dow;
    }

    @Override
    public boolean isOn(LocalDate date) {
        requireNonNull(date);
        return date.getDayOfWeek().equals(day);
    }

    @Override
    public String toDataString() {
        return "e-" + day.toString();
    }

    @Override
    public String toString() {
        String dayString = day.toString();
        dayString = dayString.substring(0, 1).toUpperCase() + dayString.substring(1).toLowerCase();
        return "Every " + dayString;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof WeeklyRecurringDate)) {
            return false;
        }

        WeeklyRecurringDate otherDate = (WeeklyRecurringDate) other;
        return this.day.equals(otherDate.day);
    }

    @Override
    public int hashCode() {
        return day.hashCode();
    }
}
