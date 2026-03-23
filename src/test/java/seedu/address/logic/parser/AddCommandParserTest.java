package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.POSTAL_CODE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.POSTAL_CODE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSTAL_CODE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalLocations.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.model.location.Address;
import seedu.address.model.location.Email;
import seedu.address.model.location.Location;
import seedu.address.model.location.Name;
import seedu.address.model.location.Phone;
import seedu.address.model.location.VisitDate;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.LocationBuilder;

/**
 * Tests for {@code AddCommandParser}.
 */
public class AddCommandParserTest {

    private final AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Location expectedLocation = new LocationBuilder(BOB).build();

        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + ADDRESS_DESC_BOB + POSTAL_CODE_DESC_BOB + DATE_DESC_BOB
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                new AddCommand(expectedLocation));

        Location expectedLocationMultipleDates = new LocationBuilder(BOB)
                .withVisitDates(VALID_DATE_BOB, VALID_DATE_AMY)
                .build();

        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + POSTAL_CODE_DESC_BOB + DATE_DESC_BOB + DATE_DESC_AMY
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                new AddCommand(expectedLocationMultipleDates));
    }

    @Test
    public void parse_repeatedNonRepeatableValue_failure() {
        String validExpectedLocationString = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + POSTAL_CODE_DESC_BOB + DATE_DESC_BOB + TAG_DESC_FRIEND;

        assertParseFailure(parser, NAME_DESC_AMY + validExpectedLocationString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedLocationString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        assertParseFailure(parser, EMAIL_DESC_AMY + validExpectedLocationString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        assertParseFailure(parser, ADDRESS_DESC_AMY + validExpectedLocationString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        assertParseFailure(parser, POSTAL_CODE_DESC_AMY + validExpectedLocationString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_POSTAL_CODE));

        assertParseFailure(parser,
                validExpectedLocationString + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY
                        + ADDRESS_DESC_AMY + POSTAL_CODE_DESC_AMY,
                Messages.getErrorMessageForDuplicatePrefixes(
                        PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_POSTAL_CODE));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        Location expectedLocation = new LocationBuilder()
                .withName("Amy Bee")
                .withoutPhone()
                .withoutEmail()
                .withoutAddress()
                .withoutPostalCode()
                .withoutVisitDates()
                .withTags()
                .build();

        assertParseSuccess(parser, NAME_DESC_AMY, new AddCommand(expectedLocation));

        Location expectedLocationWithTagsOnly = new LocationBuilder()
                .withName("Amy Bee")
                .withoutPhone()
                .withoutEmail()
                .withoutAddress()
                .withoutPostalCode()
                .withoutVisitDates()
                .withTags(VALID_TAG_FRIEND)
                .build();

        assertParseSuccess(parser,
                NAME_DESC_AMY + TAG_DESC_FRIEND,
                new AddCommand(expectedLocationWithTagsOnly));

        Location expectedLocationWithDateOnly = new LocationBuilder()
                .withName("Amy Bee")
                .withoutPhone()
                .withoutEmail()
                .withoutAddress()
                .withoutPostalCode()
                .withVisitDate(VALID_DATE_AMY)
                .withTags()
                .build();

        assertParseSuccess(parser,
                NAME_DESC_AMY + DATE_DESC_AMY,
                new AddCommand(expectedLocationWithDateOnly));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        assertParseFailure(parser,
                PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + POSTAL_CODE_DESC_BOB,
                expectedMessage);

        assertParseFailure(parser,
                ADDRESS_DESC_BOB + POSTAL_CODE_DESC_BOB + DATE_DESC_BOB + TAG_DESC_FRIEND,
                expectedMessage);

        assertParseFailure(parser,
                PREAMBLE_NON_EMPTY + NAME_DESC_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser,
                INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + POSTAL_CODE_DESC_BOB,
                Name.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser,
                NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + POSTAL_CODE_DESC_BOB,
                Phone.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB + POSTAL_CODE_DESC_BOB,
                Email.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC + POSTAL_CODE_DESC_BOB,
                Address.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser,
                NAME_DESC_BOB + INVALID_DATE_DESC,
                VisitDate.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + INVALID_TAG_DESC,
                Tag.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser,
                PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
