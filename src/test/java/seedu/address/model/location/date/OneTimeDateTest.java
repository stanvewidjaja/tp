package seedu.address.model.location.date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.model.location.dates.EveryDayDate;
import seedu.address.model.location.dates.OneTimeDate;
import seedu.address.model.location.dates.VisitDate;

public class OneTimeDateTest {
    // ---- constructor tests ----
    @Test
    void testInitSuccess() {
        OneTimeDate testDate = new OneTimeDate(LocalDate.of(2026, 7, 26));
        assertNotNull(testDate);
    }

    @Test
    void testInitFailure() {
        assertThrows(NullPointerException.class, ()->new OneTimeDate(null));
    }

    // ---- isOn tests ----
    @Test
    void testIsOn_true() {
        OneTimeDate testDate = new OneTimeDate(LocalDate.now());
        // returns true for same dates
        assertTrue(testDate.isOn(LocalDate.now()));
    }

    @Test
    void testIsOn_false() {
        OneTimeDate testDate = new OneTimeDate(LocalDate.now());
        // returns false for dates on the different day
        assertFalse(testDate.isOn(LocalDate.now().plusDays(1)));
        assertFalse(testDate.isOn(LocalDate.now().plusDays(-1)));
        assertFalse(testDate.isOn(LocalDate.now().plusYears(1)));
        assertFalse(testDate.isOn(LocalDate.now().plusYears(-1)));
    }

    @Test
    void testIsOn_invalid() {
        OneTimeDate testDate = new OneTimeDate(LocalDate.now());
        assertThrows(NullPointerException.class, ()->testDate.isOn(null));
    }

    // ---- toDataString tests ----
    @Test
    void testToDataString() {
        OneTimeDate testDate = new OneTimeDate(LocalDate.of(2026, 6, 15));
        assertNotNull(testDate.toDataString());
        assertEquals("2026-06-15", testDate.toDataString());
    }

    // ---- toString tests ----
    @Test
    void testToString() {
        OneTimeDate testDate = new OneTimeDate(LocalDate.of(2000, 7, 26));
        assertNotNull(testDate.toString());
        assertEquals("26 Jul 00", testDate.toString());
    }

    // ---- equals tests ----
    @Test
    void testEquals() {
        OneTimeDate testDate = new OneTimeDate(LocalDate.of(2026, 7, 26));
        assertNotEquals(null, testDate);

        // Equal if same day
        OneTimeDate sameDate = new OneTimeDate(LocalDate.of(2026, 7, 26));
        assertEquals(testDate, sameDate);
    }

    @Test
    void testNotEquals() {
        OneTimeDate testDate = new OneTimeDate(LocalDate.of(2026, 8, 13));

        OneTimeDate diffDate = new OneTimeDate(LocalDate.of(2027, 8, 13));
        assertNotEquals(testDate, diffDate);

        VisitDate diffSubclass = EveryDayDate.EVERYDAY_DATE;
        assertNotEquals(testDate, diffSubclass);
    }
}
