package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.DeleteNoteCommand;
import seedu.address.model.location.dates.VisitDate;

public class DeleteNoteCommandParserTest {

    private final DeleteNoteCommandParser parser = new DeleteNoteCommandParser();

    @Test
    public void parse_validArgs_success() throws Exception {
        DeleteNoteCommand expected = new DeleteNoteCommand(VisitDate.of("2026-03-24"));

        assertParseSuccess(parser, " d-/2026-03-24", expected);
    }

    @Test
    public void parse_missingDate_throwsParseException() {
        assertParseFailure(parser, " d-/",
                VisitDate.MESSAGE_CONSTRAINTS_EMPTY);
    }

    @Test
    public void parse_invalidPreamble_throwsParseException() {
        assertParseFailure(parser, "abc d-/2026-03-24", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteNoteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicateDatePrefix_throwsParseException() {
        assertParseFailure(parser, " d-/2026-03-24 d-/2026-03-25",
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_DATE_REMOVE));
    }
}
