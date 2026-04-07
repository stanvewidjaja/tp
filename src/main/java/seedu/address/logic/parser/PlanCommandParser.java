package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_DATE_COMMAND_FORMAT;

import java.time.LocalDate;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.PlanCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code PlanCommand} object
 */
public class PlanCommandParser implements Parser<PlanCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code PlanCommand}
     * and returns a {@code PlanCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected date format
     */
    public PlanCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            return new PlanCommand(null);
        }

        try {
            LocalDate date = DateParser.parse(trimmedArgs);
            return new PlanCommand(date);
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_DATE_COMMAND_FORMAT, PlanCommand.MESSAGE_USAGE), ive);
        }
    }
}
