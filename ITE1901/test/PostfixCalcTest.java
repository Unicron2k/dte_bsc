import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class PostfixCalcTest {
    PostfixCalc calc;

    @BeforeEach
    void setup(){
        calc = new PostfixCalc();
    }

    @ParameterizedTest
    @CsvSource({
            "4 2 + 3 5 1 - * +,18",
            "4 5 + 3 2 * -,3",
            "- 4 - 3 + 1 +,0",
            "4 55 + 62 23 - *,2301",
            "4 3 23 + * 9 -,95",
            "4 3 23 + * 9 %,5",
            "4 3 0 * + 3 +,7",
            "0 0 * 1 * 1 1 + +,2",
            "23 4 2 / 2 2 * - +,21",
            "23 4 0 / 2 2 * - +,23", //test division by zero(basically ignoring it.....)
            "+ + - 4 3 + 5 5 7 + + *,119",
            "23 4 2 2 + % - 34 3 1 - + -,-13"})

    void parameterizedUnitTest_evaluateExpression_shouldSucceed(String expression, int result) {
        assertThat(calc.evaluateExpression(expression), is(equalTo(result)));
    }

    @Test
    void normalUnitTest_evaluateExpressionWithNegativeResult_shouldSucceed(){
        assertThat(calc.evaluateExpression("23 8 91 8 + - * 7 5 - %"), is(equalTo(-1)));
    }

    @Test
    void normalUnitTest_evaluateExpressionWithNegativeNumber_shouldSucceed(){
        assertThat(calc.evaluateExpression("23 -8 91 8 + - * 7 5 - %"), is(equalTo(1)));
    }

    @Test
    void normalUnitTest_evaluateExpressionWithPrefixes_shouldSucceed(){
        assertThat(calc.evaluateExpression("+ * % / 23 -8 91 8 + - * 7 5 - %"), is(equalTo(1)));
    }


}