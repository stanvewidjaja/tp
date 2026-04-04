package seedu.address.model.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalLocations.ALICE;
import static seedu.address.testutil.TypicalLocations.BOB;
import static seedu.address.testutil.TypicalLocations.ZERO;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.LocationBuilder;

public class LocationTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Location location = new LocationBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> location.getTags().remove(0));
    }

    @Test
    public void getVisitDates_modifyList_throwsUnsupportedOperationException() {
        Location location = new LocationBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> location.getVisitDates().remove(0));
    }

    @Test
    public void occursOn() {
        assertTrue(ALICE.occursOn(LocalDate.parse("2026-01-07")));
        assertTrue(ALICE.occursOn(LocalDate.parse("2026-01-10")));
        assertFalse(ALICE.occursOn(LocalDate.parse("2026-01-08")));
    }

    @Test
    public void isSameLocation() {
        assertTrue(ALICE.isSameLocation(ALICE));
        assertFalse(ALICE.isSameLocation(null));

        Location editedAlice = new LocationBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSameLocation(editedAlice));

        editedAlice = new LocationBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSameLocation(editedAlice));

        Location editedBob = new LocationBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.isSameLocation(editedBob));

        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new LocationBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSameLocation(editedBob));
    }

    @Test
    public void equals() {
        Location aliceCopy = new LocationBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        assertTrue(ALICE.equals(ALICE));
        assertFalse(ALICE.equals(null));
        assertFalse(ALICE.equals(5));
        assertFalse(ALICE.equals(BOB));

        Location editedAlice = new LocationBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = new LocationBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = new LocationBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = new LocationBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = new LocationBuilder(ALICE).withPostalCode("999999").build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = new LocationBuilder(ALICE).withVisitDates("2026-02-20").build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = new LocationBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void testAttributeStrings() {

        // test strings with empty optionals
        assertEquals("-", ZERO.getAddressString());
        assertEquals("-", ZERO.getEmailString());
        assertEquals("-", ZERO.getPostalString());
        assertEquals("-", ZERO.getPhoneString());

        // test strings with non-empty optionals
        assertEquals(ALICE.getAddress().map(Address::toString).orElse("fail"), ALICE.getAddressString());
        assertEquals(ALICE.getEmail().map(Email::toString).orElse("fail"), ALICE.getEmailString());
        assertEquals(ALICE.getPostalCode().map(PostalCode::toString).orElse("fail"), ALICE.getPostalString());
        assertEquals(ALICE.getPhone().map(Phone::toString).orElse("fail"), ALICE.getPhoneString());
    }

    @Test
    public void toStringMethod() {
        String expected = Location.class.getCanonicalName()
                + "{name=" + ALICE.getName()
                + ", phone=" + ALICE.getPhoneString()
                + ", email=" + ALICE.getEmailString()
                + ", address=" + ALICE.getAddressString()
                + ", postalCode=" + ALICE.getPostalString()
                + ", visitDates=" + ALICE.getVisitDates()
                + ", tags=" + ALICE.getTags()
                + ", notes=" + ALICE.getNotes() + "}";
        assertEquals(expected, ALICE.toString());
    }


    @Test
    public void toStringMethod_missingOptionalFields() {
        Location location = new LocationBuilder()
                .withName("Test Place")
                .withoutPhone()
                .withoutEmail()
                .withoutAddress()
                .withoutPostalCode()
                .withoutVisitDates()
                .withTags()
                .build();

        String expected = Location.class.getCanonicalName()
                + "{name=" + location.getName()
                + ", phone=-"
                + ", email=-"
                + ", address=-"
                + ", postalCode=-"
                + ", visitDates=" + location.getVisitDates()
                + ", tags=" + location.getTags()
                + ", notes=" + location.getNotes() + "}";
        assertEquals(expected, location.toString());
    }
}
