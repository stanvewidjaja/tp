package seedu.address.model.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalLocations.ALICE;
import static seedu.address.testutil.TypicalLocations.BOB;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.location.exceptions.DuplicateLocationException;
import seedu.address.model.location.exceptions.LocationNotFoundException;
import seedu.address.testutil.LocationBuilder;

public class UniqueLocationListTest {

    private final UniqueLocationList uniqueLocationList = new UniqueLocationList();

    @Test
    public void contains_nullLocation_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueLocationList.contains(null));
    }

    @Test
    public void contains_locationNotInList_returnsFalse() {
        assertFalse(uniqueLocationList.contains(ALICE));
    }

    @Test
    public void contains_locationInList_returnsTrue() {
        uniqueLocationList.add(ALICE);
        assertTrue(uniqueLocationList.contains(ALICE));
    }

    @Test
    public void contains_locationWithSameIdentityFieldsInList_returnsTrue() {
        uniqueLocationList.add(ALICE);
        Location editedAlice = new LocationBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(uniqueLocationList.contains(editedAlice));
    }

    @Test
    public void add_nullLocation_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueLocationList.add(null));
    }

    @Test
    public void add_duplicateLocation_throwsDuplicateLocationException() {
        uniqueLocationList.add(ALICE);
        assertThrows(DuplicateLocationException.class, () -> uniqueLocationList.add(ALICE));
    }

    @Test
    public void setLocation_nullTargetLocation_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueLocationList.setLocation(null, ALICE));
    }

    @Test
    public void setLocation_nullEditedLocation_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueLocationList.setLocation(ALICE, null));
    }

    @Test
    public void setLocation_targetLocationNotInList_throwsLocationNotFoundException() {
        assertThrows(LocationNotFoundException.class, () -> uniqueLocationList.setLocation(ALICE, ALICE));
    }

    @Test
    public void setLocation_editedLocationIsSameLocation_success() {
        uniqueLocationList.add(ALICE);
        uniqueLocationList.setLocation(ALICE, ALICE);
        UniqueLocationList expectedUniqueLocationList = new UniqueLocationList();
        expectedUniqueLocationList.add(ALICE);
        assertEquals(expectedUniqueLocationList, uniqueLocationList);
    }

    @Test
    public void setLocation_editedLocationHasSameIdentity_success() {
        uniqueLocationList.add(ALICE);
        Location editedAlice = new LocationBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        uniqueLocationList.setLocation(ALICE, editedAlice);
        UniqueLocationList expectedUniqueLocationList = new UniqueLocationList();
        expectedUniqueLocationList.add(editedAlice);
        assertEquals(expectedUniqueLocationList, uniqueLocationList);
    }

    @Test
    public void setLocation_editedLocationHasDifferentIdentity_success() {
        uniqueLocationList.add(ALICE);
        uniqueLocationList.setLocation(ALICE, BOB);
        UniqueLocationList expectedUniqueLocationList = new UniqueLocationList();
        expectedUniqueLocationList.add(BOB);
        assertEquals(expectedUniqueLocationList, uniqueLocationList);
    }

    @Test
    public void setLocation_editedLocationHasNonUniqueIdentity_throwsDuplicateLocationException() {
        uniqueLocationList.add(ALICE);
        uniqueLocationList.add(BOB);
        assertThrows(DuplicateLocationException.class, () -> uniqueLocationList.setLocation(ALICE, BOB));
    }

    @Test
    public void remove_nullLocation_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueLocationList.remove(null));
    }

    @Test
    public void remove_locationDoesNotExist_throwsLocationNotFoundException() {
        assertThrows(LocationNotFoundException.class, () -> uniqueLocationList.remove(ALICE));
    }

    @Test
    public void remove_existingLocation_removesLocation() {
        uniqueLocationList.add(ALICE);
        uniqueLocationList.remove(ALICE);
        UniqueLocationList expectedUniqueLocationList = new UniqueLocationList();
        assertEquals(expectedUniqueLocationList, uniqueLocationList);
    }

    @Test
    public void setLocations_nullUniqueLocationList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueLocationList.setLocations((UniqueLocationList) null));
    }

    @Test
    public void setLocations_uniqueLocationList_replacesOwnListWithProvidedUniqueLocationList() {
        uniqueLocationList.add(ALICE);
        UniqueLocationList expectedUniqueLocationList = new UniqueLocationList();
        expectedUniqueLocationList.add(BOB);
        uniqueLocationList.setLocations(expectedUniqueLocationList);
        assertEquals(expectedUniqueLocationList, uniqueLocationList);
    }

    @Test
    public void setLocations_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueLocationList.setLocations((List<Location>) null));
    }

    @Test
    public void setLocations_list_replacesOwnListWithProvidedList() {
        uniqueLocationList.add(ALICE);
        List<Location> locationList = Collections.singletonList(BOB);
        uniqueLocationList.setLocations(locationList);
        UniqueLocationList expectedUniqueLocationList = new UniqueLocationList();
        expectedUniqueLocationList.add(BOB);
        assertEquals(expectedUniqueLocationList, uniqueLocationList);
    }

    @Test
    public void setLocations_listWithDuplicateLocations_throwsDuplicateLocationException() {
        List<Location> listWithDuplicateLocations = Arrays.asList(ALICE, ALICE);
        assertThrows(
                DuplicateLocationException.class, () -> uniqueLocationList.setLocations(listWithDuplicateLocations));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniqueLocationList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueLocationList.asUnmodifiableObservableList().toString(), uniqueLocationList.toString());
    }

    @Test
    public void contains_locationWithSameNameDifferentCase_returnsTrue() {
        uniqueLocationList.add(ALICE);

        Location editedAlice = new LocationBuilder(ALICE)
                .withName(ALICE.getName().fullName.toLowerCase()) // or "sunrise cafe"
                .build();

        assertTrue(uniqueLocationList.contains(editedAlice));
    }
}
