package seedu.address.model.location.predicates;

import java.util.function.Predicate;

import seedu.address.model.location.Location;

/**
 * Tests that a {@code Location}'s postal code contains the given keyword.
 */
public class PostalCodeContainsKeywordsPredicate implements Predicate<Location> {
    private final String keyword;

    public PostalCodeContainsKeywordsPredicate(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public boolean test(Location location) {
        return location.getPostalCode()
                .map(postalCode -> postalCode.toString().toLowerCase()
                        .contains(keyword.toLowerCase()))
                .orElse(false);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof PostalCodeContainsKeywordsPredicate
                && keyword.equals(((PostalCodeContainsKeywordsPredicate) other).keyword));
    }

    @Override
    public String toString() {
        return PostalCodeContainsKeywordsPredicate.class.getCanonicalName() + "{keyword=" + keyword + "}";
    }
}
