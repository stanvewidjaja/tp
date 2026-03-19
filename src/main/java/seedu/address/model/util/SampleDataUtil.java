package seedu.address.model.util;

import java.util.Arrays;
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
import seedu.address.model.location.VisitDate;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {

    @SuppressWarnings("checkstyle:Indentation")
    public static Location[] getSampleLocations() {
        return new Location[]{new Location(
                        new Name("Alex Yeoh"),
                        Optional.of(new Phone("87438807")),
                        Optional.of(new Email("alexyeoh@example.com")),
                        Optional.of(new Address("Blk 30 Geylang Street 29, #06-40")),
                        Optional.of(new PostalCode("408615")),
                        getVisitDateSet("2026-03-12", "2026-04-12"),
                        getTagSet("friends")
                ), new Location(
                        new Name("Bernice Yu"),
                        Optional.of(new Phone("99272758")),
                        Optional.of(new Email("berniceyu@example.com")),
                        Optional.of(new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18")),
                        Optional.of(new PostalCode("554530")),
                        getVisitDateSet("2026-03-18"),
                        getTagSet("colleagues", "friends")
                ), new Location(
                        new Name("Charlotte Oliveiro"),
                        Optional.of(new Phone("93210283")),
                        Optional.of(new Email("charlotte@example.com")),
                        Optional.of(new Address("Blk 11 Ang Mo Kio Street 74, #11-04")),
                        Optional.of(new PostalCode("560011")),
                        getVisitDateSet("2026-03-13"),
                        getTagSet("neighbours")
                ), new Location(
                        new Name("David Li"),
                        Optional.of(new Phone("91031282")),
                        Optional.of(new Email("lidavid@example.com")),
                        Optional.of(new Address("Blk 436 Serangoon Gardens Street 26, #16-43")),
                        Optional.of(new PostalCode("550436")),
                        getVisitDateSet("2026-03-21"),
                        getTagSet("family")
                ), new Location(
                        new Name("Irfan Ibrahim"),
                        Optional.of(new Phone("92492021")),
                        Optional.of(new Email("irfan@example.com")),
                        Optional.of(new Address("Blk 47 Tampines Street 20, #17-35")),
                        Optional.of(new PostalCode("520047")),
                        getVisitDateSet("2026-03-05"),
                        getTagSet("classmates")
                ), new Location(
                        new Name("Roy Balakrishnan"),
                        Optional.of(new Phone("92624417")),
                        Optional.of(new Email("royb@example.com")),
                        Optional.of(new Address("Blk 45 Aljunied Street 85, #11-31")),
                        Optional.of(new PostalCode("380045")),
                        getVisitDateSet("2026-03-01"),
                        getTagSet("colleagues")
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
                .map(VisitDate::new)
                .collect(Collectors.toSet());
    }
}
