package seedu.address.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Theme;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandDatabase;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.location.Location;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final AddressBookParser addressBookParser;
    private final CliHistory cliHistory;
    private final ShortcutManager shortcutManager;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        this.cliHistory = new CliHistory();
        addressBookParser = new AddressBookParser();
        shortcutManager = new ShortcutManager(model, new CommandDatabase());
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        cliHistory.addInput(commandText);

        CommandResult commandResult;

        String expandedCommandText = shortcutManager.expandShortcut(commandText);
        try {
            Command command = addressBookParser.parseCommand(expandedCommandText);
            commandResult = executeWithModelState(command);
        } catch (ParseException e) {
            // since it is error, the most recent history remains in the box
            cliHistory.setPointerMostRecent();
            throw e;
        }

        saveAddressBook();
        saveShortcuts();
        // Save user preferences as a best-effort operation. Failures here should not cause the entire
        // command to be reported as failed when the address book has already been successfully persisted.
        savePreferences();

        return commandResult;
    }

    private CommandResult executeWithModelState(Command command) throws ParseException, CommandException {
        CommandResult commandResult;

        if (command.isStateMutating()) {
            model.saveState();
        }

        try {
            commandResult = command.execute(model);
            if (command.isStateMutating()) {
                model.commitState();
            }
        } catch (CommandException | RuntimeException e) {
            if (command.isStateMutating()) {
                model.discardState();
            }
            throw e;
        }

        return commandResult;
    }

    private void saveAddressBook() throws CommandException {
        try {
            storage.saveAddressBook(model.getAddressBook());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }
    }

    private void saveShortcuts() {
        try {
            storage.saveShortcutMap(model.getShortcutMap());
        } catch (AccessDeniedException e) {
            logger.warning(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()));
        } catch (IOException ioe) {
            logger.warning(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()));
        }
    }

    private void savePreferences() {
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (AccessDeniedException e) {
            logger.warning(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()));
        } catch (IOException ioe) {
            logger.warning(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()));
        }
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }

    @Override
    public ObservableList<Location> getFilteredLocationList() {
        return model.getFilteredLocationList();
    }

    @Override
    public ObservableList<Location> getPlannerLocationList() {
        return model.getPlannerLocationList();
    }

    @Override
    public javafx.beans.value.ObservableValue<seedu.address.model.location.NoteContent> getPlannerNoteProperty() {
        return model.getPlannerNoteProperty();
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public Theme getTheme() {
        return model.getTheme();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }

    @Override
    public void setTheme(Theme theme) {
        model.setTheme(theme);
    }

    @Override
    public CliHistory getCommandHistory() {
        return this.cliHistory;
    }
}
