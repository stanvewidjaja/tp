package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.POSTAL_CODE_DESC_AMY;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalLocations.AMY;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.Theme;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.DeleteNoteCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.NoteCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.ShortcutCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyShortcutMap;
import seedu.address.model.UserPrefs;
import seedu.address.model.location.Location;
import seedu.address.model.location.NoteContent;
import seedu.address.model.location.dates.VisitDate;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonShortcutStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.LocationBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION = new AccessDeniedException("dummy access denied exception");

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        JsonShortcutStorage shortcutStorage =
                new JsonShortcutStorage(temporaryFolder.resolve("shortcut.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage, shortcutStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete 9";
        assertCommandException(deleteCommand, Messages.getInvalidLocationDisplayedIndexMessage(0));
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_shortcutExpansion_success() throws Exception {
        logic.execute(ShortcutCommand.COMMAND_WORD + " set a add");

        String aliasedAddCommand = "a"
                + NAME_DESC_AMY
                + PHONE_DESC_AMY
                + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY
                + POSTAL_CODE_DESC_AMY
                + DATE_DESC_AMY;
        Location expectedLocation = new LocationBuilder(AMY).withTags().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addLocation(expectedLocation);
        expectedModel.setShortcut("a", "add");

        assertCommandSuccess(aliasedAddCommand, String.format(AddCommand.MESSAGE_SUCCESS,
                Messages.format(expectedLocation)), expectedModel);
    }

    @Test
    public void execute_undoRedoAddressBookChange_success() throws Exception {
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + POSTAL_CODE_DESC_AMY + DATE_DESC_AMY;

        logic.execute(addCommand);
        assertCommandSuccess(UndoCommand.COMMAND_WORD, UndoCommand.MESSAGE_SUCCESS, new ModelManager());

        ModelManager expectedModel = new ModelManager();
        Location expectedLocation = new LocationBuilder(AMY).withTags().build();
        expectedModel.addLocation(expectedLocation);
        assertCommandSuccess(RedoCommand.COMMAND_WORD, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_undoRedoShortcutChange_success() throws Exception {
        assertCommandSuccess(ShortcutCommand.COMMAND_WORD + " set a add",
                String.format(ShortcutCommand.MESSAGE_SET_SUCCESS, "a", "add"),
                createModelWithShortcut("a", "add"));

        assertCommandSuccess(UndoCommand.COMMAND_WORD, UndoCommand.MESSAGE_SUCCESS, new ModelManager());
        assertCommandSuccess(RedoCommand.COMMAND_WORD, RedoCommand.MESSAGE_SUCCESS,
                createModelWithShortcut("a", "add"));
    }

    @Test
    public void execute_nonMutatingShortcutList_doesNotAffectRedoState() throws Exception {
        logic.execute(ShortcutCommand.COMMAND_WORD + " set a add");
        logic.execute(UndoCommand.COMMAND_WORD);

        Model expectedModel = new ModelManager();
        assertCommandSuccess(ShortcutCommand.COMMAND_WORD + " list",
                "No shortcuts defined.", expectedModel);
        assertCommandSuccess(RedoCommand.COMMAND_WORD, RedoCommand.MESSAGE_SUCCESS,
                createModelWithShortcut("a", "add"));
    }

    @Test
    public void execute_undoAfterFailedCommand_preservesPreviousUndoState() throws Exception {
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + POSTAL_CODE_DESC_AMY + DATE_DESC_AMY;

        logic.execute(addCommand);
        assertCommandException("delete 9", Messages.getInvalidLocationDisplayedIndexMessage(1));
        assertCommandSuccess(UndoCommand.COMMAND_WORD, UndoCommand.MESSAGE_SUCCESS, new ModelManager());
    }

    @Test
    public void execute_undoWithoutHistory_throwsCommandException() {
        assertCommandException(UndoCommand.COMMAND_WORD, UndoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void execute_redoWithoutHistory_throwsCommandException() {
        assertCommandException(RedoCommand.COMMAND_WORD, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_IO_EXCEPTION,
                String.format(LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_storageThrowsAdException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_AD_EXCEPTION,
                String.format(LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT, DUMMY_AD_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_shortcutStorageThrowsIoException_commandSucceeds() throws Exception {
        assertCommandSuccessForExceptionFromShortcutStorage(DUMMY_IO_EXCEPTION);
    }

    @Test
    public void execute_shortcutStorageThrowsAdException_commandSucceeds() throws Exception {
        assertCommandSuccessForExceptionFromShortcutStorage(DUMMY_AD_EXCEPTION);
    }

    @Test
    public void getFilteredLocationList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredLocationList().remove(0));
    }

    @Test
    public void getPlannerLocationList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getPlannerLocationList().remove(0));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown
     * - the feedback message is equal to {@code expectedMessage}
     * - the internal model manager state is the same as that in {@code expectedModel}
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage, Model expectedModel)
            throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown
     * and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown
     * and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */

    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     *
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
                                      String expectedMessage) {
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(model.getUserPrefs()),
                model.getShortcutMap());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown
     * - the resulting error message is equal to {@code expectedMessage}
     * - the internal model manager state is the same as that in {@code expectedModel}
     *
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
                                      String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * Tests the Logic component's handling of an {@code IOException} thrown by the Storage component.
     *
     * @param e               the exception to be thrown by the Storage component
     * @param expectedMessage the message expected inside exception thrown by the Logic component
     */
    private void assertCommandFailureForExceptionFromStorage(IOException e, String expectedMessage) {
        Path prefPath = temporaryFolder.resolve("ExceptionUserPrefs.json");

        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(prefPath) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
                throw e;
            }
        };

        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ExceptionUserPrefs.json"));
        JsonShortcutStorage shortcutStorage =
                new JsonShortcutStorage(temporaryFolder.resolve("ExceptionShortcut.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage, shortcutStorage);
        logic = new LogicManager(model, storage);

        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + POSTAL_CODE_DESC_AMY + DATE_DESC_AMY;

        Location expectedLocation = new LocationBuilder(AMY).withTags().build();

        ModelManager expectedModel = new ModelManager();
        expectedModel.addLocation(expectedLocation);

        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }

    /**
     * Tests the Logic component's handling of an {@code IOException} thrown while saving shortcuts.
     * Shortcut persistence is best-effort, so command execution should still succeed.
     *
     * @param e the exception to be thrown by the shortcut storage component
     */
    private void assertCommandSuccessForExceptionFromShortcutStorage(IOException e) throws Exception {
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("ExceptionAddressBook.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ExceptionUserPrefs.json"));
        JsonShortcutStorage shortcutStorage =
                new JsonShortcutStorage(temporaryFolder.resolve("ExceptionShortcut.json")) {
                    @Override
                    public void saveShortcutMap(ReadOnlyShortcutMap shortcutMap, Path filePath) throws IOException {
                        throw e;
                    }
                };

        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage, shortcutStorage);
        logic = new LogicManager(model, storage);

        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + POSTAL_CODE_DESC_AMY + DATE_DESC_AMY;

        Location expectedLocation = new LocationBuilder(AMY).withTags().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addLocation(expectedLocation);

        assertCommandSuccess(addCommand, String.format(AddCommand.MESSAGE_SUCCESS,
                Messages.format(expectedLocation)), expectedModel);
    }

    private ModelManager createModelWithTheme(Theme theme) {
        ModelManager expectedModel = new ModelManager();
        expectedModel.setTheme(theme);
        return expectedModel;
    }

    private ModelManager createModelWithShortcut(String alias, String commandWord) {
        ModelManager expectedModel = new ModelManager();
        expectedModel.setShortcut(alias, commandWord);
        return expectedModel;
    }

    @Test
    public void execute_addNote_success() throws Exception {
        String inputCommand = "note n/Test Note d/2026-03-24";

        ModelManager expectedModel = new ModelManager();
        expectedModel.setNote(VisitDate.of("2026-03-24"), new NoteContent("Test Note"));

        assertCommandSuccess(inputCommand,
                String.format(NoteCommand.MESSAGE_SUCCESS, "Test Note (24 Mar 26)"),
                expectedModel);
    }

    @Test
    public void execute_deleteNote_success() throws Exception {
        model.setNote(VisitDate.of("2026-03-24"), new NoteContent("Test Note"));

        String inputCommand = "note d-/2026-03-24";

        ModelManager expectedModel = new ModelManager();

        assertCommandSuccess(inputCommand,
                String.format(DeleteNoteCommand.MESSAGE_SUCCESS, VisitDate.of("2026-03-24")),
                expectedModel);
    }

    @Test
    public void execute_deleteNote_missingNote() {
        String inputCommand = "note d-/2026-03-24";

        assertCommandException(inputCommand, DeleteNoteCommand.MESSAGE_NO_NOTES_FOUND);
    }
}

