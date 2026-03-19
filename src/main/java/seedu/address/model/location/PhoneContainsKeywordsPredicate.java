package seedu.address.model.location;

import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Location}'s {@code Phone} matches the keyword given.
 */
public class PhoneContainsKeywordsPredicate implements Predicate<Location> {
    private final String keyword;

    public PhoneContainsKeywordsPredicate(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public boolean test(Location location) {
        return location.getPhone()
                .map(phone -> StringUtil.containsSubstringIgnoreCase(phone.value, keyword))
                .orElse(false);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PhoneContainsKeywordsPredicate)) {
            return false;
        }

        PhoneContainsKeywordsPredicate otherPredicate = (PhoneContainsKeywordsPredicate) other;
        return keyword.equals(otherPredicate.keyword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keyword", keyword).toString();
    }
}
