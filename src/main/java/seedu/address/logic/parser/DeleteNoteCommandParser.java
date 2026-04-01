package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE_REMOVE;

import seedu.address.logic.commands.DeleteNoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.location.dates.VisitDate;

/**
 * Parses input arguments and creates a new DeleteNoteCommand object.
 */
public class DeleteNoteCommandParser implements Parser<DeleteNoteCommand> {

    @Override
    public DeleteNoteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_DATE_REMOVE);

        if (!arePrefixesPresent(argMultimap, PREFIX_DATE_REMOVE) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteNoteCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_DATE_REMOVE);

        VisitDate date = ParserUtil.parseVisitDate(argMultimap.getValue(PREFIX_DATE_REMOVE).get());
        return new DeleteNoteCommand(date);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return java.util.stream.Stream.of(prefixes)
                .allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
