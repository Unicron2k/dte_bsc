import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashForStringTest {

    @Test
    void hashCodeForString() {
        String teststring = "teststring123456";
        assertEquals((int)HashForString.hashCodeForString(teststring), teststring.hashCode());
    }
}