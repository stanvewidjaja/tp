package seedu.address.logic;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.location.Location;
import seedu.address.model.location.dates.VisitDate;
import seedu.address.model.tag.Tag;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_DATE_COMMAND_FORMAT = "%1$s is not a valid date! \n%2$s";
    public static final String MESSAGE_INVALID_LOCATION_DISPLAYED_INDEX = "The location index provided is invalid";
    public static final String MESSAGE_LOCATIONS_LISTED_OVERVIEW = "%1$d matching locations found!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Duplicate prefixes for the following field(s) are not allowed: ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Returns an error message indicating the valid displayed index range.
     */
    public static String getInvalidLocationDisplayedIndexMessage(int locationCount) {
        if (locationCount <= 0) {
            return "Invalid index. There are no entries in the current list.";
        }
        return String.format("Invalid index. Valid index range is 1 to %d.", locationCount);
    }

    /**
     * Returns an error message indicating the valid displayed index range for multiple indices.
     */
    public static String getInvalidLocationDisplayedIndexesMessage(int locationCount) {
        if (locationCount <= 0) {
            return "Invalid indices. There are no entries in the current list.";
        }
        return String.format("At least one of the indices is invalid. Valid index range is 1 to %d.", locationCount);
    }

    /**
     * Formats the {@code location} for display to the user.
     */
    public static String format(Location location) {
        final StringBuilder builder = new StringBuilder();
        builder.append(location.getName())
                .append("; Phone: ")
                .append(location.getPhone().map(phone -> phone.value).orElse("-"))
                .append("; Email: ")
                .append(location.getEmail().map(email -> email.value).orElse("-"))
                .append("; Address: ")
                .append(location.getAddress().map(address -> address.value).orElse("-"))
                .append("; Postal Code: ")
                .append(location.getPostalCode().map(postalCode -> postalCode.value).orElse("-"))
                .append("; Visit Dates: ");

        if (location.getVisitDates().isEmpty()) {
            builder.append("-");
        } else {
            builder.append(location.getVisitDates().stream()
                    .sorted(Comparator.comparing(VisitDate::toSortString))
                    .map(VisitDate::toString)
                    .collect(Collectors.joining(", ")));
        }

        builder.append("; Tags: ");
        if (location.getTags().isEmpty()) {
            builder.append("-");
        } else {
            builder.append(location.getTags().stream()
                    .map(Tag::toString)
                    .collect(Collectors.joining(", ")));
        }

        return builder.toString();
    }
}
