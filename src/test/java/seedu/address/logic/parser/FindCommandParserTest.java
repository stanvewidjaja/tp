package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.location.NameContainsKeywordsPredicate;

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
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);
    }

    @Test
    public void parse_substringArgs_returnsFindCommand() {
        // Substring keyword (prefix)
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Ali")));
        assertParseSuccess(parser, "Ali", expectedFindCommand);

        // Substring keyword (middle)
        expectedFindCommand = new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("lic")));
        assertParseSuccess(parser, "lic", expectedFindCommand);

        // Multiple substring keywords
        expectedFindCommand = new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Jo", "Mey")));
        assertParseSuccess(parser, "Jo Mey", expectedFindCommand);

        // Mixed case substring
        expectedFindCommand = new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("aLi")));
        assertParseSuccess(parser, "aLi", expectedFindCommand);
    }
}
