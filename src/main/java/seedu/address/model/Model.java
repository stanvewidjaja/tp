package seedu.address.model;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.function.Predicate;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.Theme;
import seedu.address.model.location.Location;
import seedu.address.model.location.NoteContent;
import seedu.address.model.location.dates.VisitDate;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Location> PREDICATE_SHOW_ALL_LOCATIONS = unused -> true;

    /** {@code Predicate} that always evaluate to false */
    Predicate<Location> PREDICATE_HIDE_ALL_LOCATIONS = unused -> false;


    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Returns the user prefs' theme.
     */
    Theme getTheme();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Sets the user prefs' theme.
     */
    void setTheme(Theme theme);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Returns an unmodifiable view of the user-defined shortcuts.
     */
    ReadOnlyShortcutMap getShortcutMap();

    /**
     * Returns true if a shortcut exists for {@code alias}.
     */
    boolean hasShortcut(String alias);

    /**
     * Adds or updates a shortcut.
     */
    void setShortcut(String alias, String commandWord);

    /**
     * Removes the shortcut associated with {@code alias}.
     */
    void removeShortcut(String alias);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /**
     * Saves the current undoable application state as a pending snapshot before a command runs.
     */
    default void saveState() {}

    /**
     * Commits the pending undo snapshot if the command changed undoable application state.
     */
    default void commitState() {}

    /**
     * Discards any pending undo snapshot because the command failed.
     */
    default void discardState() {}

    /**
     * Returns true if the previous undoable state can be restored.
     */
    default boolean canUndoState() {
        return false;
    }

    /**
     * Restores the previous undoable state.
     */
    default void undoState() {}

    /**
     * Returns true if the most recently undone state can be reapplied.
     */
    default boolean canRedoState() {
        return false;
    }

    /**
     * Reapplies the most recently undone state.
     */
    default void redoState() {}

    /**
     * Returns true if the persisted address book differs from the last successful save.
     */
    default boolean hasUnsavedAddressBookChanges() {
        return false;
    }

    /**
     * Returns true if the persisted shortcut map differs from the last successful save.
     */
    default boolean hasUnsavedShortcutMapChanges() {
        return false;
    }

    /**
     * Returns true if the persisted user preferences differ from the last successful save.
     */
    default boolean hasUnsavedUserPrefsChanges() {
        return false;
    }

    /**
     * Marks the address book as saved after a successful persistence operation.
     */
    default void markAddressBookSaved() {}

    /**
     * Marks the shortcut map as saved after a successful persistence operation.
     */
    default void markShortcutMapSaved() {}

    /**
     * Marks the user preferences as saved after a successful persistence operation.
     */
    default void markUserPrefsSaved() {}

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a location with the same identity as {@code location} exists in the address book.
     */
    boolean hasLocation(Location location);

    /**
     * Deletes the given location.
     * The location must exist in the address book.
     */
    void deleteLocation(Location target);

    /**
     * Adds the given location.
     * {@code location} must not already exist in the address book.
     */
    void addLocation(Location location);

    /**
     * Replaces the given location {@code target} with {@code editedLocation}.
     * {@code target} must exist in the address book.
     * The location identity of {@code editedLocation} must not be the same as another existing one in the address book.
     */
    void setLocation(Location target, Location editedLocation);

    /** Returns an unmodifiable view of the filtered location list */
    ObservableList<Location> getFilteredLocationList();

    /** Returns an unmodifiable view of the planner location list */
    ObservableList<Location> getPlannerLocationList();

    /**
     * Updates the filter of the filtered location list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredLocationList(Predicate<Location> predicate);

    /**
     * Updates the planner list to filter by the given {@code date}.
     * clears the list if {@code date} is null.
     */
    void updatePlannerLocationList(LocalDate date);

    /**
     * Sets a note for the specified visit date.
     */
    void setNote(VisitDate date, NoteContent note);

    /**
     * Returns true if a note exists for the given visit date.
     */
    boolean hasNote(VisitDate date);

    /**
     * Removes the note for the specified visit date.
     */
    void removeNote(VisitDate date);

    /**
     * Returns an observable property pointing to the current active note in the planner.
     */
    ObservableValue<NoteContent> getPlannerNoteProperty();

}
