package seedu.address.model.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.location.Address;
import seedu.address.model.location.Email;
import seedu.address.model.location.Location;
import seedu.address.model.location.Name;
import seedu.address.model.location.Phone;
import seedu.address.model.location.PostalCode;
import seedu.address.model.location.dates.VisitDate;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {

    @SuppressWarnings("checkstyle:Indentation")
    public static Location[] getSampleLocations() {
        return new Location[]{new Location(
                        new Name("Sunrise Cafe"),
                        Optional.of(new Phone("67438807")),
                        Optional.of(new Email("contact@sunrisecafe.com")),
                        Optional.of(new Address("10 Morning Blvd, #01-01")),
                        Optional.of(new PostalCode("408615")),
                        getVisitDateSet("2026-03-12", "2026-04-12"),
                        getTagSet("cafe", "breakfast"),
                        getNoteMap("2026-03-12", "Test note")
                ), new Location(
                        new Name("HarbourBack Hotel"),
                        Optional.of(new Phone("69272758")),
                        Optional.of(new Email("stay@harbourbackhotel.com")),
                        Optional.of(new Address("200 Marina Promenade, #28-01")),
                        Optional.of(new PostalCode("554530")),
                        getVisitDateSet("2026-03-18"),
                        getTagSet("hotel", "stay"),
                        new HashMap<>()
                ), new Location(
                        new Name("PastaHut"),
                        Optional.of(new Phone("63210283")),
                        Optional.of(new Email("contact@pastahut.com")),
                        Optional.of(new Address("5 Little Italy Rd, #02-10")),
                        Optional.of(new PostalCode("560011")),
                        getVisitDateSet("2026-03-13"),
                        getTagSet("restaurant", "italian"),
                        new HashMap<>()
                ), new Location(
                        new Name("Metro Inn"),
                        Optional.of(new Phone("61031282")),
                        Optional.of(new Email("reservations@metroinn.com")),
                        Optional.of(new Address("12 Queen St, #05-02")),
                        Optional.of(new PostalCode("550436")),
                        getVisitDateSet("2026-03-21"),
                        getTagSet("hotel", "stay", "budget"),
                        new HashMap<>()
                ), new Location(
                        new Name("Green Garden Restaurant"),
                        Optional.of(new Phone("62492021")),
                        Optional.of(new Email("hello@greengarden.sg")),
                        Optional.of(new Address("78 Orchard Road, #03-05")),
                        Optional.of(new PostalCode("520047")),
                        getVisitDateSet("2026-03-05"),
                        getTagSet("restaurant", "vegetarian"),
                        new HashMap<>()
                ), new Location(
                        new Name("Museum of Contemporary Fruit"),
                        Optional.of(new Phone("62624417")),
                        Optional.of(new Email("contact@museumofcontemporaryfruit.com")),
                        Optional.of(new Address("15 Art Lane, #01-01")),
                        Optional.of(new PostalCode("380045")),
                        getVisitDateSet("2026-03-01"),
                        getTagSet("museum", "art", "attraction"),
                        getNoteMap("2026-03-01", "Good Place to Visit")
                )
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Location sampleLocation : getSampleLocations()) {
            sampleAb.addLocation(sampleLocation);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a visit date set containing the list of strings given.
     */
    public static Set<VisitDate> getVisitDateSet(String... dates) {
        return Arrays.stream(dates)
                .map(VisitDate::safeOf)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a note map containing one note for the given visit date.
     */
    public static HashMap<VisitDate, String> getNoteMap(String date, String note) {
        HashMap<VisitDate, String> notes = new HashMap<>();
        notes.put(VisitDate.safeOf(date), note);
        return notes;
    }
}
