package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.location.Location;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_LOCATION = "Locations list contains duplicate location(s).";

    private final List<JsonAdaptedLocation> locations = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given locations.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("locations") List<JsonAdaptedLocation> locations) {
        this.locations.addAll(locations);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        locations.addAll(source.getLocationList().stream().map(JsonAdaptedLocation::new).collect(Collectors.toList()));
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
        return addressBook;
    }

}
