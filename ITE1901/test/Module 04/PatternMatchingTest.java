import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;


class PatternMatchingTest {
    PatternMatching patternMatching = new PatternMatching();

    @ParameterizedTest
    @CsvFileSource(resources = "PatternMatchingExamples.csv", delimiter = '~')
    void testIndexesOfKnownStrings(String str, String pattern, int expected) {
        assertEquals(patternMatching.findPatternInString(str, pattern), expected);
    }

}