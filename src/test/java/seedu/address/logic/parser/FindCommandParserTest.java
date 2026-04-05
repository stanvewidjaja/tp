package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.location.predicates.AddressContainsKeywordsPredicate;
import seedu.address.model.location.predicates.CombinedLocationPredicate;
import seedu.address.model.location.predicates.EmailContainsKeywordsPredicate;
import seedu.address.model.location.predicates.NameContainsKeywordsPredicate;
import seedu.address.model.location.predicates.PhoneContainsKeywordsPredicate;
import seedu.address.model.location.predicates.TagMatchesKeywordsPredicate;
import seedu.address.model.location.predicates.VisitDateMatchesKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyPrefixValues_throwsParseException() {
        // empty name prefix
        assertParseFailure(parser, " " + PREFIX_NAME,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));

        // empty phone prefix
        assertParseFailure(parser, " " + PREFIX_PHONE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));

        // empty email prefix
        assertParseFailure(parser, " " + PREFIX_EMAIL,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));

        // empty address prefix
        assertParseFailure(parser, " " + PREFIX_ADDRESS,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));

        // empty tag prefix
        assertParseFailure(parser, " " + PREFIX_TAG,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));

        // empty date prefix
        assertParseFailure(parser, " " + PREFIX_DATE,
                DateParser.MESSAGE_WRONG_DATE_FORMAT + System.lineSeparator() + FindCommand.MESSAGE_USAGE);

        // blank values
        assertParseFailure(parser, " " + PREFIX_NAME + "  ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " " + PREFIX_DATE + "  ",
                DateParser.MESSAGE_WRONG_DATE_FORMAT + System.lineSeparator() + FindCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new CombinedLocationPredicate(Collections.singletonList(
                        new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")))));
        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);
    }

    @Test
    public void parse_prefixArgs_returnsFindCommand() {
        // Name prefix
        FindCommand expectedFindCommand =
                new FindCommand(new CombinedLocationPredicate(Collections.singletonList(
                        new NameContainsKeywordsPredicate(Collections.singletonList("Alice")))));
        assertParseSuccess(parser, " " + PREFIX_NAME + "Alice", expectedFindCommand);

        // Phone prefix
        expectedFindCommand =
                new FindCommand(new CombinedLocationPredicate(Collections.singletonList(
                        new PhoneContainsKeywordsPredicate("9123"))));
        assertParseSuccess(parser, " " + PREFIX_PHONE + "9123", expectedFindCommand);

        // Email prefix
        expectedFindCommand =
                new FindCommand(new CombinedLocationPredicate(Collections.singletonList(
                        new EmailContainsKeywordsPredicate("alice@example.com"))));
        assertParseSuccess(parser, " " + PREFIX_EMAIL + "alice@example.com", expectedFindCommand);

        // Address prefix
        expectedFindCommand =
                new FindCommand(new CombinedLocationPredicate(Collections.singletonList(
                        new AddressContainsKeywordsPredicate("Clementi"))));
        assertParseSuccess(parser, " " + PREFIX_ADDRESS + "Clementi", expectedFindCommand);

        // Tag prefix
        expectedFindCommand =
                new FindCommand(new CombinedLocationPredicate(Collections.singletonList(
                        new TagMatchesKeywordsPredicate("important"))));
        assertParseSuccess(parser, " " + PREFIX_TAG + "important", expectedFindCommand);

        // Date prefix
        expectedFindCommand =
                new FindCommand(new CombinedLocationPredicate(Collections.singletonList(
                        new VisitDateMatchesKeywordsPredicate(LocalDate.parse("2024-01-15")))));
        assertParseSuccess(parser, " " + PREFIX_DATE + "2024-01-15", expectedFindCommand);

        // Different date format (e.g., dd/MM/yyyy)
        assertParseSuccess(parser, " " + PREFIX_DATE + "15/01/2024", expectedFindCommand);

        // Combined prefixes (AND logic)
        expectedFindCommand =
                new FindCommand(new CombinedLocationPredicate(Arrays.asList(
                        new NameContainsKeywordsPredicate(Collections.singletonList("Alice")),
                        new TagMatchesKeywordsPredicate("important")
                )));
        assertParseSuccess(parser, " Alice " + PREFIX_TAG + "important", expectedFindCommand);

        // Multiple tags (AND logic)
        expectedFindCommand =
                new FindCommand(new CombinedLocationPredicate(Arrays.asList(
                        new TagMatchesKeywordsPredicate("tag1"),
                        new TagMatchesKeywordsPredicate("tag2")
                )));
        assertParseSuccess(parser, " " + PREFIX_TAG + "tag1 " + PREFIX_TAG + "tag2", expectedFindCommand);
    }

    @Test
    public void parse_substringArgs_returnsFindCommand() {
        // Substring keyword (prefix)
        FindCommand expectedFindCommand =
                new FindCommand(new CombinedLocationPredicate(Collections.singletonList(
                        new NameContainsKeywordsPredicate(Arrays.asList("Ali")))));
        assertParseSuccess(parser, "Ali", expectedFindCommand);

        // Substring keyword (middle)
        expectedFindCommand = new FindCommand(new CombinedLocationPredicate(Collections.singletonList(
                new NameContainsKeywordsPredicate(Arrays.asList("lic")))));
        assertParseSuccess(parser, "lic", expectedFindCommand);

        // Multiple substring keywords
        expectedFindCommand = new FindCommand(new CombinedLocationPredicate(Collections.singletonList(
                new NameContainsKeywordsPredicate(Arrays.asList("Jo", "Mey")))));
        assertParseSuccess(parser, "Jo Mey", expectedFindCommand);

        // Mixed case substring
        expectedFindCommand = new FindCommand(new CombinedLocationPredicate(Collections.singletonList(
                new NameContainsKeywordsPredicate(Arrays.asList("aLi")))));
        assertParseSuccess(parser, "aLi", expectedFindCommand);
    }

    @Test
    void parse_unsupportedPrefix_throwsParseException() {
        assertParseFailure(parser, " o/123",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    void parse_supportedAndUnsupportedPrefix_throwsParseException() {
        assertParseFailure(parser, " n/John o/123",
                           String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }
}
