package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.location.NoteContent;
import seedu.address.model.location.dates.VisitDate;

public class NoteCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        expectedModel = new ModelManager();
    }

    @Test
    public void execute_noteWithDate_success() throws IllegalValueException {
        NoteCommand command = new NoteCommand(new NoteContent("Great place"), VisitDate.of("2026-03-24"));
        String expectedMessage = String.format(NoteCommand.MESSAGE_SUCCESS, "Great place (24 Mar 26)");

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() throws IllegalValueException {
        NoteCommand first = new NoteCommand(new NoteContent("Great place"), VisitDate.of("2026-03-24"));
        NoteCommand second = new NoteCommand(new NoteContent("Great place"), VisitDate.of("2026-03-24"));
        NoteCommand third = new NoteCommand(new NoteContent("Different note"), VisitDate.of("2026-03-24"));

        assertEquals(first, first);
        assertEquals(first, second);
        assertNotEquals(first, third);
        assertNotEquals(first, null);
        assertNotEquals(first, new Object());
    }
}
