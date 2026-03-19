package seedu.address.storage;

//import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.storage.JsonAdaptedLocation.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;

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
import seedu.address.model.location.VisitDate;
import seedu.address.testutil.TypicalLocations;

public class JsonAdaptedLocationTest {

    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "abc";
    private static final String INVALID_ADDRESS = "";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_POSTAL_CODE = "12 34!";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_VISIT_DATE = "invalid-date";

    private static final Location BENSON = TypicalLocations.BENSON;

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().map(Object::toString).orElse("91234567");
    private static final String VALID_EMAIL = BENSON.getEmail().map(Object::toString).orElse("benson@example.com");
    private static final String VALID_ADDRESS = BENSON.getAddress().map(Object::toString).orElse("311, Clementi Ave 2");
    private static final String VALID_POSTAL_CODE = BENSON.getPostalCode().map(Object::toString).orElse("123456");

    private static final List<String> VALID_VISIT_DATES = BENSON.getVisitDates().stream()
            .map(VisitDate::toString)
            .collect(Collectors.toList());

    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());
    /*
    @Test
    public void toModelType_validLocationDetails_returnsLocation() throws Exception {
        JsonAdaptedLocation location = new JsonAdaptedLocation(BENSON);
        assertEquals(BENSON, location.toModelType());
    }
    */

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedLocation location =
                new JsonAdaptedLocation(INVALID_NAME, VALID_PHONE, VALID_EMAIL,
                        VALID_ADDRESS, VALID_POSTAL_CODE, VALID_VISIT_DATES, VALID_TAGS);

        assertThrows(IllegalValueException.class, Name.MESSAGE_CONSTRAINTS, location::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedLocation location =
                new JsonAdaptedLocation(null, VALID_PHONE, VALID_EMAIL,
                        VALID_ADDRESS, VALID_POSTAL_CODE, VALID_VISIT_DATES, VALID_TAGS);

        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, location::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedLocation location =
                new JsonAdaptedLocation(VALID_NAME, INVALID_PHONE, VALID_EMAIL,
                        VALID_ADDRESS, VALID_POSTAL_CODE, VALID_VISIT_DATES, VALID_TAGS);

        assertThrows(IllegalValueException.class, Phone.MESSAGE_CONSTRAINTS, location::toModelType);
    }

    @Test
    public void toModelType_nullPhone_returnsEmptyOptional() throws Exception {
        JsonAdaptedLocation location =
                new JsonAdaptedLocation(VALID_NAME, null, VALID_EMAIL,
                        VALID_ADDRESS, VALID_POSTAL_CODE, VALID_VISIT_DATES, VALID_TAGS);

        assertTrue(location.toModelType().getPhone().isEmpty());
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedLocation location =
                new JsonAdaptedLocation(VALID_NAME, VALID_PHONE, INVALID_EMAIL,
                        VALID_ADDRESS, VALID_POSTAL_CODE, VALID_VISIT_DATES, VALID_TAGS);

        assertThrows(IllegalValueException.class, Email.MESSAGE_CONSTRAINTS, location::toModelType);
    }

    @Test
    public void toModelType_nullEmail_returnsEmptyOptional() throws Exception {
        JsonAdaptedLocation location =
                new JsonAdaptedLocation(VALID_NAME, VALID_PHONE, null,
                        VALID_ADDRESS, VALID_POSTAL_CODE, VALID_VISIT_DATES, VALID_TAGS);

        assertTrue(location.toModelType().getEmail().isEmpty());
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedLocation location =
                new JsonAdaptedLocation(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                        INVALID_ADDRESS, VALID_POSTAL_CODE, VALID_VISIT_DATES, VALID_TAGS);

        assertThrows(IllegalValueException.class, Address.MESSAGE_CONSTRAINTS, location::toModelType);
    }

    @Test
    public void toModelType_nullAddress_returnsEmptyOptional() throws Exception {
        JsonAdaptedLocation location =
                new JsonAdaptedLocation(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                        null, VALID_POSTAL_CODE, VALID_VISIT_DATES, VALID_TAGS);

        assertTrue(location.toModelType().getAddress().isEmpty());
    }

    @Test
    public void toModelType_invalidPostalCode_throwsIllegalValueException() {
        JsonAdaptedLocation location =
                new JsonAdaptedLocation(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                        VALID_ADDRESS, INVALID_POSTAL_CODE, VALID_VISIT_DATES, VALID_TAGS);

        assertThrows(IllegalValueException.class, PostalCode.MESSAGE_CONSTRAINTS, location::toModelType);
    }

    @Test
    public void toModelType_nullPostalCode_returnsEmptyOptional() throws Exception {
        JsonAdaptedLocation location =
                new JsonAdaptedLocation(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                        VALID_ADDRESS, null, VALID_VISIT_DATES, VALID_TAGS);

        assertTrue(location.toModelType().getPostalCode().isEmpty());
    }

    @Test
    public void toModelType_invalidVisitDate_throwsIllegalValueException() {
        List<String> invalidVisitDates = new ArrayList<>(VALID_VISIT_DATES);
        invalidVisitDates.add(INVALID_VISIT_DATE);

        JsonAdaptedLocation location =
                new JsonAdaptedLocation(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                        VALID_ADDRESS, VALID_POSTAL_CODE, invalidVisitDates, VALID_TAGS);

        assertThrows(IllegalValueException.class, VisitDate.MESSAGE_CONSTRAINTS, location::toModelType);
    }

    @Test
    public void toModelType_nullVisitDates_returnsEmptySet() throws Exception {
        JsonAdaptedLocation location =
                new JsonAdaptedLocation(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                        VALID_ADDRESS, VALID_POSTAL_CODE, null, VALID_TAGS);

        assertTrue(location.toModelType().getVisitDates().isEmpty());
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));

        JsonAdaptedLocation location =
                new JsonAdaptedLocation(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                        VALID_ADDRESS, VALID_POSTAL_CODE, VALID_VISIT_DATES, invalidTags);

        assertThrows(IllegalValueException.class, location::toModelType);
    }
}
