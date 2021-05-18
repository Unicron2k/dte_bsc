import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MyLinkedListTest {

    //<editor-fold desc="Method test: contains(Object e)">
    @Test
    void contains_ReturnsTrueIfObjectExists() {
        MyLinkedList myLinkedList = new MyLinkedList();
        myLinkedList.add("Los Angeles");
        assertTrue(myLinkedList.contains("Los Angeles"));
    }
    @Test
    void contains_ReturnsFalseIfListIsEmptyANDifElementIsNotInList() {
        MyLinkedList myLinkedList = new MyLinkedList();
        myLinkedList.add("One");
        assertFalse(myLinkedList.contains("Two"));
    }
    //</editor-fold>

    //<editor-fold desc="Method test: get(int index)">
    @Test
    void get_ReturnsCorrectElementAtGivenIndex() {
        MyLinkedList myLinkedList = new MyLinkedList();
        myLinkedList.add("First element");
        myLinkedList.add("Second element");
        assertThat(myLinkedList.get(1), equalTo("Second element"));
    }
    @Test
    void get_ThrowsExceptionIfIndexIsOutOfBoundsOfList() {
        MyLinkedList myLinkedList = new MyLinkedList();
        // Check if correct message is thrown
        IndexOutOfBoundsException thrown =
                assertThrows(IndexOutOfBoundsException.class,
                        () -> myLinkedList.get(0),
                        "Expected get(0) to throw, but it didn't");
        assertTrue(thrown.getMessage().contains("Index '0' is out of bounds of list!"));
    }
    //</editor-fold>

    //<editor-fold desc="Method test: indexOf(Object e)">
    @Test
    void indexOf_ReturnsCorrectIndexOfGivenElement() {
        MyLinkedList myLinkedList = new MyLinkedList();
        myLinkedList.add("First element");
        myLinkedList.add("Second element");
        assertThat(myLinkedList.indexOf("Second element"), equalTo(1));
    }
    @Test
    void indexOf_ReturnsErrorValueIfElementIsNotInList() {
        MyLinkedList myLinkedList = new MyLinkedList();
        myLinkedList.add("First element");
        myLinkedList.add("Second element");
        assertThat(myLinkedList.indexOf("Third element"), equalTo(-1));
    }
    //</editor-fold>

    //<editor-fold desc="Method test: lastIndexOf(E e)">
    @Test
    void lastIndexOf_ReturnsCorrectIndexOfElement() {
        MyLinkedList myLinkedList = new MyLinkedList();
        myLinkedList.add("Andy");
        myLinkedList.add("B-Man");
        myLinkedList.add("B-Man");
        assertThat(myLinkedList.lastIndexOf("B-Man"), equalTo(2));
    }
    @Test
    void lastIndexOf_ReturnsErrorValueIfElementNotInList() {
        MyLinkedList myLinkedList = new MyLinkedList();
        myLinkedList.add("Andy");
        myLinkedList.add("Is");
        assertThat(myLinkedList.lastIndexOf("Awesome"), equalTo(-1));
    }
    //</editor-fold>

    //<editor-fold desc="Method test: set(int index, E e)">
    @Test
    void set_newElementWasSuccessfullySetInGivenIndex() {
        MyLinkedList myLinkedList = new MyLinkedList();
        myLinkedList.add("Andy");
        myLinkedList.add("is");
        myLinkedList.add("dumb...");
        myLinkedList.set(2, "smart!");
        assertThat(myLinkedList.get(2), equalTo("smart!"));
    }
    @Test
    void set_returnsPreviousElementOnSuccess() {
        MyLinkedList myLinkedList = new MyLinkedList();
        myLinkedList.add("Ferrari");
        assertThat(myLinkedList.set(0, "Ferrari2"), equalTo("Ferrari"));
    }
    @Test
    void set_ThrowsExceptionIfIndexIsOutOfBoundsOfList() {
        MyLinkedList myLinkedList = new MyLinkedList();
        // Check if correct message is thrown
        IndexOutOfBoundsException thrown =
                assertThrows(IndexOutOfBoundsException.class,
                        () -> myLinkedList.set(5, "testing"),
                        "Expected set(5) to throw, but it didn't");
        assertTrue(thrown.getMessage().contains("Index '5' is out of bounds of list!"));
    }
    //</editor-fold>
}
