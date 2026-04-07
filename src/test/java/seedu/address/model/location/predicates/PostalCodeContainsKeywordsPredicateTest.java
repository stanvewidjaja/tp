package seedu.address.model.location.predicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.LocationBuilder;

public class PostalCodeContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        String firstKeyword = "123456";
        String secondKeyword = "654321";

        PostalCodeContainsKeywordsPredicate firstPredicate =
                new PostalCodeContainsKeywordsPredicate(firstKeyword);
        PostalCodeContainsKeywordsPredicate secondPredicate =
                new PostalCodeContainsKeywordsPredicate(secondKeyword);

        // same object -> true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> true
        PostalCodeContainsKeywordsPredicate firstPredicateCopy =
                new PostalCodeContainsKeywordsPredicate(firstKeyword);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> false
        assertFalse(firstPredicate.equals(1));

        // null -> false
        assertFalse(firstPredicate.equals(null));

        // different keyword -> false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_postalCodeContainsKeywords_returnsTrue() {
        // exact match
        PostalCodeContainsKeywordsPredicate predicate =
                new PostalCodeContainsKeywordsPredicate("123456");
        assertTrue(predicate.test(new LocationBuilder().withPostalCode("123456").build()));

        // substring match
        predicate = new PostalCodeContainsKeywordsPredicate("234");
        assertTrue(predicate.test(new LocationBuilder().withPostalCode("123456").build()));

        // case-insensitive
        predicate = new PostalCodeContainsKeywordsPredicate("abc");
        assertTrue(predicate.test(new LocationBuilder().withPostalCode("ABC123").build()));
    }

    @Test
    public void test_postalCodeDoesNotContainKeywords_returnsFalse() {
        // non-matching keyword
        PostalCodeContainsKeywordsPredicate predicate =
                new PostalCodeContainsKeywordsPredicate("999");
        assertFalse(predicate.test(new LocationBuilder().withPostalCode("123456").build()));

        // different postal code
        predicate = new PostalCodeContainsKeywordsPredicate("ABC");
        assertFalse(predicate.test(new LocationBuilder().withPostalCode("123456").build()));
    }

    @Test
    public void toStringMethod() {
        String keyword = "123456";
        PostalCodeContainsKeywordsPredicate predicate =
                new PostalCodeContainsKeywordsPredicate(keyword);
        String expected = PostalCodeContainsKeywordsPredicate.class.getCanonicalName()
                + "{keyword=" + keyword + "}";
        assertEquals(expected, predicate.toString());
    }
}
