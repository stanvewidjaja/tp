package seedu.address.model.location;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Location}'s fields match all the predicates given.
 */
public class CombinedLocationPredicate implements Predicate<Location> {
    private final List<Predicate<Location>> predicates;

    public CombinedLocationPredicate(List<Predicate<Location>> predicates) {
        this.predicates = predicates;
    }

    @Override
    public boolean test(Location location) {
        return predicates.stream().allMatch(predicate -> predicate.test(location));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CombinedLocationPredicate)) {
            return false;
        }

        CombinedLocationPredicate otherCombinedLocationPredicate = (CombinedLocationPredicate) other;
        return predicates.equals(otherCombinedLocationPredicate.predicates);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("predicates", predicates).toString();
    }
}
