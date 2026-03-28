package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Wraps all shortcut data at the shortcut-map level.
 */
public class ShortcutMap implements ReadOnlyShortcutMap {

    private final Map<String, String> shortcutMappings = new LinkedHashMap<>();

    public ShortcutMap() {}

    /**
     * Creates a ShortcutMap using the entries in the {@code toBeCopied}.
     */
    public ShortcutMap(ReadOnlyShortcutMap toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    /**
     * Replaces the contents of this shortcut map with {@code shortcutMappings}.
     */
    public void setShortcutMappings(Map<String, String> shortcutMappings) {
        requireNonNull(shortcutMappings);
        this.shortcutMappings.clear();
        this.shortcutMappings.putAll(shortcutMappings);
    }

    /**
     * Resets the existing data of this {@code ShortcutMap} with {@code newData}.
     */
    public void resetData(ReadOnlyShortcutMap newData) {
        requireNonNull(newData);
        setShortcutMappings(newData.getShortcutMappings());
    }

    /**
     * Returns true if a shortcut exists for {@code alias}.
     */
    public boolean hasShortcut(String alias) {
        requireNonNull(alias);
        return shortcutMappings.containsKey(alias);
    }

    /**
     * Adds or updates the shortcut mapped to {@code alias}.
     */
    public void setShortcut(String alias, String commandWord) {
        requireNonNull(alias);
        requireNonNull(commandWord);
        shortcutMappings.put(alias, commandWord);
    }

    /**
     * Removes the shortcut mapped to {@code alias}.
     */
    public void removeShortcut(String alias) {
        requireNonNull(alias);
        shortcutMappings.remove(alias);
    }

    @Override
    public boolean isEmpty() {
        return shortcutMappings.isEmpty();
    }

    @Override
    public String getCommandWord(String alias) {
        requireNonNull(alias);
        return shortcutMappings.get(alias);
    }

    @Override
    public Map<String, String> getShortcutMappings() {
        return Collections.unmodifiableMap(shortcutMappings);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("shortcutMappings", shortcutMappings)
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ShortcutMap)) {
            return false;
        }

        ShortcutMap otherShortcutMap = (ShortcutMap) other;
        return shortcutMappings.equals(otherShortcutMap.shortcutMappings);
    }

    @Override
    public int hashCode() {
        return shortcutMappings.hashCode();
    }
}
