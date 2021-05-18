import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FizzBuzzTest {

    @Test
    void of_0_returns_0(){
        assertEquals("0", FizzBuzz.of(0));
    }
    @Test
    void of_1_returns_1(){
        assertEquals("1", FizzBuzz.of(1));
    }
    @Test
    void of_2_returns_2(){
        assertEquals("2", FizzBuzz.of(2));
    }
    @Test
    void of_3_returns_fizz(){
        assertEquals("fizz", FizzBuzz.of(3));
    }
    @Test
    void of_5_returns_5(){
        assertEquals("buzz", FizzBuzz.of(5));
    }
    @Test
    void of_6_returns_fizz(){
        assertEquals("fizz", FizzBuzz.of(6));
    }
    @Test
    void of_10_returns_buzz(){
        assertEquals("buzz", FizzBuzz.of(10));
    }
    @Test
    void of_15_returns_fizzbuzz(){
        assertEquals("fizzbuzz", FizzBuzz.of(15));
    }
    @Test
    void of_30_returns_fizzbuzz(){
        assertEquals("fizzbuzz", FizzBuzz.of(30));
    }
    @Test
    void of_negative_1_returns_negative_1(){
        assertEquals("-1", FizzBuzz.of(-1));
    }
    @Test
    void of_negative_3_returns_negative_3(){
        assertEquals("-3", FizzBuzz.of(-3));
    }
    @Test
    void of_negative_5_returns_negative_5(){
        assertEquals("-5", FizzBuzz.of(-5));
    }
    @Test
    void of_negative_15_returns_negative_15(){
        assertEquals("-15", FizzBuzz.of(-15));
    }
}