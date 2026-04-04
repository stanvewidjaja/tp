package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalLocations.ALICE;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.Theme;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyShortcutMap;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.location.Location;
import seedu.address.testutil.LocationBuilder;

public class AddCommandTest {

    @Test
    public void constructor_nullLocation_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }

    @Test
    public void execute_locationAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingLocationAdded modelStub = new ModelStubAcceptingLocationAdded();
        Location validLocation = new LocationBuilder()
                .withName("McDonalds Bugis")
                .withPhone("67773777")
                .withEmail("customercare@sg.mcd.com")
                .withAddress("113 Aljunied Ave 2")
                .withTags("restaurant", "fastfood")
                .build();

        CommandResult commandResult = new AddCommand(validLocation).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validLocation)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validLocation), modelStub.locationsAdded);
    }

    @Test
    public void execute_duplicateLocation_throwsCommandException() {
        Location validLocation = new LocationBuilder()
                .withName("McDonalds Bugis")
                .withPhone("67773777")
                .withEmail("customercare@sg.mcd.com")
                .withAddress("113 Aljunied Ave 2")
                .withTags("restaurant", "fastfood")
                .build();
        AddCommand addCommand = new AddCommand(validLocation);
        ModelStub modelStub = new ModelStubWithLocation(validLocation);

        assertThrows(CommandException.class,
                AddCommand.MESSAGE_DUPLICATE_LOCATION, () -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Location marinaBaySands = new LocationBuilder().withName("Marina Bay Sands").build();
        Location gardensByTheBay = new LocationBuilder().withName("Gardens by the Bay").build();
        AddCommand addMarinaBaySandsCommand = new AddCommand(marinaBaySands);
        AddCommand addGardensByTheBayCommand = new AddCommand(gardensByTheBay);

        // same object -> returns true
        assertTrue(addMarinaBaySandsCommand.equals(addMarinaBaySandsCommand));

        // same values -> returns true
        AddCommand addMarinaBaySandsCommandCopy = new AddCommand(marinaBaySands);
        assertTrue(addMarinaBaySandsCommand.equals(addMarinaBaySandsCommandCopy));

        // different types -> returns false
        assertFalse(addMarinaBaySandsCommand.equals(1));

        // null -> returns false
        assertFalse(addMarinaBaySandsCommand.equals(null));

        // different location -> returns false
        assertFalse(addMarinaBaySandsCommand.equals(addGardensByTheBayCommand));
    }

    @Test
    public void toStringMethod() {
        AddCommand addCommand = new AddCommand(ALICE);
        String expected = AddCommand.class.getCanonicalName() + "{toAdd=" + ALICE + "}";
        assertEquals(expected, addCommand.toString());
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Theme getTheme() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setTheme(Theme theme) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyShortcutMap getShortcutMap() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasShortcut(String alias) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setShortcut(String alias, String commandWord) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void removeShortcut(String alias) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addLocation(Location location) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasLocation(Location location) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteLocation(Location target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setLocation(Location target, Location editedLocation) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Location> getFilteredLocationList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Location> getPlannerLocationList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredLocationList(Predicate<Location> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updatePlannerLocationList(LocalDate date) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setNote(seedu.address.model.location.dates.VisitDate date,
                seedu.address.model.location.NoteContent note) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public javafx.beans.value.ObservableValue<seedu.address.model.location.NoteContent> getPlannerNoteProperty() {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single location.
     */
    private class ModelStubWithLocation extends ModelStub {
        private final Location location;

        ModelStubWithLocation(Location location) {
            requireNonNull(location);
            this.location = location;
        }

        @Override
        public boolean hasLocation(Location location) {
            requireNonNull(location);
            return this.location.isSameLocation(location);
        }
    }

    /**
     * A Model stub that always accept the location being added.
     */
    private class ModelStubAcceptingLocationAdded extends ModelStub {
        final ArrayList<Location> locationsAdded = new ArrayList<>();

        @Override
        public boolean hasLocation(Location location) {
            requireNonNull(location);
            return locationsAdded.stream().anyMatch(location::isSameLocation);
        }

        @Override
        public void addLocation(Location location) {
            requireNonNull(location);
            locationsAdded.add(location);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
