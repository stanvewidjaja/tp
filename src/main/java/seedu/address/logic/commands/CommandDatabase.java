package seedu.address.logic.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the collection of command keywords available to the user
 * and maps potential user shortcuts to the raw commands
 */
public class CommandDatabase {

    private final List<String> commands;

    /**
     * Initializes the database with the command keywords
     */
    public CommandDatabase() {
        // can later be substituted as loading a command txt file
        commands = List.of(
                AddCommand.COMMAND_WORD,
                ClearCommand.COMMAND_WORD,
                DeleteCommand.COMMAND_WORD,
                EditCommand.COMMAND_WORD,
                ExitCommand.COMMAND_WORD,
                FindCommand.COMMAND_WORD,
                HelpCommand.COMMAND_WORD,
                ListCommand.COMMAND_WORD);
    }

    /**
     * Autocompletes the given input into an existing command
     * @param prefix
     * @return
     */
    public String completePrefix(String prefix) {
        List<String> matches = new ArrayList<>();

        for (String cmd : commands) {
            if (cmd.startsWith(prefix.toLowerCase())) {
                matches.add(cmd);
            }
        }
        //
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
     * Finds the longest common prefix of a list of words
     * @param words a list of at least 2 Strings
     * @return the longest common prefix of all the Strings passed in
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
}
