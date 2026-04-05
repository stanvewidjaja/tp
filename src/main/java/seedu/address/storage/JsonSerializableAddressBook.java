package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.location.Location;
import seedu.address.model.location.NoteContent;
import seedu.address.model.location.dates.VisitDate;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_LOCATION = "Locations list contains duplicate location(s).";

    private final List<JsonAdaptedLocation> locations = new ArrayList<>();
    private final Map<String, String> notes = new HashMap<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given locations.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("locations") List<JsonAdaptedLocation> locations,
                                       @JsonProperty("notes") Map<String, String> notes) {
        if (locations != null) {
            this.locations.addAll(locations);
        }
        if (notes != null) {
            this.notes.putAll(notes);
        }
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        locations.addAll(source.getLocationList().stream().map(JsonAdaptedLocation::new).collect(Collectors.toList()));
        source.getNoteMap().forEach((date, note) -> notes.put(date.toDataString(), note.toString()));
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        for (JsonAdaptedLocation jsonAdaptedLocation : locations) {
            Location location = jsonAdaptedLocation.toModelType();
            if (addressBook.hasLocation(location)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_LOCATION);
            }
            addressBook.addLocation(location);
        }
        for (Map.Entry<String, String> entry : notes.entrySet()) {
            String dateKey = entry.getKey();
            VisitDate visitDate = VisitDate.of(dateKey);
            String noteValue = entry.getValue();
            NoteContent noteContent;
            if (noteValue == null) {
                throw new IllegalValueException("Note value is missing for date '" + dateKey + "'. "
                        + NoteContent.MESSAGE_CONSTRAINTS);
            }
            if (NoteContent.isValidNoteContent(noteValue)) {
                noteContent = new NoteContent(noteValue);
            } else {
                throw new IllegalValueException("Invalid note value for date '" + dateKey + "'. "
                        + NoteContent.MESSAGE_CONSTRAINTS);
            }
            addressBook.setNote(visitDate, noteContent);
        }
        return addressBook;
    }

}
