package seedu.address.model.location.date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.model.location.dates.EveryDayDate;


public class EveryDayDateTest {
    // no constructor tests (private constructor)

    // ---- isOn tests ----
    @Test
    void testIsOn_true() {
        EveryDayDate testDate = EveryDayDate.EVERYDAY_DATE;
        // should always return true
        assertTrue(testDate.isOn(LocalDate.now()));
        assertTrue(testDate.isOn(LocalDate.of(400, 2, 13)));
        assertTrue(testDate.isOn(LocalDate.of(100000, 1, 1)));
    }

    @Test
    void testIsOn_invalid() {
        EveryDayDate testDate = EveryDayDate.EVERYDAY_DATE;
        assertThrows(NullPointerException.class, ()->testDate.isOn(null));
    }

    // ---- toDataString tests ----
    @Test
    void testToDataString() {
        EveryDayDate testDate = EveryDayDate.EVERYDAY_DATE;
        assertNotNull(testDate.toDataString());
        assertEquals("everyday", testDate.toDataString());
    }

    // ---- toString tests ----
    @Test
    void testToString() {
        EveryDayDate testDate = EveryDayDate.EVERYDAY_DATE;
        assertNotNull(testDate.toString());
        assertEquals("Everyday", testDate.toString());
    }

    // ---- equals tests ----
    @Test
    void testEquals() {
        EveryDayDate testDate = EveryDayDate.EVERYDAY_DATE;
        assertNotEquals(null, testDate);

        EveryDayDate testDateB = EveryDayDate.EVERYDAY_DATE;
        assertEquals(testDate, testDateB);
    }

    @Test
    void testEquals_differentType() {
        EveryDayDate testDate = EveryDayDate.EVERYDAY_DATE;
        assertNotEquals("some string", testDate);
    }
}
