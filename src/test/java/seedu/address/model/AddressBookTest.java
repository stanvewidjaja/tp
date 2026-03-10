package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalLocations.ALICE;
import static seedu.address.testutil.TypicalLocations.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.location.Location;
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
        Location editedAlice = new LocationBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
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
        Location editedAlice = new LocationBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(addressBook.hasLocation(editedAlice));
    }

    @Test
    public void getLocationList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getLocationList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName() + "{locations=" + addressBook.getLocationList() + "}";
        assertEquals(expected, addressBook.toString());
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
    }

}
