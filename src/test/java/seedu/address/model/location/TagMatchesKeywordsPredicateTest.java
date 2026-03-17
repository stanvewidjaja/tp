package seedu.address.model.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.LocationBuilder;

public class TagMatchesKeywordsPredicateTest {

    @Test
    public void equals() {
        String firstPredicateKeyword = "first";
        String secondPredicateKeyword = "second";

        TagMatchesKeywordsPredicate firstPredicate = new TagMatchesKeywordsPredicate(firstPredicateKeyword);
        TagMatchesKeywordsPredicate secondPredicate = new TagMatchesKeywordsPredicate(secondPredicateKeyword);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        TagMatchesKeywordsPredicate firstPredicateCopy = new TagMatchesKeywordsPredicate(firstPredicateKeyword);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different location -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_tagMatchesKeywords_returnsTrue() {
        // One keyword
        TagMatchesKeywordsPredicate predicate = new TagMatchesKeywordsPredicate("important");
        assertTrue(predicate.test(new LocationBuilder().withTags("important", "visited").build()));

        // Mixed-case keyword
        predicate = new TagMatchesKeywordsPredicate("ImPoRtAnT");
        assertTrue(predicate.test(new LocationBuilder().withTags("important").build()));
    }

    @Test
    public void test_tagDoesNotMatchKeywords_returnsFalse() {
        // Non-matching keyword
        TagMatchesKeywordsPredicate predicate = new TagMatchesKeywordsPredicate("important");
        assertFalse(predicate.test(new LocationBuilder().withTags("visited").build()));

        // Substring tag match (should be exact match based on implementation)
        predicate = new TagMatchesKeywordsPredicate("import");
        assertFalse(predicate.test(new LocationBuilder().withTags("important").build()));
    }

    @Test
    public void toStringMethod() {
        String keyword = "keyword";
        TagMatchesKeywordsPredicate predicate = new TagMatchesKeywordsPredicate(keyword);
        String expected = TagMatchesKeywordsPredicate.class.getCanonicalName() + "{keyword=" + keyword + "}";
        assertEquals(expected, predicate.toString());
    }
}
