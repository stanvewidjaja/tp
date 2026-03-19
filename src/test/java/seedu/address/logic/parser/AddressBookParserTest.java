package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_LOCATION;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_LOCATION;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditLocationDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ShortcutCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.location.CombinedLocationPredicate;
import seedu.address.model.location.Location;
import seedu.address.model.location.NameContainsKeywordsPredicate;
import seedu.address.testutil.EditLocationDescriptorBuilder;
import seedu.address.testutil.LocationBuilder;
import seedu.address.testutil.LocationUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Location location = new LocationBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(LocationUtil.getAddCommand(location));
        assertEquals(new AddCommand(location), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_LOCATION.getOneBased());
        assertEquals(new DeleteCommand(List.of(INDEX_FIRST_LOCATION)), command);

        DeleteCommand multipleCommand = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " "
                        + INDEX_FIRST_LOCATION.getOneBased() + " "
                        + INDEX_SECOND_LOCATION.getOneBased());
        assertEquals(new DeleteCommand(List.of(INDEX_FIRST_LOCATION, INDEX_SECOND_LOCATION)), multipleCommand);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Location location = new LocationBuilder().build();
        EditLocationDescriptor descriptor = new EditLocationDescriptorBuilder(location).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_LOCATION.getOneBased() + " " + LocationUtil.getEditLocationDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_LOCATION, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new CombinedLocationPredicate(Collections.singletonList(
                new NameContainsKeywordsPredicate(keywords)))), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_shortcut() throws Exception {
        assertEquals(new ShortcutCommand(ShortcutCommand.Action.LIST, null, null),
                parser.parseCommand(ShortcutCommand.COMMAND_WORD + " list"));
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
