package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static seedu.address.testutil.TypicalLocations.getTypicalAddressBook;

import java.nio.file.Path;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.Theme;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ShortcutMap;
import seedu.address.model.UserPrefs;

public class StorageManagerTest {

    @TempDir
    public Path testFolder;

    private StorageManager storageManager;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        JsonShortcutStorage shortcutStorage = new JsonShortcutStorage(getTempFilePath("shortcut"));
        storageManager = new StorageManager(addressBookStorage, userPrefsStorage, shortcutStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */

        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        original.setTheme(Theme.DARK);
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void shortcutReadSave() throws Exception {
        ShortcutMap original = new ShortcutMap();
        original.setShortcutMappings(Map.of("a", "add", "e", "edit"));
        storageManager.saveShortcutMap(original);
        ShortcutMap retrieved = storageManager.readShortcutMap().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void addressBookReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonAddressBookStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonAddressBookStorageTest} class.
         */

        AddressBook original = getTypicalAddressBook();
        storageManager.saveAddressBook(original);
        ReadOnlyAddressBook retrieved = storageManager.readAddressBook().get();
        assertEquals(original, new AddressBook(retrieved));
    }

    @Test
    public void getAddressBookFilePath() {
        assertNotNull(storageManager.getAddressBookFilePath());
    }

    @Test
    public void getShortcutFilePath() {
        assertEquals(getTempFilePath("shortcut"), storageManager.getShortcutFilePath());
    }

    @Test
    public void addressBookReadSave_missingOptionalFields_success() throws Exception {
        AddressBook original = new AddressBook();

        original.addLocation(new seedu.address.testutil.LocationBuilder()
                .withName("Simple Place")
                .withoutPhone()
                .withoutEmail()
                .withoutAddress()
                .withoutPostalCode()
                .withoutVisitDates()
                .withTags()
                .build());

        storageManager.saveAddressBook(original);
        ReadOnlyAddressBook retrieved = storageManager.readAddressBook().get();

        assertEquals(original, new AddressBook(retrieved));
    }

    @Test
    public void addressBookReadSave_withPostalCodeAndVisitDates_success() throws Exception {
        AddressBook original = new AddressBook();

        original.addLocation(new seedu.address.testutil.LocationBuilder()
                .withName("Museum")
                .withPhone("61234567")
                .withEmail("museum@example.com")
                .withAddress("123 Street")
                .withPostalCode("530456")
                .withVisitDates("2026-01-01", "2026-01-05")
                .withTags("history")
                .build());

        storageManager.saveAddressBook(original);
        ReadOnlyAddressBook retrieved = storageManager.readAddressBook().get();

        assertEquals(original, new AddressBook(retrieved));
    }
}
