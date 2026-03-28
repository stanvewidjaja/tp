package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ShortcutMap;

public class JsonShortcutStorageTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonShortcutStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readShortcutMap_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readShortcutMap(null));
    }

    private Optional<ShortcutMap> readShortcutMap(String shortcutFileInTestDataFolder)
            throws DataLoadingException {
        Path shortcutFilePath = addToTestDataPathIfNotNull(shortcutFileInTestDataFolder);
        return new JsonShortcutStorage(shortcutFilePath).readShortcutMap(shortcutFilePath);
    }

    @Test
    public void readShortcutMap_missingFile_emptyResult() throws DataLoadingException {
        assertFalse(readShortcutMap("NonExistentFile.json").isPresent());
    }

    @Test
    public void readShortcutMap_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> readShortcutMap("NotJsonFormatShortcutMap.json"));
    }

    @Test
    public void readShortcutMap_fileInOrder_successfullyRead() throws DataLoadingException {
        Map<String, String> expected = Map.of("a", "add", "e", "edit");
        ShortcutMap actual = readShortcutMap("TypicalShortcutMap.json").get();
        assertEquals(expected, actual.getShortcutMappings());
    }

    @Test
    public void readShortcutMap_valuesMissingFromFile_defaultValuesUsed() throws DataLoadingException {
        ShortcutMap actual = readShortcutMap("EmptyShortcutMap.json").get();
        assertEquals(Map.of(), actual.getShortcutMappings());
    }

    @Test
    public void readShortcutMap_extraValuesInFile_extraValuesIgnored() throws DataLoadingException {
        Map<String, String> expected = Map.of("a", "add");
        ShortcutMap actual = readShortcutMap("ExtraValuesShortcutMap.json").get();
        assertEquals(expected, actual.getShortcutMappings());
    }

    private Path addToTestDataPathIfNotNull(String shortcutFileInTestDataFolder) {
        return shortcutFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(shortcutFileInTestDataFolder)
                : null;
    }

    @Test
    public void saveShortcutMap_nullShortcutMap_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveShortcutMap(null, "SomeFile.json"));
    }

    @Test
    public void saveShortcutMap_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveShortcutMap(new ShortcutMap(), null));
    }

    private void saveShortcutMap(ShortcutMap shortcutMap, String shortcutFileInTestDataFolder) {
        try {
            new JsonShortcutStorage(addToTestDataPathIfNotNull(shortcutFileInTestDataFolder))
                    .saveShortcutMap(shortcutMap);
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file", ioe);
        }
    }

    @Test
    public void saveShortcutMap_allInOrder_success() throws DataLoadingException, IOException {
        ShortcutMap original = new ShortcutMap();
        original.setShortcutMappings(Map.of("a", "add"));

        Path shortcutFilePath = testFolder.resolve("TempShortcutMap.json");
        JsonShortcutStorage jsonShortcutStorage = new JsonShortcutStorage(shortcutFilePath);

        jsonShortcutStorage.saveShortcutMap(original);
        ShortcutMap readBack = jsonShortcutStorage.readShortcutMap().get();
        assertEquals(original, readBack);

        original = new ShortcutMap();
        original.setShortcutMappings(Map.of("e", "edit", "l", "list"));
        jsonShortcutStorage.saveShortcutMap(original);
        readBack = jsonShortcutStorage.readShortcutMap().get();
        assertEquals(original, readBack);
    }
}
