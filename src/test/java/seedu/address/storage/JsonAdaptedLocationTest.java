package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedLocation.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalLocations.BOB;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.location.Address;
import seedu.address.model.location.Email;
import seedu.address.model.location.Location;
import seedu.address.model.location.Name;
import seedu.address.model.location.Phone;
import seedu.address.model.location.PostalCode;
import seedu.address.model.location.dates.VisitDate;
import seedu.address.testutil.LocationBuilder;

public class JsonAdaptedLocationTest {

    private static final String INVALID_NAME = "R^chel";
    private static final String INVALID_PHONE = "-651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_POSTAL_CODE = "12 34";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_VISIT_DATE = "invalid-date";

    private static final String VALID_NAME = BOB.getName().toString();
    private static final String VALID_PHONE = BOB.getPhone().get().toString();
    private static final String VALID_EMAIL = BOB.getEmail().get().toString();
    private static final String VALID_ADDRESS = BOB.getAddress().get().toString();
    private static final String VALID_POSTAL_CODE = BOB.getPostalCode().get().toString();

    private static final List<JsonAdaptedTag> VALID_TAGS = BOB.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    private static final List<String> VALID_VISIT_DATES = BOB.getVisitDates().stream()
            .map(VisitDate::toDataString)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validLocationDetails_returnsLocation() throws Exception {
        JsonAdaptedLocation location = new JsonAdaptedLocation(BOB);
        assertEquals(BOB, location.toModelType());
    }

    @Test
    public void toModelType_validLocationWithMissingOptionalFields_returnsLocation() throws Exception {
        Location locationWithoutOptionalFields = new LocationBuilder()
                .withName("Amy Bee")
                .withoutPhone()
                .withoutEmail()
                .withoutAddress()
                .withoutPostalCode()
                .withoutVisitDates()
                .withTags()
                .build();

        JsonAdaptedLocation jsonAdaptedLocation = new JsonAdaptedLocation(locationWithoutOptionalFields);
        assertEquals(locationWithoutOptionalFields, jsonAdaptedLocation.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedLocation location =
                new JsonAdaptedLocation(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_POSTAL_CODE, VALID_VISIT_DATES, VALID_TAGS);

        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, location::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedLocation location =
                new JsonAdaptedLocation(null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_POSTAL_CODE, VALID_VISIT_DATES, VALID_TAGS);

        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, location::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedLocation location =
                new JsonAdaptedLocation(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_POSTAL_CODE, VALID_VISIT_DATES, VALID_TAGS);

        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, location::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedLocation location =
                new JsonAdaptedLocation(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS,
                        VALID_POSTAL_CODE, VALID_VISIT_DATES, VALID_TAGS);

        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, location::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedLocation location =
                new JsonAdaptedLocation(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS,
                        VALID_POSTAL_CODE, VALID_VISIT_DATES, VALID_TAGS);

        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, location::toModelType);
    }

    @Test
    public void toModelType_invalidPostalCode_throwsIllegalValueException() {
        JsonAdaptedLocation location =
                new JsonAdaptedLocation(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        INVALID_POSTAL_CODE, VALID_VISIT_DATES, VALID_TAGS);

        String expectedMessage = PostalCode.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, location::toModelType);
    }

    @Test
    public void toModelType_invalidVisitDate_throwsIllegalValueException() {
        List<String> invalidVisitDates = new ArrayList<>(VALID_VISIT_DATES);
        invalidVisitDates.add(INVALID_VISIT_DATE);

        JsonAdaptedLocation location =
                new JsonAdaptedLocation(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_POSTAL_CODE, invalidVisitDates, VALID_TAGS);

        String expectedMessage = VisitDate.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, location::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));

        JsonAdaptedLocation location =
                new JsonAdaptedLocation(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_POSTAL_CODE, VALID_VISIT_DATES, invalidTags);

        assertThrows(IllegalValueException.class, location::toModelType);
    }

    @Test
    public void toModelType_nullOptionalFields_returnsLocation() throws Exception {
        Location expectedLocation = new LocationBuilder()
                .withName(VALID_NAME)
                .withoutPhone()
                .withoutEmail()
                .withoutAddress()
                .withoutPostalCode()
                .withVisitDates(VALID_VISIT_DATES.toArray(new String[0]))
                .withTags(BOB.getTags().stream().map(tag -> tag.tagName).toArray(String[]::new))
                .build();

        JsonAdaptedLocation location =
                new JsonAdaptedLocation(VALID_NAME, null, null, null,
                        null, VALID_VISIT_DATES, VALID_TAGS);

        assertEquals(expectedLocation, location.toModelType());
    }
}
