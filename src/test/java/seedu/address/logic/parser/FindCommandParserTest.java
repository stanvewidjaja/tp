package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.location.AddressContainsKeywordsPredicate;
import seedu.address.model.location.CombinedLocationPredicate;
import seedu.address.model.location.NameContainsKeywordsPredicate;
import seedu.address.model.location.TagMatchesKeywordsPredicate;
import seedu.address.model.location.VisitDateMatchesKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
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
        // Address prefix
        FindCommand expectedFindCommand =
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
}
