package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ShortcutCommand;

public class ShortcutCommandParserTest {
    private final ShortcutCommandParser parser = new ShortcutCommandParser();

    @Test
    public void parse_set_success() {
        assertParseSuccess(parser, "set a add",
                new ShortcutCommand(ShortcutCommand.Action.SET, "a", "add"));
    }

    @Test
    public void parse_remove_success() {
        assertParseSuccess(parser, "remove a",
                new ShortcutCommand(ShortcutCommand.Action.REMOVE, "a", null));
    }

    @Test
    public void parse_list_success() {
        assertParseSuccess(parser, "list",
                new ShortcutCommand(ShortcutCommand.Action.LIST, null, null));
    }

    @Test
    public void parse_invalidFormat_failure() {
        assertParseFailure(parser, "set a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShortcutCommand.MESSAGE_USAGE));
    }
}
