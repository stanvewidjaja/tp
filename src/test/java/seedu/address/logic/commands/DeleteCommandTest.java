package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showLocationAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_LOCATION;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_LOCATION;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_LOCATION;
import static seedu.address.testutil.TypicalLocations.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.location.Location;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Location locationToDelete = model.getFilteredLocationList().get(INDEX_FIRST_LOCATION.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(List.of(INDEX_FIRST_LOCATION));

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_LOCATION_SUCCESS,
                Messages.format(locationToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteLocation(locationToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredLocationList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(List.of(outOfBoundIndex));

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_LOCATION_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexesUnfilteredList_success() {
        Location firstLocationToDelete = model.getFilteredLocationList().get(INDEX_FIRST_LOCATION.getZeroBased());
        Location thirdLocationToDelete = model.getFilteredLocationList().get(INDEX_THIRD_LOCATION.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(List.of(INDEX_FIRST_LOCATION, INDEX_THIRD_LOCATION));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteLocation(firstLocationToDelete);
        expectedModel.deleteLocation(thirdLocationToDelete);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_LOCATIONS_SUCCESS,
                Messages.format(firstLocationToDelete) + "\n" + Messages.format(thirdLocationToDelete));
        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateIndexes_throwsCommandException() {
        DeleteCommand deleteCommand = new DeleteCommand(
                List.of(INDEX_FIRST_LOCATION, INDEX_FIRST_LOCATION));
        assertCommandFailure(deleteCommand, model, DeleteCommand.MESSAGE_DUPLICATE_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showLocationAtIndex(model, INDEX_FIRST_LOCATION);

        Location locationToDelete = model.getFilteredLocationList().get(INDEX_FIRST_LOCATION.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(List.of(INDEX_FIRST_LOCATION));

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_LOCATION_SUCCESS,
                Messages.format(locationToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteLocation(locationToDelete);
        showNoLocation(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showLocationAtIndex(model, INDEX_FIRST_LOCATION);

        Index outOfBoundIndex = INDEX_SECOND_LOCATION;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getLocationList().size());

        DeleteCommand deleteCommand = new DeleteCommand(List.of(outOfBoundIndex));

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_LOCATION_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(List.of(INDEX_FIRST_LOCATION));
        DeleteCommand deleteSecondCommand = new DeleteCommand(List.of(INDEX_SECOND_LOCATION));

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(List.of(INDEX_FIRST_LOCATION));
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different location -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteCommand = new DeleteCommand(List.of(targetIndex));
        String expected = DeleteCommand.class.getCanonicalName() + "{targetIndexes=[" + targetIndex + "]}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoLocation(Model model) {
        model.updateFilteredLocationList(p -> false);

        assertTrue(model.getFilteredLocationList().isEmpty());
    }
}
