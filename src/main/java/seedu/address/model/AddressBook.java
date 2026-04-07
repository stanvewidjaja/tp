package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.location.Location;
import seedu.address.model.location.NoteContent;
import seedu.address.model.location.UniqueLocationList;
import seedu.address.model.location.dates.VisitDate;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSameLocation comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniqueLocationList locations;
    private final Map<VisitDate, NoteContent> notes;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        locations = new UniqueLocationList();
        notes = new LinkedHashMap<>();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Locations in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the location list with {@code locations}.
     * {@code locations} must not contain duplicate locations.
     */
    public void setLocations(List<Location> locations) {
        this.locations.setLocations(locations);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setLocations(newData.getLocationList());
        setNotes(newData.getNoteMap());
    }

    //// note-level operations

    /**
     * Replaces the contents of the notes map with {@code notes}.
     */
    public void setNotes(Map<VisitDate, NoteContent> notes) {
        requireNonNull(notes);
        notes.forEach((date, note) -> {
            requireNonNull(date);
            requireNonNull(note);
        });
        this.notes.clear();
        this.notes.putAll(notes);
    }

    /**
     * Sets a note for the specified date.
     */
    public void setNote(VisitDate date, NoteContent note) {
        requireNonNull(date);
        requireNonNull(note);
        notes.put(date, note);
    }

    /**
     * Removes the note for the specified date if it exists.
     */
    public void removeNote(VisitDate date) {
        requireNonNull(date);
        notes.remove(date);
    }

    @Override
    public Map<VisitDate, NoteContent> getNoteMap() {
        return Collections.unmodifiableMap(notes);
    }


    //// location-level operations

    /**
     * Returns true if a location with the same identity as {@code location} exists in the address book.
     */
    public boolean hasLocation(Location location) {
        requireNonNull(location);
        return locations.contains(location);
    }

    /**
     * Adds a location to the address book.
     * The location must not already exist in the address book.
     */
    public void addLocation(Location p) {
        locations.add(p);
    }

    /**
     * Replaces the given location {@code target} in the list with {@code editedLocation}.
     * {@code target} must exist in the address book.
     * The location identity of {@code editedLocation} must not be the same as another existing one in the address book.
     */
    public void setLocation(Location target, Location editedLocation) {
        requireNonNull(editedLocation);

        locations.setLocation(target, editedLocation);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeLocation(Location key) {
        locations.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("locations", locations)
                .add("notes", notes)
                .toString();
    }

    @Override
    public ObservableList<Location> getLocationList() {
        return locations.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return locations.equals(otherAddressBook.locations) && notes.equals(otherAddressBook.notes);
    }

    @Override
    public int hashCode() {
        return locations.hashCode() + notes.hashCode();
    }
}
