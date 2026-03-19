package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.DateParser;
import seedu.address.model.location.Address;
import seedu.address.model.location.Email;
import seedu.address.model.location.Location;
import seedu.address.model.location.Name;
import seedu.address.model.location.Phone;
import seedu.address.model.location.VisitDate;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Location}.
 */
class JsonAdaptedLocation {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Location's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final List<String> visitDates = new ArrayList<>();
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedLocation} with the given location details.
     */
    @JsonCreator
    public JsonAdaptedLocation(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("address") String address,
            @JsonProperty("visitDate") String visitDate,
            @JsonProperty("visitDates") List<String> visitDates,
            @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        if (visitDate != null) {
            this.visitDates.add(visitDate);
        }
        if (visitDates != null) {
            this.visitDates.addAll(visitDates);
        }
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Constructs a {@code JsonAdaptedLocation} with the given location details.
     */
    public JsonAdaptedLocation(String name, String phone, String email, String address,
                               List<String> visitDates, List<JsonAdaptedTag> tags) {
        this(name, phone, email, address, null, visitDates, tags);
    }

    /**
     * Converts a given {@code Location} into this class for Jackson use.
     */
    public JsonAdaptedLocation(Location source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        visitDates.addAll(source.getVisitDates().stream()
                .map(VisitDate::toString)
                .collect(Collectors.toList()));
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted location object into the model's {@code Location} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted location.
     */
    public Location toModelType() throws IllegalValueException {
        final List<Tag> locationTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            locationTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final Set<VisitDate> modelVisitDates = new HashSet<>();
        for (String visitDate : visitDates) {
            if (!VisitDate.isValidVisitDate(visitDate)) {
                throw new IllegalValueException(DateParser.MESSAGE_WRONG_DATE_FORMAT);
            }
            modelVisitDates.add(new VisitDate(visitDate));
        }

        final Set<Tag> modelTags = new HashSet<>(locationTags);
        return new Location(modelName, modelPhone, modelEmail, modelAddress, modelVisitDates, modelTags);
    }

}
