package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_LOCATIONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalLocations.CARL;
import static seedu.address.testutil.TypicalLocations.DANIEL;
import static seedu.address.testutil.TypicalLocations.ELLE;
import static seedu.address.testutil.TypicalLocations.FIONA;
import static seedu.address.testutil.TypicalLocations.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.location.NameContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("second"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different location -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noLocationFound() {
        String expectedMessage = String.format(MESSAGE_LOCATIONS_LISTED_OVERVIEW, 0);
        NameContainsKeywordsPredicate predicate = preparePredicate(" ");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredLocationList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredLocationList());
    }

    @Test
    public void execute_multipleKeywords_multipleLocationsFound() {
        String expectedMessage = String.format(MESSAGE_LOCATIONS_LISTED_OVERVIEW, 3);
        NameContainsKeywordsPredicate predicate = preparePredicate("Kurz Elle Kunz");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredLocationList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredLocationList());
    }

    @Test
    public void execute_substringMatch_locationFound() {
        // Substring "Car" matches "Carl Kurz"
        String expectedMessage = String.format(MESSAGE_LOCATIONS_LISTED_OVERVIEW, 1);
        NameContainsKeywordsPredicate predicate = preparePredicate("Car");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredLocationList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL), model.getFilteredLocationList());
    }

    @Test
    public void execute_substringPrefixMatch_locationFound() {
        // Substring "El" (prefix) matches "Elle Meyer" and "Daniel Meier"
        String expectedMessage = String.format(MESSAGE_LOCATIONS_LISTED_OVERVIEW, 2);
        NameContainsKeywordsPredicate predicate = preparePredicate("El");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredLocationList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(DANIEL, ELLE), model.getFilteredLocationList());
    }

    @Test
    public void execute_substringMiddleMatch_locationFound() {
        // Substring in the middle of a word matches "Carl Kurz"
        String expectedMessage = String.format(MESSAGE_LOCATIONS_LISTED_OVERVIEW, 1);
        NameContainsKeywordsPredicate predicate = preparePredicate("ar");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredLocationList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL), model.getFilteredLocationList());
    }

    @Test
    public void execute_multipleSubstringKeywords_multipleLocationsFound() {
        // Multiple substring keywords with OR logic
        String expectedMessage = String.format(MESSAGE_LOCATIONS_LISTED_OVERVIEW, 2);
        NameContainsKeywordsPredicate predicate = preparePredicate("Mey Kun");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredLocationList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ELLE, FIONA), model.getFilteredLocationList());
    }

    @Test
    public void toStringMethod() {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("keyword"));
        FindCommand findCommand = new FindCommand(predicate);
        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
