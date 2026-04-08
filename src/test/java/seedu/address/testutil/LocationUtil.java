package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSTAL_CODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.EditCommand.EditLocationDescriptor;
import seedu.address.model.location.Location;
import seedu.address.model.location.dates.VisitDate;
import seedu.address.model.tag.Tag;

/**
 * A utility class for Location.
 */
public class LocationUtil {

    /**
     * Returns an add command string for adding the {@code location}.
     */
    public static String getAddCommand(Location location) {
        return AddCommand.COMMAND_WORD + " " + getLocationDetails(location);
    }

    /**
     * Returns the part of command string for the given {@code location}'s details.
     */
    public static String getLocationDetails(Location location) {
        StringBuilder sb = new StringBuilder();

        sb.append(PREFIX_NAME).append(location.getName().fullName).append(" ");

        location.getPhone().ifPresent(phone ->
                sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        location.getEmail().ifPresent(email ->
                sb.append(PREFIX_EMAIL).append(email.value).append(" "));
        location.getAddress().ifPresent(address ->
                sb.append(PREFIX_ADDRESS).append(address.value).append(" "));
        location.getPostalCode().ifPresent(postalCode ->
                sb.append(PREFIX_POSTAL_CODE).append(postalCode.value).append(" "));

        // multi-date support (IMPORTANT)
        location.getVisitDates().forEach(
                visitDate -> sb.append(PREFIX_DATE).append(visitDate.toDataString()).append(" ")
        );

        location.getTags().forEach(tag ->
                sb.append(PREFIX_TAG).append(tag.tagName).append(" "));

        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditLocationDescriptor}'s details.
     */
    public static String getEditLocationDescriptorDetails(EditLocationDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();

        descriptor.getName().ifPresent(name ->
                sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone ->
                sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        descriptor.getEmail().ifPresent(email ->
                sb.append(PREFIX_EMAIL).append(email.value).append(" "));
        descriptor.getAddress().ifPresent(address ->
                sb.append(PREFIX_ADDRESS).append(address.value).append(" "));
        descriptor.getPostalCode().ifPresent(postalCode ->
                sb.append(PREFIX_POSTAL_CODE).append(postalCode.value).append(" "));

        if (descriptor.getVisitDates().isPresent()) {
            Set<VisitDate> visitDates = descriptor.getVisitDates().get();
            if (visitDates.isEmpty()) {
                sb.append(PREFIX_DATE).append(" ");
            } else {
                visitDates.forEach(d ->
                        sb.append(PREFIX_DATE).append(d.toDataString()).append(" "));
            }
        }

        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG).append(" ");
            } else {
                tags.forEach(s ->
                        sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }

        return sb.toString();
    }
}
