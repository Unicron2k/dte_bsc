import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class NumbersTest {

    private Numbers Numbers;

    @Test
    void isOdd_OddNumbers_ReturnsTrue(){
        assertTrue(Numbers.isOdd(5));
    }

    @ParameterizedTest
    @ValueSource(ints={1,3,5,7,9,-15})
    void isOdd_OddNumbersParameterized_ReturnsTrue(int number){
        assertTrue(Numbers.isOdd(number));
    }
}