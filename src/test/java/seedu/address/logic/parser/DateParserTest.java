package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;

public class DateParserTest {
    // ---------- With year date tests ----------
    @Test
    void fullDateWithYear_correctFormat() throws IllegalValueException {
        LocalDate expectedDate = LocalDate.of(2026, 12, 31);
        assertEquals(expectedDate, DateParser.parse("31-12-2026"));
        assertEquals(expectedDate, DateParser.parse("2026-12-31"));
        assertEquals(expectedDate, DateParser.parse("31/12/2026"));
        assertEquals(expectedDate, DateParser.parse("2026/12/31"));

        assertEquals(expectedDate, DateParser.parse("31-12-26"));
        assertEquals(expectedDate, DateParser.parse("31/12/26"));
    }

    @Test
    void shortDateWithYear_correctFormat() throws IllegalValueException {
        LocalDate expectedDate = LocalDate.of(2026, 9, 3);
        assertEquals(expectedDate, DateParser.parse("3-9-2026"));
        assertEquals(expectedDate, DateParser.parse("2026-9-3"));
        assertEquals(expectedDate, DateParser.parse("3/9/2026"));
        assertEquals(expectedDate, DateParser.parse("2026/9/3"));

        assertEquals(expectedDate, DateParser.parse("3-9-26"));
        assertEquals(expectedDate, DateParser.parse("3/9/26"));
    }

    @Test
    void mixedDateWithYear_correctFormat() throws IllegalValueException {
        LocalDate expectedDate = LocalDate.of(2026, 9, 3);
        assertEquals(expectedDate, DateParser.parse("03-9-2026"));
        assertEquals(expectedDate, DateParser.parse("2026-09-3"));
        assertEquals(expectedDate, DateParser.parse("3/09/2026"));
        assertEquals(expectedDate, DateParser.parse("2026/9/03"));

        assertEquals(expectedDate, DateParser.parse("03-9-26"));
        assertEquals(expectedDate, DateParser.parse("3/09/26"));
    }

    // ---------- Partial date tests ----------
    @Test
    void testMonthDate_expectThisYear() {
        LocalDate today = LocalDate.of(2026, 5, 5);

        LocalDate expected = LocalDate.of(2026, 6, 7);
        assertEquals(expected,
                DateParser.monthDayToDate(today, MonthDay.of(6, 7)));

        expected = LocalDate.of(2026, 12, 31);
        assertEquals(expected,
                DateParser.monthDayToDate(today, MonthDay.of(12, 31)));
    }

    @Test
    void testMonthDate_expectNextYear() {
        LocalDate today = LocalDate.of(2026, 12, 2);

        LocalDate expected = LocalDate.of(2027, 2, 20);
        assertEquals(expected,
                DateParser.monthDayToDate(today, MonthDay.of(2, 20)));

        expected = LocalDate.of(2027, 12, 1);
        assertEquals(expected,
                DateParser.monthDayToDate(today, MonthDay.of(12, 1)));
    }

    @Test
    void testMonthDate_sameDay() {
        // boundary value test
        LocalDate today = LocalDate.of(2026, 12, 2);

        assertEquals(today,
                DateParser.monthDayToDate(today, MonthDay.of(12, 2)));
    }

    @Test
    void testMonthDateFormat() throws IllegalValueException {
        LocalDate today = LocalDate.now();

        String slashInput = today.format(DateTimeFormatter.ofPattern("d/M"));
        assertEquals(today, DateParser.parse(slashInput));

        String dashInput = today.format(DateTimeFormatter.ofPattern("d-M"));
        assertEquals(today, DateParser.parse(dashInput));

        String slashInputFull = today.format(DateTimeFormatter.ofPattern("dd/MM"));
        assertEquals(today, DateParser.parse(slashInputFull));

        String dashInputFull = today.format(DateTimeFormatter.ofPattern("dd-MM"));
        assertEquals(today, DateParser.parse(dashInputFull));
    }

    @Test
    void testTodayFormat() throws IllegalValueException {
        LocalDate today = LocalDate.now();

        assertEquals(today, DateParser.parse("today"));
        assertEquals(today, DateParser.parse("Today"));
        assertEquals(today, DateParser.parse("toDAY"));
    }

    // ---------- Days of Week tests ----------
    @Test
    void testDayOfWeek_parseSuccess() throws IllegalValueException {
        // test short forms
        assertEquals(DayOfWeek.MONDAY, DateParser.parseDayOfWeek("mon"));
        assertEquals(DayOfWeek.TUESDAY, DateParser.parseDayOfWeek("TuE"));
        assertEquals(DayOfWeek.WEDNESDAY, DateParser.parseDayOfWeek("WED"));
        assertEquals(DayOfWeek.THURSDAY, DateParser.parseDayOfWeek("thu"));
        // test long form
        assertEquals(DayOfWeek.FRIDAY, DateParser.parseDayOfWeek("fRiDaY"));
        assertEquals(DayOfWeek.SATURDAY, DateParser.parseDayOfWeek("SATURDAY"));
        assertEquals(DayOfWeek.SUNDAY, DateParser.parseDayOfWeek("   sunday   "));
    }

    @Test
    void testDayOfWeek_parseFailure() throws IllegalValueException {
        assertThrows(IllegalValueException.class, ()->DateParser.parseDayOfWeek(""));
        assertThrows(IllegalValueException.class, ()->DateParser.parseDayOfWeek("    "));
        assertThrows(IllegalValueException.class, ()->DateParser.parseDayOfWeek("thur"));
        assertThrows(IllegalValueException.class, ()->DateParser.parseDayOfWeek("123"));
        assertThrows(IllegalValueException.class, ()->DateParser.parseDayOfWeek("Fridaynight"));
    }


    @Test
    void testDayShortForm_everyDayOfWeek() throws IllegalValueException {
        LocalDate today = LocalDate.now();

        String[] shortDays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        String[] fullDays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        DayOfWeek[] dow = {
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY};

        for (int i = 0; i < dow.length; i++) {
            LocalDate expected = today.with(TemporalAdjusters.nextOrSame(dow[i]));

            LocalDate parsedShort = DateParser.parse(shortDays[i]);
            LocalDate parsedFull = DateParser.parse(fullDays[i]);

            assertEquals(expected, parsedShort, "Failed parsing short day: " + shortDays[i]);
            assertEquals(expected, parsedFull, "Failed parsing full day: " + fullDays[i]);
        }
    }

    @Test
    void testeveryDayOfWeek_unevenCaps() throws IllegalValueException {
        LocalDate today = LocalDate.now();

        String[] shortDays = {"mon", "Tue", "wed", "Thu", "FrI", "SAT", "Sun"};
        String[] fullDays = {"Monday", "TUESDAY", "WednesDay", "thursday", "Friday", "Saturday", "Sunday"};
        DayOfWeek[] dow = {
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY};

        for (int i = 0; i < dow.length; i++) {
            LocalDate expected = today.with(TemporalAdjusters.nextOrSame(dow[i]));

            LocalDate parsedShort = DateParser.parse(shortDays[i]);
            LocalDate parsedFull = DateParser.parse(fullDays[i]);

            assertEquals(expected, parsedShort, "Failed parsing short day: " + shortDays[i]);
            assertEquals(expected, parsedFull, "Failed parsing full day: " + fullDays[i]);
        }
    }

    @Test
    void testEmptyString() {
        assertThrows(IllegalValueException.class, ()->DateParser.parse(""));
    }

    @Test
    void testInvalidFormats() {
        assertThrows(IllegalValueException.class, ()->DateParser.parse("notadate"));

        assertThrows(IllegalValueException.class, ()->DateParser.parse("1234123"));
    }

    @Test
    void testInvalidDates() {
        assertThrows(IllegalValueException.class, ()->DateParser.parse("32/01/2026"));
        assertThrows(IllegalValueException.class, ()->DateParser.parse("12-30-2026"));
    }

    @Test
    void testPrettyStringConversion() {
        assertEquals("Tuesday, 5 May 2026", DateParser.dateToPrettyString(LocalDate.of(2026, 5, 5)));
        assertEquals("Saturday, 31 Oct 2026", DateParser.dateToPrettyString(LocalDate.of(2026, 10, 31)));
        assertEquals("Sunday, 14 Mar 2027", DateParser.dateToPrettyString(LocalDate.of(2027, 3, 14)));
    }

}
