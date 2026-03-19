package seedu.address.model.location;

import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Location}'s {@code Email} matches the keyword given.
 */
public class EmailContainsKeywordsPredicate implements Predicate<Location> {
    private final String keyword;

    public EmailContainsKeywordsPredicate(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public boolean test(Location location) {
        return location.getEmail()
                .map(email -> StringUtil.containsSubstringIgnoreCase(email.value, keyword))
                .orElse(false);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EmailContainsKeywordsPredicate)) {
            return false;
        }

        EmailContainsKeywordsPredicate otherPredicate = (EmailContainsKeywordsPredicate) other;
        return keyword.equals(otherPredicate.keyword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keyword", keyword).toString();
    }
}
