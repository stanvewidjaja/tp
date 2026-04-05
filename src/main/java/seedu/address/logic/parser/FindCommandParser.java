package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.location.Location;
import seedu.address.model.location.predicates.AddressContainsKeywordsPredicate;
import seedu.address.model.location.predicates.CombinedLocationPredicate;
import seedu.address.model.location.predicates.EmailContainsKeywordsPredicate;
import seedu.address.model.location.predicates.NameContainsKeywordsPredicate;
import seedu.address.model.location.predicates.PhoneContainsKeywordsPredicate;
import seedu.address.model.location.predicates.TagMatchesKeywordsPredicate;
import seedu.address.model.location.predicates.VisitDateMatchesKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {

        if (containsUnsupportedPrefix(args)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                        PREFIX_ADDRESS, PREFIX_TAG, PREFIX_DATE);

        List<Predicate<Location>> predicates = new ArrayList<>();

        // 1. Parse preamble for names (backward compatibility - OR logic)
        String preamble = argMultimap.getPreamble().trim();
        if (!preamble.isEmpty()) {
            String[] nameKeywords = preamble.split("\\s+");
            predicates.add(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
        }

        // 2. Parse Name prefixes (AND logic)
        for (String nameKeyword : argMultimap.getAllValues(PREFIX_NAME)) {
            if (nameKeyword.trim().isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
            predicates.add(new NameContainsKeywordsPredicate(Collections.singletonList(nameKeyword)));
        }

        // 3. Parse Phone prefixes (AND logic)
        for (String phoneKeyword : argMultimap.getAllValues(PREFIX_PHONE)) {
            if (phoneKeyword.trim().isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
            predicates.add(new PhoneContainsKeywordsPredicate(phoneKeyword));
        }

        // 4. Parse Email prefixes (AND logic)
        for (String emailKeyword : argMultimap.getAllValues(PREFIX_EMAIL)) {
            if (emailKeyword.trim().isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
            predicates.add(new EmailContainsKeywordsPredicate(emailKeyword));
        }

        // 5. Parse Addresses (AND logic)
        for (String addressKeyword : argMultimap.getAllValues(PREFIX_ADDRESS)) {
            if (addressKeyword.trim().isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
            predicates.add(new AddressContainsKeywordsPredicate(addressKeyword));
        }

        // 6. Parse Tags (AND logic for multiple tags)
        for (String tagKeyword : argMultimap.getAllValues(PREFIX_TAG)) {
            if (tagKeyword.trim().isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
            predicates.add(new TagMatchesKeywordsPredicate(tagKeyword));
        }

        // 7. Parse Dates (AND logic)
        for (String dateKeyword : argMultimap.getAllValues(PREFIX_DATE)) {
            try {
                predicates.add(new VisitDateMatchesKeywordsPredicate(DateParser.parse(dateKeyword)));
            } catch (IllegalValueException e) {
                throw new ParseException(e.getMessage() + System.lineSeparator() + FindCommand.MESSAGE_USAGE);
            }
        }

        if (predicates.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return new FindCommand(new CombinedLocationPredicate(predicates));
    }

    /**
     * Returns true if the input contains a prefix not supported by find.
     */
    private boolean containsUnsupportedPrefix(String args) {
        Pattern pattern = Pattern.compile("(^|\\s)([a-zA-Z]+/)");
        Matcher matcher = pattern.matcher(args);

        while (matcher.find()) {
            String prefix = matcher.group(2);
            if (!isSupportedPrefix(prefix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the prefix is supported by find.
     */
    private boolean isSupportedPrefix(String prefix) {
        return prefix.equals(PREFIX_NAME.getPrefix())
                || prefix.equals(PREFIX_PHONE.getPrefix())
                || prefix.equals(PREFIX_EMAIL.getPrefix())
                || prefix.equals(PREFIX_ADDRESS.getPrefix())
                || prefix.equals(PREFIX_TAG.getPrefix())
                || prefix.equals(PREFIX_DATE.getPrefix());
    }

}
