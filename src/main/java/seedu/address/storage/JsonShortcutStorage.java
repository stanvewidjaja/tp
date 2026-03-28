package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.ReadOnlyShortcutMap;
import seedu.address.model.ShortcutMap;

/**
 * A class to access shortcut data stored as a json file on the hard disk.
 */
public class JsonShortcutStorage implements ShortcutStorage {

    private final Path filePath;

    public JsonShortcutStorage(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public Path getShortcutFilePath() {
        return filePath;
    }

    @Override
    public Optional<ShortcutMap> readShortcutMap() throws DataLoadingException {
        return readShortcutMap(filePath);
    }

    /**
     * Similar to {@link #readShortcutMap()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if loading the data from storage failed.
     */
    public Optional<ShortcutMap> readShortcutMap(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializableShortcutMap> jsonShortcutMap = JsonUtil.readJsonFile(
                filePath, JsonSerializableShortcutMap.class);
        if (!jsonShortcutMap.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(jsonShortcutMap.get().toModelType());
    }

    @Override
    public void saveShortcutMap(ReadOnlyShortcutMap shortcutMap) throws IOException {
        saveShortcutMap(shortcutMap, filePath);
    }

    /**
     * Similar to {@link #saveShortcutMap(ReadOnlyShortcutMap)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveShortcutMap(ReadOnlyShortcutMap shortcutMap, Path filePath) throws IOException {
        requireNonNull(shortcutMap);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableShortcutMap(shortcutMap), filePath);
    }
}
