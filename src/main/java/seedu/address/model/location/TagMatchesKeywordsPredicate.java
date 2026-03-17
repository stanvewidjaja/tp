package seedu.address.model.location;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Location}'s {@code Tag} matches the keyword given.
 */
public class TagMatchesKeywordsPredicate implements Predicate<Location> {
    private final String keyword;

    public TagMatchesKeywordsPredicate(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public boolean test(Location location) {
        return location.getTags().stream()
                .anyMatch(tag -> tag.tagName.equalsIgnoreCase(keyword));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagMatchesKeywordsPredicate)) {
            return false;
        }

        TagMatchesKeywordsPredicate otherPredicate = (TagMatchesKeywordsPredicate) other;
        return keyword.equals(otherPredicate.keyword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keyword", keyword).toString();
    }
}
