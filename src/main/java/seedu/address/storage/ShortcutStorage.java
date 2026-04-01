package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyShortcutMap;
import seedu.address.model.ShortcutMap;

/**
 * Represents a storage for shortcut mappings.
 */
public interface ShortcutStorage {

    /**
     * Returns the file path of the shortcut data file.
     */
    Path getShortcutFilePath();

    /**
     * Returns shortcut mappings from storage.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataLoadingException if the loading of data from the shortcut file failed.
     */
    Optional<ShortcutMap> readShortcutMap() throws DataLoadingException;

    /**
     * Saves the given shortcut map to the storage.
     *
     * @param shortcutMap cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveShortcutMap(ReadOnlyShortcutMap shortcutMap) throws IOException;

    /**
     * @see #saveShortcutMap(ReadOnlyShortcutMap)
     */
    void saveShortcutMap(ReadOnlyShortcutMap shortcutMap, Path filePath) throws IOException;
}
