package seedu.address.model.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.LocationBuilder;

public class VisitDateMatchesKeywordsPredicateTest {

    @Test
    public void equals() {
        LocalDate firstPredicateDate = LocalDate.parse("2024-01-15");
        LocalDate secondPredicateDate = LocalDate.parse("2024-01-16");

        VisitDateMatchesKeywordsPredicate firstPredicate = new VisitDateMatchesKeywordsPredicate(firstPredicateDate);
        VisitDateMatchesKeywordsPredicate secondPredicate = new VisitDateMatchesKeywordsPredicate(secondPredicateDate);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        VisitDateMatchesKeywordsPredicate firstPredicateCopy = new VisitDateMatchesKeywordsPredicate(firstPredicateDate);
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
        VisitDateMatchesKeywordsPredicate predicate = new VisitDateMatchesKeywordsPredicate(LocalDate.parse("2024-01-15"));
        assertTrue(predicate.test(new LocationBuilder().withVisitDate("2024-01-15").build()));
    }

    @Test
    public void test_visitDateDoesNotMatchKeywords_returnsFalse() {
        // Non-matching date
        VisitDateMatchesKeywordsPredicate predicate = new VisitDateMatchesKeywordsPredicate(LocalDate.parse("2024-01-15"));
        assertFalse(predicate.test(new LocationBuilder().withVisitDate("2024-01-16").build()));
    }

    @Test
    public void toStringMethod() {
        LocalDate date = LocalDate.parse("2024-01-15");
        VisitDateMatchesKeywordsPredicate predicate = new VisitDateMatchesKeywordsPredicate(date);
        String expected = VisitDateMatchesKeywordsPredicate.class.getCanonicalName() + "{date=" + date + "}";
        assertEquals(expected, predicate.toString());
    }
}
