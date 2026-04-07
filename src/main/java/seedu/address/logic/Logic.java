package seedu.address.logic;

import java.nio.file.Path;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.Theme;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.location.Location;
import seedu.address.model.location.NoteContent;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /**
     * Returns the AddressBook.
     *
     * @see seedu.address.model.Model#getAddressBook()
     */
    ReadOnlyAddressBook getAddressBook();

    /** Returns an unmodifiable view of the filtered list of locations */
    ObservableList<Location> getFilteredLocationList();

    /** Returns an unmodifiable view of the locations to be displayed in the planner */
    ObservableList<Location> getPlannerLocationList();

    /** Returns an observable property pointing to the current active note in the planner */
    ObservableValue<NoteContent> getPlannerNoteProperty();

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Returns the user prefs' theme.
     */
    Theme getTheme();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Sets the user prefs' theme.
     */
    void setTheme(Theme theme);

    /**
     * Returns the user command history
     */
    CliHistory getCommandHistory();
}
