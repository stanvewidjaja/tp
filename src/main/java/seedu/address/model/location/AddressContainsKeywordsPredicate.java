package seedu.address.model.location;

import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Location}'s {@code Address} matches the keyword given.
 */
public class AddressContainsKeywordsPredicate implements Predicate<Location> {
    private final String keyword;

    public AddressContainsKeywordsPredicate(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public boolean test(Location location) {
        return location.getAddress()
                .map(address -> StringUtil.containsSubstringIgnoreCase(address.value, keyword))
                .orElse(false);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressContainsKeywordsPredicate)) {
            return false;
        }

        AddressContainsKeywordsPredicate otherPredicate = (AddressContainsKeywordsPredicate) other;
        return keyword.equals(otherPredicate.keyword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keyword", keyword).toString();
    }
}
