package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Theme;
import seedu.address.model.location.Location;
import seedu.address.model.location.NoteContent;
import seedu.address.model.location.dates.VisitDate;
import seedu.address.model.location.predicates.VisitDateMatchesKeywordsPredicate;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final ShortcutMap shortcutMap;
    private final FilteredList<Location> filteredLocations;
    private final FilteredList<Location> plannerLocations;
    private AppState undoState;
    private AppState redoState;
    private AppState pendingState;

    private final ObjectProperty<NoteContent> plannerNote;
    private LocalDate currentPlannedDate;

    /**
     * Initializes a ModelManager with the given addressBook, userPrefs and shortcuts.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs, ReadOnlyShortcutMap shortcutMap) {
        requireAllNonNull(addressBook, userPrefs, shortcutMap);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        this.shortcutMap = new ShortcutMap(shortcutMap);
        filteredLocations = new FilteredList<>(this.addressBook.getLocationList());
        plannerLocations = new FilteredList<>(
                this.addressBook.getLocationList()).filtered(PREDICATE_HIDE_ALL_LOCATIONS);

        this.plannerNote = new SimpleObjectProperty<>(null);
        this.currentPlannedDate = null;
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs(), new ShortcutMap());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public Theme getTheme() {
        return userPrefs.getTheme();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public void setTheme(Theme theme) {
        requireNonNull(theme);
        userPrefs.setTheme(theme);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== ShortcutMap ================================================================================

    @Override
    public ReadOnlyShortcutMap getShortcutMap() {
        return shortcutMap;
    }

    @Override
    public boolean hasShortcut(String alias) {
        requireNonNull(alias);
        return shortcutMap.hasShortcut(alias);
    }

    @Override
    public void setShortcut(String alias, String commandWord) {
        requireAllNonNull(alias, commandWord);
        shortcutMap.setShortcut(alias, commandWord);
    }

    @Override
    public void removeShortcut(String alias) {
        requireNonNull(alias);
        shortcutMap.removeShortcut(alias);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
        updatePlannerNote();
    }

    @Override
    public void saveState() {
        pendingState = AppState.from(addressBook, shortcutMap);
    }

    @Override
    public void commitState() {
        if (pendingState == null) {
            return;
        }

        AppState currentState = AppState.from(addressBook, shortcutMap);
        if (!pendingState.equals(currentState)) {
            undoState = pendingState;
            redoState = null;
        }

        pendingState = null;
    }

    @Override
    public void discardState() {
        pendingState = null;
    }

    @Override
    public boolean canUndoState() {
        return undoState != null;
    }

    @Override
    public void undoState() {
        redoState = AppState.from(addressBook, shortcutMap);
        restoreState(undoState);
        undoState = null;
    }

    @Override
    public boolean canRedoState() {
        return redoState != null;
    }

    @Override
    public void redoState() {
        undoState = AppState.from(addressBook, shortcutMap);
        restoreState(redoState);
        redoState = null;
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasLocation(Location location) {
        requireNonNull(location);
        return addressBook.hasLocation(location);
    }

    @Override
    public void deleteLocation(Location target) {
        addressBook.removeLocation(target);
    }

    @Override
    public void addLocation(Location location) {
        addressBook.addLocation(location);
        updateFilteredLocationList(PREDICATE_SHOW_ALL_LOCATIONS);
    }

    @Override
    public void setLocation(Location target, Location editedLocation) {
        requireAllNonNull(target, editedLocation);

        addressBook.setLocation(target, editedLocation);
    }

    //=========== Filtered Location List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Location} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Location> getFilteredLocationList() {
        return filteredLocations;
    }

    @Override
    public ObservableList<Location> getPlannerLocationList() {
        return plannerLocations;
    }

    @Override
    public void updateFilteredLocationList(Predicate<Location> predicate) {
        requireNonNull(predicate);
        filteredLocations.setPredicate(predicate);
    }

    /**
     * Updates planner list to show locations with a specific date
     * @param date LocalDate to find
     */
    @Override
    public void updatePlannerLocationList(LocalDate date) {
        currentPlannedDate = date;
        if (date == null) {
            plannerLocations.setPredicate(PREDICATE_HIDE_ALL_LOCATIONS);
            plannerNote.set(null);
        } else {
            plannerLocations.setPredicate(new VisitDateMatchesKeywordsPredicate(date));
            updatePlannerNote();
        }
    }

    private void updatePlannerNote() {
        if (currentPlannedDate == null) {
            plannerNote.set(null);
            return;
        }

        StringBuilder combinedNotes = new StringBuilder();
        for (Map.Entry<VisitDate, NoteContent> entry : addressBook.getNoteMap().entrySet()) {
            if (entry.getKey().isOn(currentPlannedDate)) {
                if (combinedNotes.length() > 0) {
                    combinedNotes.append("\n\n");
                }
                combinedNotes.append(entry.getValue().toString());
            }
        }

        if (combinedNotes.length() > 0) {
            plannerNote.set(new NoteContent(combinedNotes.toString()));
        } else {
            plannerNote.set(null);
        }
    }

    @Override
    public void setNote(VisitDate date, NoteContent note) {
        requireAllNonNull(date, note);
        addressBook.setNote(date, note);
        if (currentPlannedDate != null && date.isOn(currentPlannedDate)) {
            updatePlannerNote();
        }
    }

    @Override
    public ObservableValue<NoteContent> getPlannerNoteProperty() {
        return plannerNote;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && shortcutMap.equals(otherModelManager.shortcutMap)
                && filteredLocations.equals(otherModelManager.filteredLocations)
                && (currentPlannedDate == null ? otherModelManager.currentPlannedDate == null
                        : currentPlannedDate.equals(otherModelManager.currentPlannedDate));
    }

    private void restoreState(AppState state) {
        setAddressBook(state.addressBook());
        shortcutMap.resetData(state.shortcutMap());
    }

    private record AppState(AddressBook addressBook, ShortcutMap shortcutMap) {
        private static AppState from(ReadOnlyAddressBook addressBook, ReadOnlyShortcutMap shortcutMap) {
            return new AppState(new AddressBook(addressBook), new ShortcutMap(shortcutMap));
        }
    }

}
