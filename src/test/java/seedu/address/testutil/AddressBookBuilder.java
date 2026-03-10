package seedu.address.testutil;

import seedu.address.model.AddressBook;
import seedu.address.model.location.Location;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code AddressBook ab = new AddressBookBuilder().withLocation("John", "Doe").build();}
 */
public class AddressBookBuilder {

    private AddressBook addressBook;

    public AddressBookBuilder() {
        addressBook = new AddressBook();
    }

    public AddressBookBuilder(AddressBook addressBook) {
        this.addressBook = addressBook;
    }

    /**
     * Adds a new {@code Location} to the {@code AddressBook} that we are building.
     */
    public AddressBookBuilder withLocation(Location location) {
        addressBook.addLocation(location);
        return this;
    }

    public AddressBook build() {
        return addressBook;
    }
}
