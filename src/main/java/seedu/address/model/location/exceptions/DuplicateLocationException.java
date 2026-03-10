package seedu.address.model.location.exceptions;

/**
 * Signals that the operation will result in duplicate Locations (Locations are considered duplicates if they have
 * the same identity).
 */
public class DuplicateLocationException extends RuntimeException {
    public DuplicateLocationException() {
        super("Operation would result in duplicate locations");
    }
}
