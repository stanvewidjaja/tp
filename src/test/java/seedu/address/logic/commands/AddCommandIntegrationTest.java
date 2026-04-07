package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalLocations.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ShortcutMap;
import seedu.address.model.UserPrefs;
import seedu.address.model.location.Location;
import seedu.address.testutil.LocationBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new ShortcutMap());
    }

    @Test
    public void execute_newLocation_success() {
        Location validLocation = new LocationBuilder()
                .withName("New Place")
                .withAddress("77 Sunset Way")
                .withPostalCode("765432")
                .build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), new ShortcutMap());
        expectedModel.addLocation(validLocation);

        assertCommandSuccess(new AddCommand(validLocation), model,
                String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validLocation)),
                expectedModel);
    }

    @Test
    public void execute_duplicateLocation_throwsCommandException() {
        Location locationInList = model.getAddressBook().getLocationList().get(0);
        assertCommandFailure(new AddCommand(locationInList), model,
                AddCommand.MESSAGE_DUPLICATE_LOCATION);
    }

}
