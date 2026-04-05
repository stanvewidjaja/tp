package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Formats help instructions for local display and optionally opens the help window.
 */
public class HelpCommand extends Command {
    public static final String LINK_FLAG = "-ug";
    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows a summary of all commands, help for a "
            + "specific command, or opens the online user guide.\n"
            + "Parameters: [COMMAND_WORD] | [" + LINK_FLAG + "]\n"
            + "\nExamples:\n"
            + COMMAND_WORD + "\n"
            + COMMAND_WORD + " add\n"
            + COMMAND_WORD + " " + LINK_FLAG;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";
    public static final String MESSAGE_UNKNOWN_HELP_TOPIC = "Unknown command for help: %1$s";

    private static final CommandDatabase COMMAND_DATABASE = new CommandDatabase();

    private final String targetCommand;
    private final boolean shouldShowLink;

    /**
     * Creates a help command for the overview.
     */
    public HelpCommand() {
        this(null, false);
    }

    /**
     * Creates a help command that opens the online help window.
     */
    public HelpCommand(boolean shouldShowLink) {
        this(null, shouldShowLink);
    }

    /**
     * Creates a help command for the specified command word.
     */
    public HelpCommand(String targetCommand) {
        this(targetCommand, false);
    }

    private HelpCommand(String targetCommand, boolean shouldShowLink) {
        this.targetCommand = targetCommand;
        this.shouldShowLink = shouldShowLink;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (shouldShowLink) {
            return new CommandResult(SHOWING_HELP_MESSAGE, null, true, false);
        }

        if (targetCommand == null) {
            return new CommandResult(COMMAND_DATABASE.getHelpOverview());
        }

        return COMMAND_DATABASE.getDetailedHelp(targetCommand)
                .map(CommandResult::new)
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_UNKNOWN_HELP_TOPIC, targetCommand)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof HelpCommand)) {
            return false;
        }

        HelpCommand otherHelpCommand = (HelpCommand) other;
        return Objects.equals(targetCommand, otherHelpCommand.targetCommand)
                && shouldShowLink == otherHelpCommand.shouldShowLink;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetCommand", targetCommand)
                .add("shouldShowLink", shouldShowLink)
                .toString();
    }
}
