package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.EditCommand.EditLocationDescriptor;
import seedu.address.model.location.Location;
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
        sb.append(PREFIX_NAME + location.getName().fullName + " ");
        sb.append(PREFIX_PHONE + location.getPhone().value + " ");
        sb.append(PREFIX_EMAIL + location.getEmail().value + " ");
        sb.append(PREFIX_ADDRESS + location.getAddress().value + " ");
        sb.append(PREFIX_DATE + location.getVisitDate().toString() + " ");
        location.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditLocationDescriptor}'s details.
     */
    public static String getEditLocationDescriptorDetails(EditLocationDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        descriptor.getEmail().ifPresent(email -> sb.append(PREFIX_EMAIL).append(email.value).append(" "));
        descriptor.getAddress().ifPresent(address -> sb.append(PREFIX_ADDRESS).append(address.value).append(" "));
        descriptor.getVisitDate()
                .ifPresent(date -> sb.append(PREFIX_DATE).append(date.toString()).append(" "));
        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG);
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}
