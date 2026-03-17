package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.location.AddressContainsKeywordsPredicate;
import seedu.address.model.location.CombinedLocationPredicate;
import seedu.address.model.location.Location;
import seedu.address.model.location.NameContainsKeywordsPredicate;
import seedu.address.model.location.TagMatchesKeywordsPredicate;
import seedu.address.model.location.VisitDateMatchesKeywordsPredicate;

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
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ADDRESS, PREFIX_TAG, PREFIX_DATE);

        List<Predicate<Location>> predicates = new ArrayList<>();

        // 1. Parse preamble for names (backward compatibility - OR logic)
        String preamble = argMultimap.getPreamble().trim();
        if (!preamble.isEmpty()) {
            String[] nameKeywords = preamble.split("\\s+");
            predicates.add(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
        }

        // 2. Parse Addresses (AND logic)
        for (String addressKeyword : argMultimap.getAllValues(PREFIX_ADDRESS)) {
            predicates.add(new AddressContainsKeywordsPredicate(addressKeyword));
        }

        // 3. Parse Tags (AND logic for multiple tags)
        for (String tagKeyword : argMultimap.getAllValues(PREFIX_TAG)) {
            predicates.add(new TagMatchesKeywordsPredicate(tagKeyword));
        }

        // 4. Parse Dates (AND logic)
        for (String dateKeyword : argMultimap.getAllValues(PREFIX_DATE)) {
            try {
                predicates.add(new VisitDateMatchesKeywordsPredicate(DateParser.parse(dateKeyword)));
            } catch (IllegalValueException e) {
                throw new ParseException(e.getMessage());
            }
        }

        if (predicates.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return new FindCommand(new CombinedLocationPredicate(predicates));
    }

}
