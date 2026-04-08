package seedu.address.model.location.dates;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.DateParser;

/**
 * Represents a Location's visit date in the address book.
 * Guarantees: immutable;
 */
public abstract class VisitDate {

    public static final String MESSAGE_CONSTRAINTS_EMPTY = "The date cannot be empty!.";
    public static final String MESSAGE_CONSTRAINTS_RECURRING = "Recurring dates must be yearly or weekly. Try these"
            + " formats:\ne-d/M, e-d/M, every d/M to indicate it happening every year.\nevery Tuesday, e-fri as "
            + "examples of dates occurring every Tuesday or Friday.\nDates must be valid.";
    public static final String MESSAGE_CONSTRAINTS = DateParser.MESSAGE_WRONG_DATE_FORMAT
            + "\nFor recurring dates, use the format e-[DATE], or every [DATE],\n"
            + "where [DATE] is in day and month format or a day of the week";

    private static final String RECURRING_DATE_REGEX = "^(every\\s+|e-).+";
    private static final String RECURRING_DATE_PREFIX_REGEX = "^(every\\s+|e-)";

    /**
     * Factory method for creating VisitDates from a string
     */
    public static VisitDate of(String dateString) throws IllegalValueException {
        requireNonNull(dateString);
        if (dateString.isEmpty()) {
            throw new IllegalValueException(MESSAGE_CONSTRAINTS_EMPTY);
        }
        dateString = dateString.toLowerCase();
        // case for every day
        if (dateString.equals("everyday") || dateString.equals("every day")) {
            return EveryDayDate.EVERYDAY_DATE;
        }

        // case for recurring dates, starts with every ... or r-
        if (dateString.matches(RECURRING_DATE_REGEX)) {
            String recurring = dateString.replaceFirst(RECURRING_DATE_PREFIX_REGEX, "");

            // try to match with either yearly or weekly date
            try {
                return new YearlyRecurringDate(DateParser.parseMonthDay(recurring));
            } catch (IllegalValueException e) {
                //ignore for now
            }
            try {
                return new WeeklyRecurringDate(DateParser.parseDayOfWeek(recurring));
            } catch (IllegalValueException e) {
                //ignore for now
            }
            throw new IllegalValueException(MESSAGE_CONSTRAINTS_RECURRING);
        }

        try {
            return new OneTimeDate(DateParser.parse(dateString));
        } catch (IllegalValueException e) {
            throw new IllegalValueException(MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Factory method for creating VisitDates from a string ONLY IF it is a valid string
     */
    public static VisitDate safeOf(String dateString) {
        try {
            return of(dateString);
        } catch (IllegalValueException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract boolean isOn(LocalDate date);

    /**
     * Returns data string to store in the data files, which are parseable by AddressMe
     */
    public abstract String toDataString();

    /**
     * Returns string to order dates to compare in chronological order
     */
    public abstract String toSortString();
}
