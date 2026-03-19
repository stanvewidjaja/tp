package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import seedu.address.commons.core.GuiSettings;

/**
 * Represents User's preferences.
 */
public class UserPrefs implements ReadOnlyUserPrefs {

    private GuiSettings guiSettings = new GuiSettings();
    private Path addressBookFilePath = Paths.get("data" , "addressbook.json");
    private Map<String, String> shortcutMap = new LinkedHashMap<>();

    /**
     * Creates a {@code UserPrefs} with default values.
     */
    public UserPrefs() {}

    /**
     * Creates a {@code UserPrefs} with the prefs in {@code userPrefs}.
     */
    public UserPrefs(ReadOnlyUserPrefs userPrefs) {
        this();
        resetData(userPrefs);
    }

    /**
     * Resets the existing data of this {@code UserPrefs} with {@code newUserPrefs}.
     */
    public void resetData(ReadOnlyUserPrefs newUserPrefs) {
        requireNonNull(newUserPrefs);
        setGuiSettings(newUserPrefs.getGuiSettings());
        setAddressBookFilePath(newUserPrefs.getAddressBookFilePath());
        setShortcutMap(newUserPrefs.getShortcutMap());
    }

    public GuiSettings getGuiSettings() {
        return guiSettings;
    }

    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        this.guiSettings = guiSettings;
    }

    public Path getAddressBookFilePath() {
        return addressBookFilePath;
    }

    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        this.addressBookFilePath = addressBookFilePath;
    }

    @Override
    public Map<String, String> getShortcutMap() {
        return Collections.unmodifiableMap(shortcutMap);
    }

    public void setShortcutMap(Map<String, String> shortcutMap) {
        requireNonNull(shortcutMap);
        this.shortcutMap = new LinkedHashMap<>(shortcutMap);
    }

    /**
     * Returns true if a shortcut exists for {@code alias}.
     */
    public boolean hasShortcut(String alias) {
        requireNonNull(alias);
        return shortcutMap.containsKey(alias);
    }

    public void setShortcut(String alias, String commandWord) {
        requireNonNull(alias);
        requireNonNull(commandWord);
        shortcutMap.put(alias, commandWord);
    }

    /**
     * Removes the shortcut mapped to {@code alias}.
     */
    public void removeShortcut(String alias) {
        requireNonNull(alias);
        shortcutMap.remove(alias);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UserPrefs)) {
            return false;
        }

        UserPrefs otherUserPrefs = (UserPrefs) other;
        return guiSettings.equals(otherUserPrefs.guiSettings)
                && addressBookFilePath.equals(otherUserPrefs.addressBookFilePath)
                && shortcutMap.equals(otherUserPrefs.shortcutMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, addressBookFilePath, shortcutMap);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings);
        sb.append("\nLocal data file location : " + addressBookFilePath);
        sb.append("\nShortcuts : " + shortcutMap);
        return sb.toString();
    }

}
