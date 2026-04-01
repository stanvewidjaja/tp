package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.NoteCommand;
import seedu.address.model.location.NoteContent;
import seedu.address.model.location.VisitDate;

public class NoteCommandParserTest {

    private final NoteCommandParser parser = new NoteCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        NoteCommand expected = new NoteCommand(new NoteContent("Great place"), new VisitDate("2026-03-24"));

        assertParseSuccess(parser, " n/Great place d/2026-03-24", expected);
    }

    //    @Test
    //    public void parseOptionalDatePresentSuccess() throws Exception {
    //        NoteCommand expected = new NoteCommand(new Name("Great place"), Optional.empty());
    //
    //        assertParseSuccess(parser, " n/Great place", expected);
    //    }

    @Test
    public void parse_missingNote_throwsParseException() {
        assertParseFailure(parser, "d/2026-03-24", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                NoteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidPreamble_throwsParseException() {
        assertParseFailure(parser, "abc n/Great place", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                NoteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicateNamePrefix_throwsParseException() {
        assertParseFailure(parser, " n/Great place n/Other d/2026-03-24",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));
    }

    @Test
    public void parse_duplicateDatePrefix_throwsParseException() {
        assertParseFailure(parser, " n/Great place d/2026-03-24 d/2026-04-01",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATE));
    }

    @Test
    public void parse_invalidDateValue_throwsParseException() {
        assertParseFailure(parser, " n/Great place d/2026-02-99",
                VisitDate.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidNoteValue_throwsParseException() {
        assertParseFailure(parser, " n/  d/2026-03-24",
                NoteContent.MESSAGE_CONSTRAINTS);
    }
}
