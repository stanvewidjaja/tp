package seedu.address.logic.parser;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a static utility class that can convert between Strings and LocalDate
 */
public class DateParser {
    public static final String MESSAGE_WRONG_DATE_FORMAT = "Not a valid date! Try using these formats:\n"
            + "yyyy-MM-dd, yyyy/MM/dd, d-M-yyyy, d/M/yyyy,\n"
            + "d-M-yy, d/M/yy, d-M, d/M, day of the week (e.g. Thu or Thursday).";

    private static final List<DateTimeFormatter> DATE_FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ofPattern("yyyy-M-d"),
            DateTimeFormatter.ofPattern("d-M-yyyy"),
            DateTimeFormatter.ofPattern("yyyy/M/d"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("d/M/yy"),
            DateTimeFormatter.ofPattern("d-M-yy")
    );

    private static final List<DateTimeFormatter> MONTH_DAY_FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("dd-MM"),
            DateTimeFormatter.ofPattern("dd/MM"),
            DateTimeFormatter.ofPattern("d/M"),
            DateTimeFormatter.ofPattern("d-M")
    );

    private static final List<DateTimeFormatter> DAY_OF_WEEK_FORMATTER = List.of(
            DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH)
    );

    /**
     * Reads a string and converts it to a Date object
     *
     * @param input String to be parsed into a date
     * @return a LocalDate representation of the input
     * @throws IllegalValueException if the string cannot fit into any DateTimeFormat
     */
    public static LocalDate parse(String input) throws IllegalValueException {
        input = input.trim();

        if (input.isEmpty()) {
            throw new IllegalValueException(MESSAGE_WRONG_DATE_FORMAT);
        }

        // try to match date formatter with year
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                //ignore since exception is thrown below
            }
        }

        // try to match date formatter with no year
        LocalDate today = LocalDate.now();
        for (DateTimeFormatter formatter : MONTH_DAY_FORMATTERS) {
            try {
                MonthDay parsed = MonthDay.parse(input, formatter);
                return parseMonthDay(today, parsed);
            } catch (DateTimeParseException e) {
                //ignore since exception is thrown below
            }
        }

        // capitalize just the first letter (Assuming it is day of the week)
        input = formatDayOfWeek(input);

        // try to match day of the week e.g. Tue, Thur, Friday
        for (DateTimeFormatter formatter : DAY_OF_WEEK_FORMATTER) {
            try {
                DayOfWeek day = DayOfWeek.from(formatter.parse(input));
                return today.with(TemporalAdjusters.nextOrSame(day));
            } catch (DateTimeParseException e) {
                //ignore since exception is thrown below
            }
        }

        throw new IllegalValueException(MESSAGE_WRONG_DATE_FORMAT);
    }

    /**
     * Converts a LocalDate object back into a string to be stored on in data files
     *
     * @param date LocalDate object to be converted
     * @return a string representation of date/datetime
     */
    public static String dateToDataString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * Converts a LocalDate into the output format of dates to users
     *
     * @param date LocalDate to convert
     * @return a String to be printed to user that looks good
     */
    public static String dateToPrettyString(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DateTimeFormatter.ofPattern("EEEE, d MMM yyyy"));
    }

    /**
     * Converts a day and month to LocalDate by taking the nearest upcoming date
     *
     * @param today      LocalDate representing the current date
     * @param dateParsed MonthDay to be converted
     * @return The date with year adjusted
     */
    public static LocalDate parseMonthDay(LocalDate today, MonthDay dateParsed) {
        LocalDate dateThisYear = dateParsed.atYear(today.getYear());

        // the date has passed, so should refer to next year
        if (dateThisYear.isBefore(today)) {
            return dateThisYear.plusYears(1);
        }
        // date has not passed yet
        return dateThisYear;
    }

    /**
     * Converts a string to all lowercase except the first character, which is capitalized
     */
    private static String formatDayOfWeek(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
