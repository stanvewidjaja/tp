package seedu.address.model.location.date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;
import java.time.MonthDay;

import org.junit.jupiter.api.Test;

import seedu.address.model.location.dates.EveryDayDate;
import seedu.address.model.location.dates.VisitDate;
import seedu.address.model.location.dates.YearlyRecurringDate;

public class YearlyRecurringDateTest {
    // ---- constructor tests ----
    @Test
    void testInitSuccess() {
        YearlyRecurringDate testDate = new YearlyRecurringDate(MonthDay.of(7, 26));
        assertNotNull(testDate);
    }

    @Test
    void testInitFailure() {
        assertThrows(NullPointerException.class, ()->new YearlyRecurringDate(null));
    }

    // ---- isOn tests ----
    @Test
    void testIsOn_true() {
        YearlyRecurringDate testDate = new YearlyRecurringDate(MonthDay.now());
        // returns true for dates on the same day
        assertTrue(testDate.isOn(LocalDate.now()));
        assertTrue(testDate.isOn(LocalDate.now().plusYears(1000005)));
        assertTrue(testDate.isOn(LocalDate.now().plusYears(57)));
        assertTrue(testDate.isOn(LocalDate.now().plusYears(-89)));
    }

    @Test
    void testIsOn_false() {
        YearlyRecurringDate testDate = new YearlyRecurringDate(MonthDay.now());
        // returns false for dates on the different day
        assertFalse(testDate.isOn(LocalDate.now().plusDays(1)));
        assertFalse(testDate.isOn(LocalDate.now().plusYears(1000).plusDays(360)));
        assertFalse(testDate.isOn(LocalDate.now().plusYears(-1034).plusDays(-90)));
    }

    @Test
    void testIsOn_invalid() {
        YearlyRecurringDate testDate = new YearlyRecurringDate(MonthDay.now());
        assertThrows(NullPointerException.class, ()->testDate.isOn(null));
    }

    // ---- toDataString tests ----
    @Test
    void testToDataString() {
        YearlyRecurringDate testDate = new YearlyRecurringDate(MonthDay.of(6, 15));
        assertNotNull(testDate.toDataString());
        assertEquals("e-15/06", testDate.toDataString());
    }

    // ---- toString tests ----
    @Test
    void testToString() {
        YearlyRecurringDate testDate = new YearlyRecurringDate(MonthDay.of(7, 26));
        assertNotNull(testDate.toString());
        assertEquals("Every 26 Jul", testDate.toString());
    }

    // ---- equals tests ----
    @Test
    void testEquals() {
        YearlyRecurringDate testDate = new YearlyRecurringDate(MonthDay.of(7, 26));
        assertNotEquals(null, testDate);

        // Equal if same day
        YearlyRecurringDate sameDate = new YearlyRecurringDate(MonthDay.of(7, 26));
        assertEquals(testDate, sameDate);
    }

    @Test
    void testNotEquals() {
        YearlyRecurringDate testDate = new YearlyRecurringDate(MonthDay.of(8, 13));

        YearlyRecurringDate diffDate = new YearlyRecurringDate(MonthDay.of(8, 14));
        assertNotEquals(testDate, diffDate);

        VisitDate diffSubclass = EveryDayDate.EVERYDAY_DATE;
        assertNotEquals(testDate, diffSubclass);
    }
}
