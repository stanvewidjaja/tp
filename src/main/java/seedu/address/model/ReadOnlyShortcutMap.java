package seedu.address.model;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

/**
 * Unmodifiable view of a shortcut map.
 */
public interface ReadOnlyShortcutMap {

    /**
     * Returns true if there are no shortcut mappings.
     */
    boolean isEmpty();

    /**
     * Returns the command word mapped to {@code alias}, or {@code null} if absent.
     */
    String getCommandWord(String alias);

    /**
     * Returns an unmodifiable view of the shortcut mappings.
     */
    Map<String, String> getShortcutMappings();

    /**
     * Returns a stream of shortcut mappings sorted for display.
     */
    Stream<Entry<String, String>> getSortedShortcutMappings();
}
