package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalLocations.ALICE;
import static seedu.address.testutil.TypicalLocations.ZERO;
import static seedu.address.testutil.TypicalLocations.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.location.Location;
import seedu.address.model.location.NoteContent;
import seedu.address.model.location.dates.VisitDate;
import seedu.address.model.location.exceptions.DuplicateLocationException;
import seedu.address.testutil.LocationBuilder;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getLocationList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicateLocations_throwsDuplicateLocationException() {
        // Two locations with the same identity fields
        Location editedAlice = new LocationBuilder(ALICE).withName(VALID_NAME_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Location> newLocations = Arrays.asList(ALICE, editedAlice);
        AddressBookStub newData = new AddressBookStub(newLocations);

        assertThrows(DuplicateLocationException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasLocation_nullLocation_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasLocation(null));
    }

    @Test
    public void hasLocation_locationNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasLocation(ALICE));
    }

    @Test
    public void hasLocation_locationInAddressBook_returnsTrue() {
        addressBook.addLocation(ALICE);
        assertTrue(addressBook.hasLocation(ALICE));
    }

    @Test
    public void hasLocation_locationWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addLocation(ALICE);
        Location editedAlice = new LocationBuilder(ALICE).withName(VALID_NAME_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(addressBook.hasLocation(editedAlice));
    }

    @Test
    public void hasLocation_locationWithSameNameOnlyButDifferentAddress_returnsFalse() {
        addressBook.addLocation(ALICE);
        Location editedAlice = new LocationBuilder(ALICE)
                .withAddress("77 Sunset Way")
                .withPostalCode("765432")
                .build();
        assertFalse(addressBook.hasLocation(editedAlice));
    }

    @Test
    public void hasLocation_locationWithoutAddressSameNameDifferentCase_returnsTrue() {
        addressBook.addLocation(ZERO);
        Location editedZero = new LocationBuilder(ZERO)
                .withName(ZERO.getName().fullName.toLowerCase())
                .build();
        assertTrue(addressBook.hasLocation(editedZero));
    }

    @Test
    public void getLocationList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getLocationList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName() + "{locations=" + addressBook.getLocationList()
                + ", notes=" + addressBook.getNoteMap() + "}";
        assertEquals(expected, addressBook.toString());
    }

    @Test
    public void removeNote_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.removeNote(null));
    }

    @Test
    public void removeNote_existingDate_removesNote() throws IllegalValueException {
        VisitDate date = VisitDate.of("2026-03-24");
        NoteContent note = new NoteContent("Test Note");
        addressBook.setNote(date, note);
        assertTrue(addressBook.getNoteMap().containsKey(date));
        addressBook.removeNote(date);
        assertFalse(addressBook.getNoteMap().containsKey(date));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        assertTrue(addressBook.equals(addressBook));
    }

    @Test
    public void equals_notAddressBook_returnsFalse() {
        assertFalse(addressBook.equals(1));
    }

    @Test
    public void hashCodeMethod() {
        assertEquals(addressBook.hashCode(), addressBook.hashCode());
    }

    /**
     * A stub ReadOnlyAddressBook whose locations list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Location> locations = FXCollections.observableArrayList();

        AddressBookStub(Collection<Location> locations) {
            this.locations.setAll(locations);
        }

        @Override
        public ObservableList<Location> getLocationList() {
            return locations;
        }

        @Override
        public Map<VisitDate, NoteContent> getNoteMap() {
            return Collections.emptyMap();
        }
    }

    @Test
    public void setNote_validDate_addsNote() throws Exception {
        VisitDate date = VisitDate.of("2026-03-24");
        NoteContent note = new NoteContent("Test Note");

        addressBook.setNote(date, note);

        assertEquals(Map.of(date, note), addressBook.getNoteMap());
    }

    @Test
    public void setNote_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                addressBook.setNote(null, new NoteContent("Note")));
    }

    @Test
    public void setNote_nullNote_throwsNullPointerException() throws Exception {
        VisitDate date = VisitDate.of("2026-03-24");

        assertThrows(NullPointerException.class, () ->
                addressBook.setNote(date, null));
    }

    @Test
    public void removeNote_missingDate_noChange() throws Exception {
        VisitDate existingDate = VisitDate.of("2026-03-24");
        VisitDate missingDate = VisitDate.of("2026-03-25");
        NoteContent note = new NoteContent("Test Note");
        addressBook.setNote(existingDate, note);

        addressBook.removeNote(missingDate);

        assertEquals(Map.of(existingDate, note), addressBook.getNoteMap());
    }

    @Test
    public void resetData_preservesNotes() throws Exception {
        AddressBook newData = new AddressBook();
        VisitDate date = VisitDate.of("2026-03-24");
        NoteContent note = new NoteContent("Test Note");

        newData.setNote(date, note);

        addressBook.resetData(newData);

        assertEquals(Map.of(date, note), addressBook.getNoteMap());
    }
}
