package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class ShortcutMapTest {

    private final ShortcutMap shortcutMap = new ShortcutMap();

    @Test
    public void constructor() {
        assertTrue(shortcutMap.isEmpty());
        assertEquals(Map.of(), shortcutMap.getShortcutMappings());
    }

    @Test
    public void setShortcutMappings_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> shortcutMap.setShortcutMappings(null));
    }

    @Test
    public void setShortcutMappings_validMap_replacesData() {
        shortcutMap.setShortcut("a", "add");

        Map<String, String> newMappings = new LinkedHashMap<>();
        newMappings.put("e", "edit");
        newMappings.put("l", "list");
        shortcutMap.setShortcutMappings(newMappings);

        assertEquals(newMappings, shortcutMap.getShortcutMappings());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> shortcutMap.resetData(null));
    }

    @Test
    public void resetData_validReadOnlyShortcutMap_replacesData() {
        ShortcutMap newData = new ShortcutMap();
        newData.setShortcut("a", "add");
        newData.setShortcut("e", "edit");

        shortcutMap.resetData(newData);

        assertEquals(newData, shortcutMap);
    }

    @Test
    public void hasShortcut_nullAlias_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> shortcutMap.hasShortcut(null));
    }

    @Test
    public void hasShortcut_existingAlias_returnsTrue() {
        shortcutMap.setShortcut("a", "add");

        assertTrue(shortcutMap.hasShortcut("a"));
    }

    @Test
    public void hasShortcut_missingAlias_returnsFalse() {
        assertFalse(shortcutMap.hasShortcut("a"));
    }

    @Test
    public void setShortcut_nullAlias_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> shortcutMap.setShortcut(null, "add"));
    }

    @Test
    public void setShortcut_nullCommandWord_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> shortcutMap.setShortcut("a", null));
    }

    @Test
    public void setShortcut_addsAndUpdatesShortcut() {
        shortcutMap.setShortcut("a", "add");
        shortcutMap.setShortcut("a", "aliasadd");

        assertEquals("aliasadd", shortcutMap.getCommandWord("a"));
    }

    @Test
    public void removeShortcut_nullAlias_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> shortcutMap.removeShortcut(null));
    }

    @Test
    public void removeShortcut_existingAlias_removesShortcut() {
        shortcutMap.setShortcut("a", "add");

        shortcutMap.removeShortcut("a");

        assertFalse(shortcutMap.hasShortcut("a"));
    }

    @Test
    public void getCommandWord_nullAlias_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> shortcutMap.getCommandWord(null));
    }

    @Test
    public void getCommandWord_existingAlias_returnsCommandWord() {
        shortcutMap.setShortcut("a", "add");

        assertEquals("add", shortcutMap.getCommandWord("a"));
    }

    @Test
    public void getCommandWord_missingAlias_returnsNull() {
        assertNull(shortcutMap.getCommandWord("a"));
    }

    @Test
    public void getSortedShortcutMappings_returnsMappingsSortedByCommandWordThenAlias() {
        shortcutMap.setShortcut("e", "edit");
        shortcutMap.setShortcut("a1", "add");
        shortcutMap.setShortcut("a", "add");

        List<Entry<String, String>> expected = List.of(
                Map.entry("a", "add"),
                Map.entry("a1", "add"),
                Map.entry("e", "edit"));

        assertEquals(expected, shortcutMap.getSortedShortcutMappings().collect(Collectors.toList()));
    }

    @Test
    public void toStringMethod() {
        Map<String, String> mappings = new LinkedHashMap<>();
        mappings.put("a", "add");
        mappings.put("e", "edit");
        shortcutMap.setShortcutMappings(mappings);
        String expected = ShortcutMap.class.getCanonicalName()
                + "{shortcutMappings=" + shortcutMap.getShortcutMappings() + "}";
        assertEquals(expected, shortcutMap.toString());
    }
}
