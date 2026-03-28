package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.PlanCommand.MESSAGE_CLEAR_SUCCESS;
import static seedu.address.logic.commands.PlanCommand.MESSAGE_DISPLAY_SUCCESS;
import static seedu.address.testutil.TypicalLocations.getTypicalAddressBook;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.DateParser;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ShortcutMap;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for RemarkCommand.
 */
public class PlanCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new ShortcutMap());

    @Test
    public void execute() {
        assertCommandSuccess(new PlanCommand(null), model, MESSAGE_CLEAR_SUCCESS, model);

        LocalDate testDate = LocalDate.of(26, 3, 17);
        assertCommandSuccess(new PlanCommand(testDate), model,
                String.format(MESSAGE_DISPLAY_SUCCESS, DateParser.dateToPrettyString(testDate)), model);
    }

    @Test
    public void equals() {
        LocalDate testDate = LocalDate.of(26, 3, 17);
        LocalDate sameDate = LocalDate.of(26, 3, 17);
        LocalDate diffDate = LocalDate.of(20, 1, 1);

        final PlanCommand standardCommand = new PlanCommand(testDate);

        // same values -> returns true
        PlanCommand commandWithSameValues = new PlanCommand(sameDate);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same date object -> returns true
        PlanCommand commandWithSameDate = new PlanCommand(testDate);
        assertTrue(standardCommand.equals(commandWithSameDate));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // both nulls -> returns true
        assertTrue(new PlanCommand(null).equals(new PlanCommand(null)));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // other is null date -> returns false
        assertFalse(standardCommand.equals(new PlanCommand(null)));

        // first is null date -> returns false
        assertFalse(new PlanCommand(null).equals(standardCommand));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different dates -> returns false
        assertFalse(standardCommand.equals(new PlanCommand(diffDate)));
    }
}
