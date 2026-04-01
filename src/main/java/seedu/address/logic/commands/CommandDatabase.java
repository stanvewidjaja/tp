package seedu.address.logic.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Represents the collection of command keywords available to the user
 * and stores help metadata for built-in commands.
 */
public class CommandDatabase {

    private static final String OVERVIEW_INTRO = "Available commands:";
    private static final String OVERVIEW_FOOTER = "Use `help COMMAND_WORD` for detailed guidance.";

    private final Map<String, CommandInfo> commandRegistry;

    /**
     * Initializes the database with the command keywords.
     */
    public CommandDatabase() {
        commandRegistry = new LinkedHashMap<>();
        register(AddCommand.COMMAND_WORD, "Add a new location to AddressMe.", AddCommand.MESSAGE_USAGE);
        register(ClearCommand.COMMAND_WORD, "Remove all saved locations.", ClearCommand.MESSAGE_USAGE);
        register(DeleteCommand.COMMAND_WORD, "Delete one or more locations by index.", DeleteCommand.MESSAGE_USAGE);
        register(EditCommand.COMMAND_WORD, "Edit an existing location.", EditCommand.MESSAGE_USAGE);
        register(ExitCommand.COMMAND_WORD, "Exit the application.", ExitCommand.MESSAGE_USAGE);
        register(FindCommand.COMMAND_WORD, "Find locations matching your search criteria.", FindCommand.MESSAGE_USAGE);
        register(HelpCommand.COMMAND_WORD, "Show help overview or details for a command.",
                HelpCommand.MESSAGE_USAGE);
        register(ListCommand.COMMAND_WORD, "List all saved locations.", ListCommand.MESSAGE_USAGE);
        register(NoteCommand.COMMAND_WORD, "Record a note or delete notes by date.", NoteCommand.MESSAGE_USAGE);
        register(PlanCommand.COMMAND_WORD, "Show or clear the planner for a specific date.", PlanCommand.MESSAGE_USAGE);
        register(RedoCommand.COMMAND_WORD, "Reapply the most recent undone change.",
                RedoCommand.MESSAGE_USAGE);
        register(ShortcutCommand.COMMAND_WORD, "Manage command shortcuts.", ShortcutCommand.MESSAGE_USAGE);
        register(ThemeCommand.COMMAND_WORD, "Switch between light and dark themes.", ThemeCommand.MESSAGE_USAGE);
        register(UndoCommand.COMMAND_WORD, "Revert the most recent change.", UndoCommand.MESSAGE_USAGE);
    }

    /**
     * Autocompletes the given input into an existing command.
     */
    public String completePrefix(String prefix) {
        assert (prefix != null);
        List<String> matches = new ArrayList<>();

        for (String cmd : commandRegistry.keySet()) {
            if (cmd.startsWith(prefix.toLowerCase())) {
                matches.add(cmd);
            }
        }

        if (matches.isEmpty()) {
            return prefix;
        }
        // only one match
        if (matches.size() == 1) {
            return matches.get(0);
        }

        return longestCommonPrefix(matches);
    }

    /**
     * Returns true if {@code commandWord} is a known command word.
     */
    public boolean isKnownCommand(String commandWord) {
        return commandRegistry.containsKey(commandWord.toLowerCase());
    }

    /**
     * Returns the known command words.
     */
    public Set<String> getCommandWords() {
        return Set.copyOf(commandRegistry.keySet());
    }

    /**
     * Returns the help overview text for all built-in commands.
     */
    public String getHelpOverview() {
        StringBuilder builder = new StringBuilder(OVERVIEW_INTRO);
        commandRegistry.values().forEach(commandInfo -> builder.append("\n")
                .append(commandInfo.commandWord())
                .append(": ")
                .append(commandInfo.summary()));
        builder.append("\n\n").append(OVERVIEW_FOOTER);
        return builder.toString();
    }

    /**
     * Returns detailed help text for the given command word, if known.
     */
    public Optional<String> getDetailedHelp(String commandWord) {
        if (commandWord == null) {
            return Optional.empty();
        }

        CommandInfo commandInfo = commandRegistry.get(commandWord.toLowerCase());
        if (commandInfo == null) {
            return Optional.empty();
        }

        // return Optional.of(commandInfo.summary() + "\n\n" + commandInfo.usage());
        return Optional.of(commandInfo.usage());
    }

    private void register(String commandWord, String summary, String usage) {
        commandRegistry.put(commandWord, new CommandInfo(commandWord, summary, usage));
    }

    /**
     * Finds the longest common prefix of a list of words.
     *
     * @param words a list of at least 2 strings
     * @return the longest common prefix of all the strings passed in
     */
    private String longestCommonPrefix(List<String> words) {
        String min = Collections.min(words);
        String max = Collections.max(words);

        int i = 0;

        while (i < min.length() && i < max.length() && min.charAt(i) == max.charAt(i)) {
            i++;
        }

        return min.substring(0, i);
    }

    private record CommandInfo(String commandWord, String summary, String usage) { }
}
