package seedu.address.model.util;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.location.Location;
import seedu.address.model.location.dates.VisitDate;
import seedu.address.model.tag.Tag;

public class SampleDataUtilTest {

    @Test
    public void getSampleLocations_returnsNonEmptyArray() {
        Location[] locations = SampleDataUtil.getSampleLocations();
        assertTrue(locations.length > 0);
    }

    @Test
    public void getSampleLocations_firstAndLastContainNotes() {
        Location[] locations = SampleDataUtil.getSampleLocations();

        // first location has note
        assertFalse(locations[0].getNotes().isEmpty());

        // last location has note
        assertFalse(locations[locations.length - 1].getNotes().isEmpty());
    }

    @Test
    public void getSampleLocations_middleLocationsHaveEmptyNotes() {
        Location[] locations = SampleDataUtil.getSampleLocations();

        // pick a middle location (no notes)
        assertTrue(locations[1].getNotes().isEmpty());
    }

    @Test
    public void getSampleAddressBook_containsSampleLocations() {
        ReadOnlyAddressBook addressBook = SampleDataUtil.getSampleAddressBook();

        assertFalse(addressBook.getLocationList().isEmpty());
    }

    @Test
    public void getTagSet_validStrings_returnsCorrectSize() {
        Set<Tag> tags = SampleDataUtil.getTagSet("a", "b", "c");
        assertEquals(3, tags.size());
    }

    @Test
    public void getVisitDateSet_validDates_returnsCorrectSize() {
        Set<VisitDate> dates = SampleDataUtil.getVisitDateSet("2026-03-12", "2026-04-12");
        assertEquals(2, dates.size());
    }

    @Test
    public void getNoteMap_validInput_returnsCorrectMap() {
        Map<VisitDate, String> notes =
                SampleDataUtil.getNoteMap("2026-03-24", "Test note");

        assertEquals(1, notes.size());
        assertTrue(notes.containsValue("Test note"));
    }
}
