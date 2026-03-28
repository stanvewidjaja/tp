package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.model.ReadOnlyShortcutMap;
import seedu.address.model.ShortcutMap;

/**
 * Jackson-friendly serializable form of the shortcut map.
 */
@JsonRootName(value = "shortcuts")
class JsonSerializableShortcutMap {

    private final Map<String, String> shortcutMap;

    @JsonCreator
    JsonSerializableShortcutMap(@JsonProperty("shortcutMap") Map<String, String> shortcutMap) {
        this.shortcutMap = shortcutMap;
    }

    JsonSerializableShortcutMap(ReadOnlyShortcutMap source) {
        requireNonNull(source);
        this.shortcutMap = source.getShortcutMappings();
    }

    public ShortcutMap toModelType() {
        ShortcutMap modelShortcutMap = new ShortcutMap();
        if (shortcutMap != null) {
            modelShortcutMap.setShortcutMappings(shortcutMap);
        }
        return modelShortcutMap;
    }
}
