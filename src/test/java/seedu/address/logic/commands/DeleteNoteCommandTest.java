package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.location.Location;
import seedu.address.model.location.dates.VisitDate;
import seedu.address.testutil.LocationBuilder;

public class DeleteNoteCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        expectedModel = new ModelManager();
    }

    @Test
    public void execute_deleteNote_success() throws Exception {
        Location location = new LocationBuilder()
                .withName("Test Place")
                .withNote("2026-03-24", "Great place")
                .build();
        model.addLocation(location);

        Location editedLocation = location.removeNotesByDate(VisitDate.of("2026-03-24"));
        expectedModel.addLocation(editedLocation);

        DeleteNoteCommand command = new DeleteNoteCommand(VisitDate.of("2026-03-24"));
        String expectedMessage = String.format(DeleteNoteCommand.MESSAGE_SUCCESS, VisitDate.of("2026-03-24"));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() throws IllegalValueException {
        DeleteNoteCommand first = new DeleteNoteCommand(VisitDate.of("2026-03-24"));
        DeleteNoteCommand second = new DeleteNoteCommand(VisitDate.of("2026-03-24"));
        DeleteNoteCommand third = new DeleteNoteCommand(VisitDate.of("2026-03-25"));

        assertEquals(first, first);
        assertEquals(first, second);
        assertNotEquals(first, third);
        assertNotEquals(first, null);
        assertNotEquals(first, new Object());
    }

    @Test
    public void getDate() throws IllegalValueException {
        VisitDate date = VisitDate.of("2026-03-24");
        DeleteNoteCommand command = new DeleteNoteCommand(date);

        assertEquals(date, command.getDate());
    }

    @Test
    public void toStringMethod() throws IllegalValueException {
        DeleteNoteCommand command = new DeleteNoteCommand(VisitDate.of("2026-03-24"));

        String expected = DeleteNoteCommand.class.getCanonicalName() + "{date=2026-03-24}";
        assertEquals(expected, command.toString());
    }

    @Test
    public void execute_noNotesOnDate_throwsCommandException() throws Exception {
        Location location = new LocationBuilder()
                .withName("Test Place")
                .build();
        model.addLocation(location);
        expectedModel.addLocation(location);

        DeleteNoteCommand command = new DeleteNoteCommand(VisitDate.of("2026-03-24"));

        assertCommandFailure(command, model, DeleteNoteCommand.MESSAGE_NO_NOTES_FOUND);
    }
}
