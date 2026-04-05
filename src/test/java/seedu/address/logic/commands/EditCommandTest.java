package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_POSTAL_CODE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showLocationAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_LOCATION;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_LOCATION;
import static seedu.address.testutil.TypicalLocations.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditLocationDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ShortcutMap;
import seedu.address.model.UserPrefs;
import seedu.address.model.location.Location;
import seedu.address.testutil.EditLocationDescriptorBuilder;
import seedu.address.testutil.LocationBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new ShortcutMap());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Location editedLocation = new LocationBuilder().build();
        EditLocationDescriptor descriptor = new EditLocationDescriptorBuilder(editedLocation).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_LOCATION, descriptor);

        String expectedMessage = String.format(
                EditCommand.MESSAGE_EDIT_LOCATION_SUCCESS, Messages.format(editedLocation));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                new ShortcutMap());
        expectedModel.setLocation(model.getFilteredLocationList().get(0), editedLocation);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastLocation = Index.fromOneBased(model.getFilteredLocationList().size());
        Location lastLocation = model.getFilteredLocationList().get(indexLastLocation.getZeroBased());

        LocationBuilder locationInList = new LocationBuilder(lastLocation);
        Location editedLocation = locationInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditLocationDescriptor descriptor = new EditLocationDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastLocation, descriptor);

        String expectedMessage = String.format(
                EditCommand.MESSAGE_EDIT_LOCATION_SUCCESS, Messages.format(editedLocation));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                new ShortcutMap());
        expectedModel.setLocation(lastLocation, editedLocation);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_LOCATION, new EditLocationDescriptor());
        Location editedLocation = model.getFilteredLocationList().get(INDEX_FIRST_LOCATION.getZeroBased());

        String expectedMessage = String.format(
                EditCommand.MESSAGE_EDIT_LOCATION_SUCCESS, Messages.format(editedLocation));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                new ShortcutMap());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showLocationAtIndex(model, INDEX_FIRST_LOCATION);

        Location locationInFilteredList = model.getFilteredLocationList().get(INDEX_FIRST_LOCATION.getZeroBased());
        Location editedLocation = new LocationBuilder(locationInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_LOCATION,
                new EditLocationDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(
                EditCommand.MESSAGE_EDIT_LOCATION_SUCCESS, Messages.format(editedLocation));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                new ShortcutMap());
        expectedModel.setLocation(model.getFilteredLocationList().get(0), editedLocation);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateLocationUnfilteredList_failure() {
        Location firstLocation = model.getFilteredLocationList().get(INDEX_FIRST_LOCATION.getZeroBased());
        EditLocationDescriptor descriptor = new EditLocationDescriptorBuilder(firstLocation).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_LOCATION, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_LOCATION);
    }

    @Test
    public void execute_duplicateLocationFilteredList_failure() {
        showLocationAtIndex(model, INDEX_FIRST_LOCATION);

        // edit location in filtered list into a duplicate in address book
        Location locationInList = model.getAddressBook().getLocationList().get(INDEX_SECOND_LOCATION.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_LOCATION,
                new EditLocationDescriptorBuilder(locationInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_LOCATION);
    }

    @Test
    public void execute_invalidLocationIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredLocationList().size() + 1);
        EditLocationDescriptor descriptor = new EditLocationDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model,
                Messages.getInvalidLocationDisplayedIndexMessage(model.getFilteredLocationList().size()));
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidLocationIndexFilteredList_failure() {
        showLocationAtIndex(model, INDEX_FIRST_LOCATION);
        Index outOfBoundIndex = INDEX_SECOND_LOCATION;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getLocationList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditLocationDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model,
                Messages.getInvalidLocationDisplayedIndexMessage(model.getFilteredLocationList().size()));
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_LOCATION, DESC_AMY);

        // same values -> returns true
        EditLocationDescriptor copyDescriptor = new EditLocationDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_LOCATION, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_LOCATION, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_LOCATION, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditLocationDescriptor editLocationDescriptor = new EditLocationDescriptor();
        EditCommand editCommand = new EditCommand(index, editLocationDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", editLocationDescriptor="
                + editLocationDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

    /**
     * Helper function to make edit tests for add/remove tags/dates easier.
     */
    private void assertEditSuccess(Index index, EditLocationDescriptor descriptor, Location editedLocation) {
        Location locationToEdit = model.getFilteredLocationList().get(index.getZeroBased());
        EditCommand editCommand = new EditCommand(index, descriptor);

        String expectedMessage = String.format(
                EditCommand.MESSAGE_EDIT_LOCATION_SUCCESS, Messages.format(editedLocation));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                new ShortcutMap());
        expectedModel.setLocation(locationToEdit, editedLocation);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addTagUnfilteredList_success() {
        Location locationToEdit = model.getFilteredLocationList().get(INDEX_FIRST_LOCATION.getZeroBased());
        Location editedLocation = new LocationBuilder(locationToEdit)
                .withTags("friends", VALID_TAG_HUSBAND)
                .build();

        EditLocationDescriptor descriptor = new EditLocationDescriptorBuilder()
                .withTagsToAdd(VALID_TAG_HUSBAND)
                .build();

        assertEditSuccess(INDEX_FIRST_LOCATION, descriptor, editedLocation);
    }

    @Test
    public void execute_removeTagUnfilteredList_success() {
        Location locationToEdit = model.getFilteredLocationList().get(INDEX_FIRST_LOCATION.getZeroBased());
        Location editedLocation = new LocationBuilder(locationToEdit)
                .withTags()
                .build();

        EditLocationDescriptor descriptor = new EditLocationDescriptorBuilder()
                .withTagsToRemove("friends")
                .build();

        assertEditSuccess(INDEX_FIRST_LOCATION, descriptor, editedLocation);
    }

    @Test
    public void execute_addAndRemoveTagsUnfilteredList_success() {
        Location locationToEdit = model.getFilteredLocationList().get(INDEX_SECOND_LOCATION.getZeroBased());
        Location editedLocation = new LocationBuilder(locationToEdit)
                .withTags("owesMoney", VALID_TAG_HUSBAND)
                .build();

        EditLocationDescriptor descriptor = new EditLocationDescriptorBuilder()
                .withTagsToAdd(VALID_TAG_HUSBAND)
                .withTagsToRemove("friends")
                .build();

        assertEditSuccess(INDEX_SECOND_LOCATION, descriptor, editedLocation);
    }

    @Test
    public void execute_addExistingTagUnfilteredList_success() {
        Location locationToEdit = model.getFilteredLocationList().get(INDEX_FIRST_LOCATION.getZeroBased());
        Location editedLocation = new LocationBuilder(locationToEdit).build();

        EditLocationDescriptor descriptor = new EditLocationDescriptorBuilder()
                .withTagsToAdd("friends")
                .build();

        assertEditSuccess(INDEX_FIRST_LOCATION, descriptor, editedLocation);
    }

    @Test
    public void execute_removeNonExistingTagUnfilteredList_success() {
        Location locationToEdit = model.getFilteredLocationList().get(INDEX_FIRST_LOCATION.getZeroBased());
        Location editedLocation = new LocationBuilder(locationToEdit).build();

        EditLocationDescriptor descriptor = new EditLocationDescriptorBuilder()
                .withTagsToRemove(VALID_TAG_HUSBAND)
                .build();

        assertEditSuccess(INDEX_FIRST_LOCATION, descriptor, editedLocation);
    }

    @Test
    public void execute_addAndRemoveSameTagUnfilteredList_success() {
        Location locationToEdit = model.getFilteredLocationList().get(INDEX_FIRST_LOCATION.getZeroBased());
        Location editedLocation = new LocationBuilder(locationToEdit)
                .withTags()
                .build();

        EditLocationDescriptor descriptor = new EditLocationDescriptorBuilder()
                .withTagsToAdd("friends")
                .withTagsToRemove("friends")
                .build();

        assertEditSuccess(INDEX_FIRST_LOCATION, descriptor, editedLocation);
    }

    @Test
    public void execute_addDateUnfilteredList_success() {
        Location locationToEdit = model.getFilteredLocationList().get(INDEX_FIRST_LOCATION.getZeroBased());
        Location editedLocation = new LocationBuilder(locationToEdit)
                .withVisitDates("2026-01-07", "2026-01-10", "2026-02-10")
                .build();

        EditLocationDescriptor descriptor = new EditLocationDescriptorBuilder()
                .withVisitDatesToAdd("2026-02-10")
                .build();

        assertEditSuccess(INDEX_FIRST_LOCATION, descriptor, editedLocation);
    }

    @Test
    public void execute_addAndRemoveSameDateUnfilteredList_success() {
        Location locationToEdit = model.getFilteredLocationList().get(INDEX_FIRST_LOCATION.getZeroBased());
        Location editedLocation = new LocationBuilder(locationToEdit)
                .withVisitDates("2026-01-07")
                .build();

        EditLocationDescriptor descriptor = new EditLocationDescriptorBuilder()
                .withVisitDatesToAdd("2026-01-10")
                .withVisitDatesToRemove("2026-01-10")
                .build();

        assertEditSuccess(INDEX_FIRST_LOCATION, descriptor, editedLocation);
    }

    @Test
    public void execute_editPostalCodeUnfilteredList_success() {
        Location locationToEdit = model.getFilteredLocationList().get(INDEX_FIRST_LOCATION.getZeroBased());
        Location editedLocation = new LocationBuilder(locationToEdit)
                .withPostalCode(VALID_POSTAL_CODE_BOB)
                .build();

        EditLocationDescriptor descriptor = new EditLocationDescriptorBuilder()
                .withPostalCode(VALID_POSTAL_CODE_BOB)
                .build();

        assertEditSuccess(INDEX_FIRST_LOCATION, descriptor, editedLocation);
    }
}
