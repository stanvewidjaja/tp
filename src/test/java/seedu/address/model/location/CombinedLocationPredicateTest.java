package seedu.address.model.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.LocationBuilder;

public class CombinedLocationPredicateTest {

    @Test
    public void equals() {
        List<Predicate<Location>> firstPredicateList = Collections.singletonList(new NameContainsKeywordsPredicate(Collections.singletonList("first")));
        List<Predicate<Location>> secondPredicateList = Arrays.asList(new NameContainsKeywordsPredicate(Collections.singletonList("first")), new TagMatchesKeywordsPredicate("tag"));

        CombinedLocationPredicate firstPredicate = new CombinedLocationPredicate(firstPredicateList);
        CombinedLocationPredicate secondPredicate = new CombinedLocationPredicate(secondPredicateList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        CombinedLocationPredicate firstPredicateCopy = new CombinedLocationPredicate(firstPredicateList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different location -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_combinedLocationPredicate_returnsTrue() {
        // Multiple predicates, all matching
        CombinedLocationPredicate predicate = new CombinedLocationPredicate(Arrays.asList(
                new NameContainsKeywordsPredicate(Collections.singletonList("Alice")),
                new TagMatchesKeywordsPredicate("important")
        ));
        assertTrue(predicate.test(new LocationBuilder().withName("Alice").withTags("important").build()));
    }

    @Test
    public void test_combinedLocationPredicate_returnsFalse() {
        // Multiple predicates, one not matching
        CombinedLocationPredicate predicate = new CombinedLocationPredicate(Arrays.asList(
                new NameContainsKeywordsPredicate(Collections.singletonList("Alice")),
                new TagMatchesKeywordsPredicate("important")
        ));
        assertFalse(predicate.test(new LocationBuilder().withName("Alice").withTags("visited").build()));

        // All predicates not matching
        assertFalse(predicate.test(new LocationBuilder().withName("Bob").withTags("visited").build()));
    }

    @Test
    public void toStringMethod() {
        List<Predicate<Location>> predicates = Collections.singletonList(new NameContainsKeywordsPredicate(Collections.singletonList("Alice")));
        CombinedLocationPredicate predicate = new CombinedLocationPredicate(predicates);
        String expected = CombinedLocationPredicate.class.getCanonicalName() + "{predicates=" + predicates + "}";
        assertEquals(expected, predicate.toString());
    }
}
