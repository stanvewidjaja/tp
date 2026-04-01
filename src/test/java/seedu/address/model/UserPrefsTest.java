package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

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
    public void setShortcutMapFilePath_nullPath_throwsNullPointerException() {
        UserPrefs userPrefs = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPrefs.setShortcutMapFilePath(null));
    }

    @Test
    public void copyConstructorAndResetData_ignoreLegacyShortcutFields() {
        UserPrefs original = new UserPrefs();
        original.setAddressBookFilePath(java.nio.file.Paths.get("data", "custom.json"));
        original.setShortcutMapFilePath(java.nio.file.Paths.get("data", "custom-shortcut.json"));

        UserPrefs copied = new UserPrefs(original);
        assertEquals(original, copied);
    }

}
