import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class GenericMergeSortTest {

    @Test
    void mergeSortIntegers() {
        Integer[] integersToTest ={9,8,7,6,5,4,3,2,1};
        Integer[] integersSorted = {1,2,3,4,5,6,7,8,9};
        GenericMergeSort.mergeSort(integersToTest);
        assertThat(integersToTest, is(equalTo(integersSorted)));
    }
    @Test
    void mergeSortStrings() {
        String[] stringsToTest = {"Illinois", "Idaho",  "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Mississippi",
                "Alabama", "Alaska", "Arizona", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Wisconsin", "Wyoming", "Minnesota", "Missouri",
                "Rhode Island", "South Carolina", "South Dakota", "Arkansas", "Tennessee", "New Jersey", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia",
                "Montana", "Nebraska", "Nevada", "New Hampshire", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania"};

        String[] stringsSorted = {"Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii",
                "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri",
                "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania",
                "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"};
        GenericMergeSort.mergeSort(stringsToTest);
        assertThat(stringsToTest, is(equalTo(stringsSorted)));
    }
    @Test
    void mergeSortDoubles() {
        Double[] doublesToTest = {22.4, 88.8, 3.14, 0.0000000000001, 99.9, 0.2, 0.1, 0.3, 0.7, 0.5, 0.4, 0.0001};
        Double[] doublesSorted = {0.0000000000001, 0.0001, 0.1, 0.2, 0.3, 0.4, 0.5, 0.7, 3.14, 22.4, 88.8, 99.9};
        GenericMergeSort.mergeSort(doublesToTest);
        assertThat(doublesToTest, is(equalTo(doublesSorted)));
    }
}