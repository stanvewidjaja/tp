package seedu.address.logic;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.CommandDatabase;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;

/**
 * Manages command shortcuts and command expansion.
 */
public class ShortcutManager {
    public static final String MESSAGE_INVALID_ALIAS =
            "Alias must start with a letter and contain only alphanumeric characters.";
    public static final String MESSAGE_RESERVED_ALIAS =
            "Alias cannot be the same as an existing command word.";
    public static final String MESSAGE_ALIAS_EXISTS =
            "Alias already exists: %1$s";
    public static final String MESSAGE_ALIAS_NOT_FOUND =
            "Alias does not exist: %1$s";
    public static final String MESSAGE_INVALID_COMMAND_WORD =
            "Shortcut target must be an existing command word.";
    public static final String MESSAGE_USAGE_LIST_EMPTY = "No shortcuts defined.";

    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<firstToken>\\S+)(?<remainder>.*)");
    private static final Pattern VALID_ALIAS_PATTERN = Pattern.compile("[A-Za-z][A-Za-z0-9]*");

    private final Model model;
    private final CommandDatabase commandDatabase;

    /**
     * Creates a {@code ShortcutManager} with the given {@code Model} and {@code CommandDatabase}.
     *
     * @param model model containing the shortcut mappings
     * @param commandDatabase command database used to validate command words
     */
    public ShortcutManager(Model model, CommandDatabase commandDatabase) {
        requireNonNull(model);
        requireNonNull(commandDatabase);
        this.model = model;
        this.commandDatabase = commandDatabase;
    }

    /**
     * Expands the first token in {@code userInput} if it is a shortcut.
     */
    public String expandShortcut(String userInput) {
        requireNonNull(userInput);

        String trimmedInput = userInput.trim();
        if (trimmedInput.isEmpty()) {
            return userInput;
        }

        Matcher matcher = BASIC_COMMAND_FORMAT.matcher(trimmedInput);
        if (!matcher.matches()) {
            return userInput;
        }

        String originalFirstToken = matcher.group("firstToken");
        String normalizedFirstToken = normalizeToken(originalFirstToken);
        String remainder = matcher.group("remainder");
        String expandedToken = model.getShortcutMap().get(normalizedFirstToken);
        if (expandedToken == null) {
            // No shortcut mapping; return the original (trimmed) input unchanged.
            return trimmedInput;
        }
        return expandedToken + remainder;
    }

    /**
     * Adds a new shortcut.
     */
    public void addShortcut(String alias, String commandWord) throws ParseException {
        String normalizedAlias = normalizeAndValidateAlias(alias);
        String normalizedCommandWord = normalizeAndValidateCommandWord(commandWord);

        if (model.hasShortcut(normalizedAlias)) {
            throw new ParseException(String.format(MESSAGE_ALIAS_EXISTS, normalizedAlias));
        }

        model.setShortcut(normalizedAlias, normalizedCommandWord);
    }

    /**
     * Removes an existing shortcut.
     */
    public void removeShortcut(String alias) throws ParseException {
        String normalizedAlias = normalizeToken(alias);
        validateAliasSyntax(normalizedAlias);

        if (!model.hasShortcut(normalizedAlias)) {
            throw new ParseException(String.format(MESSAGE_ALIAS_NOT_FOUND, normalizedAlias));
        }

        model.removeShortcut(normalizedAlias);
    }

    /**
     * Returns a user-friendly listing of all shortcuts.
     */
    public String formatShortcutList() {
        Map<String, String> shortcuts = model.getShortcutMap();
        if (shortcuts.isEmpty()) {
            return MESSAGE_USAGE_LIST_EMPTY;
        }

        StringJoiner joiner = new StringJoiner("\n");
        shortcuts.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry<String, String>::getValue)
                        .thenComparing(Map.Entry::getKey))
                .forEach(entry -> joiner.add(entry.getKey() + " -> " + entry.getValue()));
        return joiner.toString();
    }

    private String normalizeAndValidateAlias(String alias) throws ParseException {
        String normalizedAlias = normalizeToken(alias);
        validateAliasSyntax(normalizedAlias);

        if (commandDatabase.isKnownCommand(normalizedAlias)) {
            throw new ParseException(MESSAGE_RESERVED_ALIAS);
        }

        return normalizedAlias;
    }

    private String normalizeAndValidateCommandWord(String commandWord) throws ParseException {
        String normalizedCommandWord = normalizeToken(commandWord);
        if (!commandDatabase.isKnownCommand(normalizedCommandWord)) {
            throw new ParseException(MESSAGE_INVALID_COMMAND_WORD);
        }
        return normalizedCommandWord;
    }

    private void validateAliasSyntax(String alias) throws ParseException {
        if (!VALID_ALIAS_PATTERN.matcher(alias).matches()) {
            throw new ParseException(MESSAGE_INVALID_ALIAS);
        }
    }

    private String normalizeToken(String token) {
        requireNonNull(token);
        return token.trim().toLowerCase();
    }
}
