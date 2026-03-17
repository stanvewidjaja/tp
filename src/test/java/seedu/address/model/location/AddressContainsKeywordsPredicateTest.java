package seedu.address.model.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.LocationBuilder;

public class AddressContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        String firstPredicateKeyword = "first";
        String secondPredicateKeyword = "second";

        AddressContainsKeywordsPredicate firstPredicate = new AddressContainsKeywordsPredicate(firstPredicateKeyword);
        AddressContainsKeywordsPredicate secondPredicate = new AddressContainsKeywordsPredicate(secondPredicateKeyword);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        AddressContainsKeywordsPredicate firstPredicateCopy = new AddressContainsKeywordsPredicate(firstPredicateKeyword);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different location -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_addressContainsKeywords_returnsTrue() {
        // One keyword
        AddressContainsKeywordsPredicate predicate = new AddressContainsKeywordsPredicate("Main");
        assertTrue(predicate.test(new LocationBuilder().withAddress("123 Main St").build()));

        // Mixed-case keyword
        predicate = new AddressContainsKeywordsPredicate("mAiN");
        assertTrue(predicate.test(new LocationBuilder().withAddress("123 Main St").build()));

        // Substring
        predicate = new AddressContainsKeywordsPredicate("clem");
        assertTrue(predicate.test(new LocationBuilder().withAddress("Clementi Road").build()));
    }

    @Test
    public void test_addressDoesNotContainKeywords_returnsFalse() {
        // Non-matching keyword
        AddressContainsKeywordsPredicate predicate = new AddressContainsKeywordsPredicate("Clementi");
        assertFalse(predicate.test(new LocationBuilder().withAddress("Main Street").build()));
    }

    @Test
    public void toStringMethod() {
        String keyword = "keyword";
        AddressContainsKeywordsPredicate predicate = new AddressContainsKeywordsPredicate(keyword);
        String expected = AddressContainsKeywordsPredicate.class.getCanonicalName() + "{keyword=" + keyword + "}";
        assertEquals(expected, predicate.toString());
    }
}
