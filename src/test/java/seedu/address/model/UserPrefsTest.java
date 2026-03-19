package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class UserPrefsTest {

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        UserPrefs userPref = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPref.setGuiSettings(null));
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        UserPrefs userPrefs = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPrefs.setAddressBookFilePath(null));
    }

    @Test
    public void setShortcutMap_nullMap_throwsNullPointerException() {
        UserPrefs userPrefs = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPrefs.setShortcutMap(null));
    }

    @Test
    public void shortcutOperations_success() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setShortcut("a", "add");
        userPrefs.setShortcut("e", "edit");

        assertEquals(Map.of("a", "add", "e", "edit"), userPrefs.getShortcutMap());

        userPrefs.removeShortcut("a");
        assertEquals(Map.of("e", "edit"), userPrefs.getShortcutMap());
    }

}
