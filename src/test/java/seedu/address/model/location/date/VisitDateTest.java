package seedu.address.model.location.date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.location.dates.EveryDayDate;
import seedu.address.model.location.dates.OneTimeDate;
import seedu.address.model.location.dates.VisitDate;
import seedu.address.model.location.dates.WeeklyRecurringDate;
import seedu.address.model.location.dates.YearlyRecurringDate;

public class VisitDateTest {
    // Factory method tests

    @Test
    void of_everyday_returnsEveryDayDate() throws IllegalValueException {
        VisitDate result = VisitDate.of("everyday");
        assertTrue(result instanceof EveryDayDate);

        result = VisitDate.of("Every Day");
        assertTrue(result instanceof EveryDayDate);

        result = VisitDate.of("every DAY");
        assertTrue(result instanceof EveryDayDate);
    }

    // ---- PREFIX: e- ----
    @Test
    void of_ePrefixWithMonthDay_returnsYearlyRecurringDate() throws IllegalValueException {
        VisitDate result = VisitDate.of("e-25-12"); // Christmas
        assertTrue(result instanceof YearlyRecurringDate);
        assertEquals(new YearlyRecurringDate(MonthDay.of(12, 25)), result);
    }

    @Test
    void of_ePrefixWithDayOfWeek_returnsWeeklyRecurringDate() throws IllegalValueException {
        VisitDate result = VisitDate.of("e-MONDAY");
        assertTrue(result instanceof WeeklyRecurringDate);
        assertEquals(new WeeklyRecurringDate(DayOfWeek.MONDAY), result);
    }

    // ---- PREFIX: "every " ----
    @Test
    void of_everyMonthDay_returnsYearlyRecurringDate() throws IllegalValueException {
        VisitDate result = VisitDate.of("every 25/12");
        assertTrue(result instanceof YearlyRecurringDate);
        assertEquals(new YearlyRecurringDate(MonthDay.of(12, 25)), result);
    }

    @Test
    void of_everyDayOfWeek_returnsWeeklyRecurringDate() throws IllegalValueException {
        VisitDate result = VisitDate.of("every Tue");
        assertTrue(result instanceof WeeklyRecurringDate);
        assertEquals(new WeeklyRecurringDate(DayOfWeek.TUESDAY), result);
    }

    // ---- ONE-TIME cases ----
    @Test
    void of_localDate_returnsOneTimeDate() throws IllegalValueException {
        VisitDate result = VisitDate.of("2026-04-01");
        assertTrue(result instanceof OneTimeDate);
        assertEquals(new OneTimeDate(LocalDate.of(2026, 4, 1)), result);
    }

    @Test
    void of_monthDay_returnsOneTimeDate() throws IllegalValueException {
        VisitDate result = VisitDate.of("3/4");
        assertTrue(result instanceof OneTimeDate);
    }

    @Test
    void of_dayOfWeek_returnsOneTimeDate() throws IllegalValueException {
        VisitDate result = VisitDate.of("Thu");
        assertTrue(result instanceof OneTimeDate);
    }

    // ---- INVALID INPUTS ----
    @Test
    void of_throwsException() {
        assertThrows(NullPointerException.class, () -> VisitDate.of(null));
        assertThrows(IllegalValueException.class, () -> VisitDate.of(""));
        assertThrows(IllegalValueException.class, () -> VisitDate.of("   "));
        assertThrows(IllegalValueException.class, () -> VisitDate.of("not a date"));
    }

    @Test
    void of_invalidRecurring_throwsException() {
        assertThrows(IllegalValueException.class, () -> VisitDate.of("every   "));
        assertThrows(IllegalValueException.class, () -> VisitDate.of("every 1234"));
        assertThrows(IllegalValueException.class, () -> VisitDate.of("e-  "));
        assertThrows(IllegalValueException.class, () -> VisitDate.of("e-notadate"));
    }

    @Test
    void safeOf_invalidUse_throwsException() {
        assertThrows(RuntimeException.class, () -> VisitDate.safeOf(""));
    }
}
