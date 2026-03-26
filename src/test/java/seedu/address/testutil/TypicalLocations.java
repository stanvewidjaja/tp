package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_POSTAL_CODE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_POSTAL_CODE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.location.Location;

/**
 * A utility class containing a list of {@code Location} objects to be used in tests.
 */
public class TypicalLocations {

    public static final Location ALICE = new LocationBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111")
            .withEmail("alice@example.com")
            .withPhone("94351253")
            .withPostalCode("640123")
            .withVisitDates("2026-01-07", "2026-01-10")
            .withTags("friends").build();
    public static final Location BENSON = new LocationBuilder().withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withEmail("johnd@example.com")
            .withPhone("98765432")
            .withPostalCode("540103")
            .withVisitDates("2026-03-15")
            .withTags("owesMoney", "friends").build();
    public static final Location CARL = new LocationBuilder().withName("Carl Kurz")
            .withPhone("95352563")
            .withEmail("heinz@example.com")
            .withAddress("wall street")
            .withPostalCode("452398")
            .withVisitDate("2026-07-13").build();
    public static final Location DANIEL = new LocationBuilder().withName("Daniel Meier")
            .withPhone("87652533")
            .withEmail("cornelia@example.com")
            .withAddress("10th street")
            .withPostalCode("356895")
            .withVisitDates("2026-08-10")
            .withTags("friends").build();
    public static final Location ELLE = new LocationBuilder().withName("Elle Meyer")
            .withPhone("9482224")
            .withEmail("werner@example.com")
            .withAddress("michegan ave")
            .withPostalCode("236485")
            .withVisitDates("2026-05-17").build();
    public static final Location FIONA = new LocationBuilder().withName("Fiona Kunz")
            .withPhone("9482427")
            .withEmail("lydia@example.com")
            .withAddress("little tokyo")
            .withPostalCode("782365")
            .withVisitDates("2026-10-17").build();
    public static final Location GEORGE = new LocationBuilder().withName("George Best")
            .withPhone("9482442")
            .withEmail("anna@example.com")
            .withAddress("4th street")
            .withPostalCode("128567")
            .withVisitDates("2026-12-31").build();

    // Manually added
    public static final Location HOON = new LocationBuilder().withName("Hoon Meier").withPhone("8482424")
            .withEmail("stefan@example.com").withAddress("little india").build();
    public static final Location IDA = new LocationBuilder().withName("Ida Mueller").withPhone("8482131")
            .withEmail("hans@example.com").withAddress("chicago ave").build();

    // Manually added - Location's details found in {@code CommandTestUtil}
    public static final Location AMY = new LocationBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
            .withPostalCode(VALID_POSTAL_CODE_AMY)
            .withTags(VALID_TAG_FRIEND)
            .withVisitDates(VALID_DATE_AMY).build();

    public static final Location BOB = new LocationBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
            .withPostalCode(VALID_POSTAL_CODE_BOB)
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .withVisitDates(VALID_DATE_BOB).build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier";

    private TypicalLocations() {}

    /**
     * Returns an {@code AddressBook} with all the typical locations.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Location location : getTypicalLocations()) {
            ab.addLocation(location);
        }
        return ab;
    }

    public static List<Location> getTypicalLocations() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
