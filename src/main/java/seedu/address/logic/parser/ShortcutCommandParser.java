package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.ShortcutCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code ShortcutCommand} object.
 */
public class ShortcutCommandParser implements Parser<ShortcutCommand> {
    private static final Pattern SET_ARGS_PATTERN = Pattern.compile("(?<alias>\\S+)\\s+(?<commandWord>\\S+)");
    private static final Pattern REMOVE_ARGS_PATTERN = Pattern.compile("(?<alias>\\S+)");

    @Override
    public ShortcutCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.equals("list")) {
            return new ShortcutCommand(ShortcutCommand.Action.LIST, null, null);
        }

        if (trimmedArgs.startsWith("set ")) {
            String setArgs = trimmedArgs.substring(4).trim();
            Matcher matcher = SET_ARGS_PATTERN.matcher(setArgs);
            if (!matcher.matches()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        ShortcutCommand.MESSAGE_USAGE));
            }

            return new ShortcutCommand(ShortcutCommand.Action.SET,
                    matcher.group("alias"), matcher.group("commandWord"));
        }

        if (trimmedArgs.startsWith("remove ")) {
            String removeArgs = trimmedArgs.substring(7).trim();
            Matcher matcher = REMOVE_ARGS_PATTERN.matcher(removeArgs);
            if (!matcher.matches()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        ShortcutCommand.MESSAGE_USAGE));
            }

            return new ShortcutCommand(ShortcutCommand.Action.REMOVE, matcher.group("alias"), null);
        }

        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ShortcutCommand.MESSAGE_USAGE));
    }
}
