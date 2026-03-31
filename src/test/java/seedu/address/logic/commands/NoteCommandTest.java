package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.location.Name;
import seedu.address.model.location.VisitDate;

public class NoteCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        expectedModel = new ModelManager();
    }

    @Test
    public void execute_noteWithDate_success() {
        NoteCommand command = new NoteCommand(new Name("Great place"), Optional.of(new VisitDate("2026-03-24")));
        String expectedMessage = String.format(NoteCommand.MESSAGE_SUCCESS, "Great place (2026-03-24)");

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noteWithoutDate_success() {
        NoteCommand command = new NoteCommand(new Name("Solo note"), Optional.empty());
        String expectedMessage = String.format(NoteCommand.MESSAGE_SUCCESS, "Solo note");

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        NoteCommand first = new NoteCommand(new Name("Great place"), Optional.of(new VisitDate("2026-03-24")));
        NoteCommand second = new NoteCommand(new Name("Great place"), Optional.of(new VisitDate("2026-03-24")));
        NoteCommand third = new NoteCommand(new Name("Different note"), Optional.of(new VisitDate("2026-03-24")));

        assertEquals(first, first);
        assertEquals(first, second);
        assertNotEquals(first, third);
        assertNotEquals(first, null);
        assertNotEquals(first, new Object());
    }
}
