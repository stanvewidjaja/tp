package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE_ADD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE_REMOVE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSTAL_CODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_ADD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_REMOVE;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditLocationDescriptor;
import seedu.address.logic.parser.exceptions.EmptyFieldException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.location.dates.VisitDate;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object.
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                        PREFIX_POSTAL_CODE, PREFIX_DATE, PREFIX_TAG,
                        PREFIX_DATE_ADD, PREFIX_DATE_REMOVE,
                        PREFIX_TAG_ADD, PREFIX_TAG_REMOVE);

        Index index;

        String preamble = argMultimap.getPreamble().trim();
        String[] preambleParts = preamble.split("\\s+");

        if (preamble.isEmpty() || preambleParts.length != 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        index = ParserUtil.parseIndex(preamble);

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_POSTAL_CODE);
        validateVisitDatePrefixCombination(argMultimap);
        validateTagPrefixCombination(argMultimap);

        EditLocationDescriptor editLocationDescriptor = new EditLocationDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editLocationDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        try {
            if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
                editLocationDescriptor.setPhone(
                        ParserUtil.parseOptionalPhone(argMultimap.getValue(PREFIX_PHONE).get()));
            }
        } catch (EmptyFieldException e) {
            editLocationDescriptor.setClearPhone();
        }
        try {
            if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
                editLocationDescriptor.setEmail(
                        ParserUtil.parseOptionalEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
            }
        } catch (EmptyFieldException e) {
            editLocationDescriptor.setClearEmail();
        }
        try {
            if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
                editLocationDescriptor.setAddress(
                        ParserUtil.parseOptionalAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
            }
        } catch (EmptyFieldException e) {
            editLocationDescriptor.setClearAddress();
        }
        try {
            if (argMultimap.getValue(PREFIX_POSTAL_CODE).isPresent()) {
                editLocationDescriptor.setPostalCode(
                        ParserUtil.parseOptionalPostalCode(argMultimap.getValue(PREFIX_POSTAL_CODE).get()));
            }
        } catch (EmptyFieldException e) {
            editLocationDescriptor.setClearPostal();
        }

        parseVisitDatesForEdit(argMultimap.getAllValues(PREFIX_DATE))
                .ifPresent(editLocationDescriptor::setVisitDates);
        parseVisitDatesForEdit(argMultimap.getAllValues(PREFIX_DATE_ADD))
                .ifPresent(editLocationDescriptor::setVisitDatesToAdd);
        parseVisitDatesForEdit(argMultimap.getAllValues(PREFIX_DATE_REMOVE))
                .ifPresent(editLocationDescriptor::setVisitDatesToRemove);

        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG))
                .ifPresent(editLocationDescriptor::setTags);
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG_ADD))
                .ifPresent(editLocationDescriptor::setTagsToAdd);
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG_REMOVE))
                .ifPresent(editLocationDescriptor::setTagsToRemove);

        if (!editLocationDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editLocationDescriptor);
    }

    private void validateVisitDatePrefixCombination(ArgumentMultimap argMultimap) throws ParseException {
        boolean hasVisitDateOverride = !argMultimap.getAllValues(PREFIX_DATE).isEmpty();
        boolean hasVisitDateAdd = !argMultimap.getAllValues(PREFIX_DATE_ADD).isEmpty();
        boolean hasVisitDateRemove = !argMultimap.getAllValues(PREFIX_DATE_REMOVE).isEmpty();

        if (hasVisitDateOverride && (hasVisitDateAdd || hasVisitDateRemove)) {
            throw new ParseException(EditCommand.MESSAGE_CANNOT_OVERRIDE_AND_MODIFY_DATES);
        }
    }

    private void validateTagPrefixCombination(ArgumentMultimap argMultimap) throws ParseException {
        boolean hasTagOverride = !argMultimap.getAllValues(PREFIX_TAG).isEmpty();
        boolean hasTagAdd = !argMultimap.getAllValues(PREFIX_TAG_ADD).isEmpty();
        boolean hasTagRemove = !argMultimap.getAllValues(PREFIX_TAG_REMOVE).isEmpty();

        if (hasTagOverride && (hasTagAdd || hasTagRemove)) {
            throw new ParseException(EditCommand.MESSAGE_CANNOT_OVERRIDE_AND_MODIFY_TAGS);
        }
    }

    private Optional<Set<VisitDate>> parseVisitDatesForEdit(Collection<String> visitDates) throws ParseException {
        assert visitDates != null;

        if (visitDates.isEmpty()) {
            return Optional.empty();
        }

        Collection<String> visitDateSet =
                visitDates.size() == 1 && visitDates.contains("")
                        ? Collections.emptySet()
                        : visitDates;

        return Optional.of(ParserUtil.parseVisitDates(visitDateSet));
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }

        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }
}
