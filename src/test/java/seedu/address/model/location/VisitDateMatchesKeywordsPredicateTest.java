package seedu.address.model.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.LocationBuilder;

public class VisitDateMatchesKeywordsPredicateTest {

    @Test
    public void equals() {
        String firstPredicateKeyword = "2024-01-15";
        String secondPredicateKeyword = "2024-01-16";

        VisitDateMatchesKeywordsPredicate firstPredicate = new VisitDateMatchesKeywordsPredicate(firstPredicateKeyword);
        VisitDateMatchesKeywordsPredicate secondPredicate = new VisitDateMatchesKeywordsPredicate(secondPredicateKeyword);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        VisitDateMatchesKeywordsPredicate firstPredicateCopy = new VisitDateMatchesKeywordsPredicate(firstPredicateKeyword);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different location -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_visitDateMatchesKeywords_returnsTrue() {
        // Matching date
        VisitDateMatchesKeywordsPredicate predicate = new VisitDateMatchesKeywordsPredicate("2024-01-15");
        assertTrue(predicate.test(new LocationBuilder().withVisitDate("2024-01-15").build()));
    }

    @Test
    public void test_visitDateDoesNotMatchKeywords_returnsFalse() {
        // Non-matching date
        VisitDateMatchesKeywordsPredicate predicate = new VisitDateMatchesKeywordsPredicate("2024-01-15");
        assertFalse(predicate.test(new LocationBuilder().withVisitDate("2024-01-16").build()));
    }

    @Test
    public void toStringMethod() {
        String keyword = "2024-01-15";
        VisitDateMatchesKeywordsPredicate predicate = new VisitDateMatchesKeywordsPredicate(keyword);
        String expected = VisitDateMatchesKeywordsPredicate.class.getCanonicalName() + "{keyword=" + keyword + "}";
        assertEquals(expected, predicate.toString());
    }
}
