import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class YourClassTest {

    @Test
    void testConvertToString_withNonNullObject() {
        YourClass yourClass = new YourClass();
        Object obj = 123;  // A non-null object (e.g., Integer)
        String result = yourClass.convertToString(obj);
        assertEquals("123", result, "The string representation of the object should be '123'");
    }

    @Test
    void testConvertToString_withNullObject() {
        YourClass yourClass = new YourClass();
        Object obj = null;  // A null object
        String result = yourClass.convertToString(obj);
        assertNull(result, "The result should be null when the object is null");
    }

    @Test
    void testConvertToString_withEmptyString() {
        YourClass yourClass = new YourClass();
        Object obj = "";  // An empty string
        String result = yourClass.convertToString(obj);
        assertEquals("", result, "The string representation of an empty string should be an empty string");
    }

    @Test
    void testConvertToString_withStringObject() {
        YourClass yourClass = new YourClass();
        Object obj = "Hello World";  // A non-null string object
        String result = yourClass.convertToString(obj);
        assertEquals("Hello World", result, "The string representation of 'Hello World' should be 'Hello World'");
    }
}
