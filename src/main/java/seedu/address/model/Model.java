package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.location.Location;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Location> PREDICATE_SHOW_ALL_LOCATIONS = unused -> true;

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
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

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

    /**
     * Updates the filter of the filtered location list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredLocationList(Predicate<Location> predicate);
}
