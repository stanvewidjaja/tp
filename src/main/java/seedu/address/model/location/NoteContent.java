package seedu.address.model.location;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the content of a Note in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidNoteContent(String)}
 */
public class NoteContent {

    public static final String MESSAGE_CONSTRAINTS =
            "Note content should not be blank";

    /*
     * The first character of the note must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     * Use DOTALL to match newlines as well.
     */
    public static final String VALIDATION_REGEX = "(?s)[^\\s].*";

    public final String value;

    /**
     * Constructs a {@code NoteContent}.
     *
     * @param noteContent A valid note content.
     */
    public NoteContent(String noteContent) {
        requireNonNull(noteContent);
        checkArgument(isValidNoteContent(noteContent), MESSAGE_CONSTRAINTS);
        value = noteContent;
    }

    /**
     * Returns true if a given string is a valid note content.
     */
    public static boolean isValidNoteContent(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NoteContent)) {
            return false;
        }

        NoteContent otherNoteContent = (NoteContent) other;
        return value.equals(otherNoteContent.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
