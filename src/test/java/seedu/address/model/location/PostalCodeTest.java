package seedu.address.model.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PostalCodeTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PostalCode(null));
    }

    @Test
    public void constructor_invalidPostalCode_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new PostalCode("12@45"));
    }

    @Test
    public void isValidPostalCode() {
        assertFalse(PostalCode.isValidPostalCode("")); // empty string
        assertFalse(PostalCode.isValidPostalCode(" ")); // spaces only
        assertFalse(PostalCode.isValidPostalCode("12 45")); // contains space
        assertFalse(PostalCode.isValidPostalCode("12@45")); // contains symbol

        assertTrue(PostalCode.isValidPostalCode("530456"));
        assertTrue(PostalCode.isValidPostalCode("A12345"));
    }

    @Test
    public void equals() {
        PostalCode postalCode = new PostalCode("530456");
        PostalCode samePostalCode = new PostalCode("530456");
        PostalCode differentPostalCode = new PostalCode("640123");

        // same values -> true
        assertTrue(postalCode.equals(samePostalCode));

        // same object -> true
        assertTrue(postalCode.equals(postalCode));

        // null -> false
        assertFalse(postalCode.equals(null));

        // different type -> false
        assertFalse(postalCode.equals(5));

        // different value -> false
        assertFalse(postalCode.equals(differentPostalCode));
    }

    @Test
    public void hashCode_sameValue_sameHashCode() {
        PostalCode first = new PostalCode("530456");
        PostalCode second = new PostalCode("530456");

        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void toString_returnsValue() {
        PostalCode postalCode = new PostalCode("530456");
        assertEquals("530456", postalCode.toString());
    }
}
