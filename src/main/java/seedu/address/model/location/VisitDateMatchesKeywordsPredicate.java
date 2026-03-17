package seedu.address.model.location;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Location}'s {@code VisitDate} matches the keyword given.
 */
public class VisitDateMatchesKeywordsPredicate implements Predicate<Location> {
    private final String keyword;

    public VisitDateMatchesKeywordsPredicate(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public boolean test(Location location) {
        return location.getVisitDate().toString().equals(keyword);
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
        return keyword.equals(otherPredicate.keyword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keyword", keyword).toString();
    }
}
