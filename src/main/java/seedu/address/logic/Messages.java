package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.location.Location;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_LOCATION_DISPLAYED_INDEX = "The location index provided is invalid";
    public static final String MESSAGE_LOCATIONS_LISTED_OVERVIEW = "%1$d locations listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";

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
     * Formats the {@code location} for display to the user.
     */
    public static String format(Location location) {
        final StringBuilder builder = new StringBuilder();
        builder.append(location.getName())
                .append("; Phone: ")
                .append(location.getPhone())
                .append("; Email: ")
                .append(location.getEmail())
                .append("; Address: ")
                .append(location.getAddress())
                .append("; Visit Dates: ");
        location.getVisitDates().forEach(vd -> builder.append("[" + vd + "]"));
        builder.append("; Tags: ");
        location.getTags().forEach(builder::append);
        return builder.toString();
    }

}
