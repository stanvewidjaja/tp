package seedu.address.model.location;

import java.time.LocalDate;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Location}'s {@code VisitDate} matches the date given.
 */
public class VisitDateMatchesKeywordsPredicate implements Predicate<Location> {
    private final LocalDate date;

    public VisitDateMatchesKeywordsPredicate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean test(Location location) {
        return location.getVisitDates().stream()
                .anyMatch(visitDate -> visitDate.getValue().equals(date));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof VisitDateMatchesKeywordsPredicate)) {
            return false;
        }

        VisitDateMatchesKeywordsPredicate otherPredicate = (VisitDateMatchesKeywordsPredicate) other;
        return date.equals(otherPredicate.date);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("date", date).toString();
    }
}
