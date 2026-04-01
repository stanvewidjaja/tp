package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.location.Address;
import seedu.address.model.location.Email;
import seedu.address.model.location.Location;
import seedu.address.model.location.Name;
import seedu.address.model.location.Phone;
import seedu.address.model.location.PostalCode;
import seedu.address.model.location.dates.VisitDate;
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
    private final String postalCode;
    private final List<String> visitDates = new ArrayList<>();
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedLocation} with the given location details.
     */
    @JsonCreator
    public JsonAdaptedLocation(@JsonProperty("name") String name,
                               @JsonProperty("phone") String phone,
                               @JsonProperty("email") String email,
                               @JsonProperty("address") String address,
                               @JsonProperty("postalCode") String postalCode,
                               @JsonProperty("visitDates") List<String> visitDates,
                               @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.postalCode = postalCode;

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
        phone = source.getPhone().map(p -> p.value).orElse(null);
        email = source.getEmail().map(e -> e.value).orElse(null);
        address = source.getAddress().map(a -> a.value).orElse(null);
        postalCode = source.getPostalCode().map(p -> p.value).orElse(null);

        visitDates.addAll(source.getVisitDates().stream()
                .map(VisitDate::toDataString)
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

        Optional<Phone> modelPhone = Optional.empty();
        if (phone != null) {
            if (!Phone.isValidPhone(phone)) {
                throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
            }
            modelPhone = Optional.of(new Phone(phone));
        }

        Optional<Email> modelEmail = Optional.empty();
        if (email != null) {
            if (!Email.isValidEmail(email)) {
                throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
            }
            modelEmail = Optional.of(new Email(email));
        }

        Optional<Address> modelAddress = Optional.empty();
        if (address != null) {
            if (!Address.isValidAddress(address)) {
                throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
            }
            modelAddress = Optional.of(new Address(address));
        }

        Optional<PostalCode> modelPostalCode = Optional.empty();
        if (postalCode != null) {
            if (!PostalCode.isValidPostalCode(postalCode)) {
                throw new IllegalValueException(PostalCode.MESSAGE_CONSTRAINTS);
            }
            modelPostalCode = Optional.of(new PostalCode(postalCode));
        }

        final Set<VisitDate> modelVisitDates = new HashSet<>();
        for (String visitDate : visitDates) {
            modelVisitDates.add(VisitDate.of(visitDate));
        }

        final Set<Tag> modelTags = new HashSet<>(locationTags);

        return new Location(modelName, modelPhone, modelEmail,
                modelAddress, modelPostalCode, modelVisitDates, modelTags);
    }
}
