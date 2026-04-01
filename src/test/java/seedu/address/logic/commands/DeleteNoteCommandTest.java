package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.location.dates.VisitDate;

public class DeleteNoteCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        expectedModel = new ModelManager();
    }

    @Test
    public void execute_deleteNote_success() throws IllegalValueException {
        DeleteNoteCommand command = new DeleteNoteCommand(VisitDate.of("2026-03-24"));
        String expectedMessage = String.format(DeleteNoteCommand.MESSAGE_SUCCESS, "24 Mar 26");

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
}
