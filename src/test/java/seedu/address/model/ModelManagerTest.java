package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_LOCATIONS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalLocations.ALICE;
import static seedu.address.testutil.TypicalLocations.BENSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.location.NoteContent;
import seedu.address.model.location.dates.VisitDate;
import seedu.address.model.location.predicates.NameContainsKeywordsPredicate;
import seedu.address.testutil.AddressBookBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void shortcutOperations_success() {
        modelManager.setShortcut("a", "add");
        assertTrue(modelManager.hasShortcut("a"));
        assertEquals(Map.of("a", "add"), modelManager.getShortcutMap().getShortcutMappings());

        modelManager.removeShortcut("a");
        assertFalse(modelManager.hasShortcut("a"));
        assertTrue(modelManager.getShortcutMap().getShortcutMappings().isEmpty());
    }

    @Test
    public void hasLocation_nullLocation_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasLocation(null));
    }

    @Test
    public void hasLocation_locationNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasLocation(ALICE));
    }

    @Test
    public void hasLocation_locationInAddressBook_returnsTrue() {
        modelManager.addLocation(ALICE);
        assertTrue(modelManager.hasLocation(ALICE));
    }

    @Test
    public void getFilteredLocationList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredLocationList().remove(0));
    }

    @Test
    public void getPlannerLocationList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getPlannerLocationList().remove(0));
    }

    @Test
    public void undoRedoState_singleLevelHistory_success() {
        modelManager.saveState();
        modelManager.addLocation(ALICE);
        modelManager.commitState();

        assertTrue(modelManager.canUndoState());
        assertFalse(modelManager.canRedoState());

        modelManager.undoState();
        assertFalse(modelManager.hasLocation(ALICE));
        assertFalse(modelManager.canUndoState());
        assertTrue(modelManager.canRedoState());

        modelManager.redoState();
        assertTrue(modelManager.hasLocation(ALICE));
        assertTrue(modelManager.canUndoState());
        assertFalse(modelManager.canRedoState());
    }

    @Test
    public void setNote_updatesPlannerNote() throws IllegalValueException {
        VisitDate date = VisitDate.of("2026-03-24");
        NoteContent note = new NoteContent("Test Note");
        modelManager.updatePlannerLocationList(LocalDate.of(2026, 3, 24));
        NoteContent initialPlannerNote = modelManager.getPlannerNoteProperty().getValue();
        modelManager.setNote(date, note);
        NoteContent updatedPlannerNote = modelManager.getPlannerNoteProperty().getValue();

        assertFalse(note.equals(initialPlannerNote));
        assertEquals(note, updatedPlannerNote);
    }

    @Test
    public void discardState_clearsPendingState() {
        modelManager.saveState();
        modelManager.discardState();
        modelManager.commitState();
        assertFalse(modelManager.canUndoState());
    }

    @Test
    public void commitState_noPendingState_noChange() {
        modelManager.commitState();
        assertFalse(modelManager.canUndoState());
    }

    @Test
    public void updatePlannerLocationList_nullDate_clearsPredicate() {
        modelManager.updatePlannerLocationList(null);
        assertEquals(0, modelManager.getPlannerLocationList().size());
    }

    @Test
    public void equals_differentTypes_returnsFalse() {
        assertFalse(modelManager.equals(new Object()));
    }

    @Test
    public void commitState_noChange_preservesHistory() {
        modelManager.saveState();
        modelManager.commitState();
        assertFalse(modelManager.canUndoState());

        modelManager.saveState();
        modelManager.addLocation(ALICE);
        modelManager.commitState();
        modelManager.undoState();

        assertTrue(modelManager.canRedoState());

        modelManager.saveState();
        modelManager.commitState();

        assertTrue(modelManager.canRedoState());
    }

    @Test
    public void discardState_failedCommand_preservesHistory() {
        modelManager.saveState();
        modelManager.addLocation(ALICE);
        modelManager.commitState();

        modelManager.saveState();
        modelManager.discardState();

        assertTrue(modelManager.canUndoState());
        modelManager.undoState();
        assertFalse(modelManager.hasLocation(ALICE));
    }

    @Test
    public void undoRedoState_shortcutOnly_success() {
        modelManager.saveState();
        modelManager.setShortcut("a", "add");
        modelManager.commitState();

        assertTrue(modelManager.hasShortcut("a"));

        modelManager.undoState();
        assertFalse(modelManager.hasShortcut("a"));

        modelManager.redoState();
        assertTrue(modelManager.hasShortcut("a"));
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withLocation(ALICE).withLocation(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        ShortcutMap shortcutMap = new ShortcutMap();
        shortcutMap.setShortcutMappings(Map.of("a", "add"));
        modelManager = new ModelManager(addressBook, userPrefs, shortcutMap);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs, shortcutMap);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs, shortcutMap)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredLocationList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs, shortcutMap)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredLocationList(PREDICATE_SHOW_ALL_LOCATIONS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentUserPrefs, shortcutMap)));

        // different shortcuts -> returns false
        ShortcutMap differentShortcutMap = new ShortcutMap();
        differentShortcutMap.setShortcutMappings(Map.of("e", "edit"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs, differentShortcutMap)));

        // different currentPlannedDate -> returns false
        modelManager.updatePlannerLocationList(LocalDate.of(2026, 3, 24));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs, shortcutMap)));
    }

    @Test
    public void setNote_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setNote(null, new NoteContent("Note")));
    }

    @Test
    public void setNote_nullNote_throwsNullPointerException() throws Exception {
        assertThrows(NullPointerException.class, () -> modelManager.setNote(VisitDate.of("2026-03-24"), null));
    }

    @Test
    public void getPlannerNoteProperty_initialState_returnsNull() {
        assertEquals(null, modelManager.getPlannerNoteProperty().getValue());
    }

    @Test
    public void updatePlannerLocationList_validDate_updatesPlannerNote() throws Exception {
        ModelManager separateModel = new ModelManager();
        VisitDate date = VisitDate.of("2026-03-24");
        NoteContent note = new NoteContent("Great day");
        separateModel.setNote(date, note);

        // Date matches -> note should be present
        separateModel.updatePlannerLocationList(LocalDate.of(2026, 3, 24));
        assertEquals(note, separateModel.getPlannerNoteProperty().getValue());

        // Date does not match -> note should be null
        separateModel.updatePlannerLocationList(LocalDate.of(2026, 3, 25));
        assertEquals(null, separateModel.getPlannerNoteProperty().getValue());
    }

    @Test
    public void updatePlannerNote_multipleNotes_mergesNotes() throws Exception {
        ModelManager separateModel = new ModelManager();
        VisitDate everyDay = VisitDate.of("everyday");
        VisitDate oneTime = VisitDate.of("2026-03-24");
        NoteContent genericNote = new NoteContent("Generic note");
        NoteContent specificNote = new NoteContent("Specific note");

        // Use LinkedHashMap in AddressBook to ensure order
        separateModel.setNote(everyDay, genericNote);
        separateModel.setNote(oneTime, specificNote);

        separateModel.updatePlannerLocationList(LocalDate.of(2026, 3, 24));

        String value = separateModel.getPlannerNoteProperty().getValue().toString();
        String[] mergedNotes = value.split("\n\n");
        assertEquals(2, mergedNotes.length);
        assertTrue(java.util.Arrays.asList(mergedNotes).contains("Generic note"));
        assertTrue(java.util.Arrays.asList(mergedNotes).contains("Specific note"));
    }
}
