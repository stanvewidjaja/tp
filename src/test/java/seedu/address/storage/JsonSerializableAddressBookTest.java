package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.TypicalLocations;

public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_LOCATIONS_FILE = TEST_DATA_FOLDER.resolve("typicalLocationsAddressBook.json");
    private static final Path INVALID_LOCATION_FILE = TEST_DATA_FOLDER.resolve("invalidLocationAddressBook.json");
    private static final Path DUPLICATE_LOCATION_FILE = TEST_DATA_FOLDER.resolve("duplicateLocationAddressBook.json");

    @Test
    public void toModelType_typicalLocationsFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_LOCATIONS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalLocationsAddressBook = TypicalLocations.getTypicalAddressBook();
        assertEquals(addressBookFromFile, typicalLocationsAddressBook);
    }

    @Test
    public void toModelType_invalidLocationFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_LOCATION_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateLocations_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_LOCATION_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_LOCATION,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_locationWithMissingOptionalFields_success() throws Exception {
        AddressBook addressBook = new AddressBook();

        addressBook.addLocation(new seedu.address.testutil.LocationBuilder()
                .withName("Simple Place")
                .withoutPhone()
                .withoutEmail()
                .withoutAddress()
                .withoutPostalCode()
                .withoutVisitDates()
                .withTags()
                .build());

        // Deserialization check
        JsonSerializableAddressBook dataFromModel = new JsonSerializableAddressBook(addressBook);
        assertEquals(addressBook, dataFromModel.toModelType());
    }

    @Test
    public void constructor_nullLocations_success() throws IllegalValueException {
        JsonSerializableAddressBook jsonSerializableAddressBook = new JsonSerializableAddressBook(null, null);
        AddressBook addressBook = jsonSerializableAddressBook.toModelType();
        assertEquals(new AddressBook(), addressBook);
    }

    @Test
    public void toModelType_addressBookWithNotes_success() throws Exception {
        AddressBook addressBook = new AddressBookBuilder()
                .withNote(seedu.address.model.location.dates.VisitDate.of("2026-03-24"),
                        new seedu.address.model.location.NoteContent("Note 1"))
                .withNote(seedu.address.model.location.dates.VisitDate.of("every Tuesday"),
                        new seedu.address.model.location.NoteContent("Note 2"))
                .build();

        JsonSerializableAddressBook dataFromModel = new JsonSerializableAddressBook(addressBook);
        assertEquals(addressBook, dataFromModel.toModelType());
    }

    @Test
    public void toModelType_locationWithPostalCodeAndVisitDates_success() throws Exception {
        AddressBook addressBook = new AddressBook();

        addressBook.addLocation(new seedu.address.testutil.LocationBuilder()
                .withName("Museum")
                .withPhone("61234567")
                .withEmail("museum@example.com")
                .withAddress("123 Street")
                .withPostalCode("530456")
                .withVisitDates("2026-01-01", "2026-01-05")
                .withTags("history")
                .build());

        JsonSerializableAddressBook jsonAddressBook =
                new JsonSerializableAddressBook(addressBook);

        AddressBook converted = jsonAddressBook.toModelType();

        assertEquals(addressBook, converted);
    }

    @Test
    public void toModelType_addressBookWithEmptyNotes_success() throws Exception {
        AddressBook addressBook = new AddressBook();

        JsonSerializableAddressBook dataFromModel = new JsonSerializableAddressBook(addressBook);

        assertEquals(addressBook, dataFromModel.toModelType());
    }
}
