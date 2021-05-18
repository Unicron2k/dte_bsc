import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

    @ParameterizedTest
    @ValueSource(strings={"pass123","password12","password123%", "Password###123"})
    @NullAndEmptySource
    void checkPassword_StringAlphaNumeric_fail(String password) {
        assertFalse(Password.checkPassword(password));
    }

    @ParameterizedTest
    @ValueSource(strings={"password123","PASSWORD123"})
    void checkPassword_StringAlphaNumeric_success(String password) {
        assertTrue(Password.checkPassword(password));
    }
}