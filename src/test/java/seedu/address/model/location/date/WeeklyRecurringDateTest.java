package seedu.address.model.location.date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.model.location.dates.EveryDayDate;
import seedu.address.model.location.dates.VisitDate;
import seedu.address.model.location.dates.WeeklyRecurringDate;

public class WeeklyRecurringDateTest {
    // ---- constructor tests ----
    @Test
    void testInitSuccess() {
        WeeklyRecurringDate testDate = new WeeklyRecurringDate(DayOfWeek.TUESDAY);
        assertNotNull(testDate);
    }

    @Test
    void testInitFailure() {
        assertThrows(NullPointerException.class, ()->new WeeklyRecurringDate(null));
    }

    // ---- isOn tests ----
    @Test
    void testIsOn_true() {
        WeeklyRecurringDate testDate = new WeeklyRecurringDate(LocalDate.now().getDayOfWeek());
        // returns true for dates on the same day
        assertTrue(testDate.isOn(LocalDate.now()));
        assertTrue(testDate.isOn(LocalDate.now().plusDays(7)));
        assertTrue(testDate.isOn(LocalDate.now().plusDays(7 * 90)));
        assertTrue(testDate.isOn(LocalDate.now().plusDays(7 * -100)));
    }

    @Test
    void testIsOn_false() {
        WeeklyRecurringDate testDate = new WeeklyRecurringDate(LocalDate.now().getDayOfWeek());
        // returns false for dates on the different day
        assertFalse(testDate.isOn(LocalDate.now().plusDays(6)));
        assertFalse(testDate.isOn(LocalDate.now().plusDays(7 * 90 + 1)));
        assertFalse(testDate.isOn(LocalDate.now().plusDays(7 * -100 - 4)));
    }

    @Test
    void testIsOn_invalid() {
        WeeklyRecurringDate testDate = new WeeklyRecurringDate(LocalDate.now().getDayOfWeek());
        assertThrows(NullPointerException.class, ()->testDate.isOn(null));
    }

    // ---- toDataString tests ----
    @Test
    void testToDataString() {
        WeeklyRecurringDate testDate = new WeeklyRecurringDate(DayOfWeek.MONDAY);
        assertNotNull(testDate.toDataString());
        assertEquals("e-MONDAY", testDate.toDataString());
    }

    // ---- toString tests ----
    @Test
    void testToString() {
        WeeklyRecurringDate testDate = new WeeklyRecurringDate(DayOfWeek.MONDAY);
        assertNotNull(testDate.toString());
        assertEquals("Every Monday", testDate.toString());
    }

    // ---- equals tests ----
    @Test
    void testEquals() {
        WeeklyRecurringDate testDate = new WeeklyRecurringDate(DayOfWeek.MONDAY);
        assertNotEquals(null, testDate);

        // Equal if same day
        WeeklyRecurringDate testDateB = new WeeklyRecurringDate(DayOfWeek.MONDAY);
        assertEquals(testDate, testDateB);
    }

    @Test
    void testNotEquals() {
        WeeklyRecurringDate testDate = new WeeklyRecurringDate(DayOfWeek.MONDAY);

        WeeklyRecurringDate diffDate = new WeeklyRecurringDate(DayOfWeek.FRIDAY);
        assertNotEquals(testDate, diffDate);

        VisitDate diffSubclass = EveryDayDate.EVERYDAY_DATE;
        assertNotEquals(testDate, diffSubclass);
    }
}
