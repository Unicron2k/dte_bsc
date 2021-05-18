import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MyTwoWayLinkedListTest {

    //<editor-fold desc="Method test: contains(Object e)">
    @Test
    void contains_ReturnsTrueIfObjectExists() {
        MyTwoWayLinkedList myTwoWayLinkedList = new MyTwoWayLinkedList();
        myTwoWayLinkedList.add("Los Angeles");
        assertTrue(myTwoWayLinkedList.contains("Los Angeles"));
    }
    @Test
    void contains_ReturnsFalseIfListIsEmptyANDifElementIsNotInList() {
        MyTwoWayLinkedList myTwoWayLinkedList = new MyTwoWayLinkedList();
        myTwoWayLinkedList.add("One");
        assertFalse(myTwoWayLinkedList.contains("Two"));
    }
    //</editor-fold>

    //<editor-fold desc="Method test: get(int index)">
    @Test
    void get_ReturnsCorrectElementAtGivenIndex() {
        MyTwoWayLinkedList myTwoWayLinkedList = new MyTwoWayLinkedList();
        myTwoWayLinkedList.add("First element");
        myTwoWayLinkedList.add("Second element");
        assertThat(myTwoWayLinkedList.get(1), equalTo("Second element"));
    }
    @Test
    void get_ThrowsExceptionIfIndexIsOutOfBoundsOfList() {
        MyTwoWayLinkedList myTwoWayLinkedList = new MyTwoWayLinkedList();
        // Check if correct message is thrown
        IndexOutOfBoundsException thrown =
                assertThrows(IndexOutOfBoundsException.class,
                        () -> myTwoWayLinkedList.get(0),
                        "Expected get(0) to throw, but it didn't");
        assertTrue(thrown.getMessage().contains("Index '0' is out of bounds of list!"));
    }
    //</editor-fold>

    //<editor-fold desc="Method test: indexOf(Object e)">
    @Test
    void indexOf_ReturnsCorrectIndexOfGivenElement() {
        MyTwoWayLinkedList myTwoWayLinkedList = new MyTwoWayLinkedList();
        myTwoWayLinkedList.add("First element");
        myTwoWayLinkedList.add("Second element");
        assertThat(myTwoWayLinkedList.indexOf("Second element"), equalTo(1));
    }
    @Test
    void indexOf_ReturnsErrorValueIfElementIsNotInList() {
        MyTwoWayLinkedList myTwoWayLinkedList = new MyTwoWayLinkedList();
        myTwoWayLinkedList.add("First element");
        myTwoWayLinkedList.add("Second element");
        assertThat(myTwoWayLinkedList.indexOf("Third element"), equalTo(-1));
    }
    //</editor-fold>

    //<editor-fold desc="Method test: lastIndexOf(E e)">
    @Test
    void lastIndexOf_ReturnsCorrectIndexOfElement() {
        MyTwoWayLinkedList myTwoWayLinkedList = new MyTwoWayLinkedList();
        myTwoWayLinkedList.add("Andy");
        myTwoWayLinkedList.add("B-Man");
        myTwoWayLinkedList.add("B-Man");
        assertThat(myTwoWayLinkedList.lastIndexOf("B-Man"), equalTo(2));
    }
    @Test
    void lastIndexOf_ReturnsErrorValueIfElementNotInList() {
        MyTwoWayLinkedList myTwoWayLinkedList = new MyTwoWayLinkedList();
        myTwoWayLinkedList.add("Andy");
        myTwoWayLinkedList.add("Is");
        assertThat(myTwoWayLinkedList.lastIndexOf("Awesome"), equalTo(-1));
    }
    //</editor-fold>

    //<editor-fold desc="Method test: set(int index, E e)">
    @Test
    void set_newElementWasSuccessfullySetInGivenIndex() {
        MyTwoWayLinkedList myTwoWayLinkedList = new MyTwoWayLinkedList();
        myTwoWayLinkedList.add("Andy");
        myTwoWayLinkedList.add("is");
        myTwoWayLinkedList.add("dumb...");
        myTwoWayLinkedList.set(2, "smart!");
        assertThat(myTwoWayLinkedList.get(2), equalTo("smart!"));
    }
    @Test
    void set_returnsPreviousElementOnSuccess() {
        MyTwoWayLinkedList myTwoWayLinkedList = new MyTwoWayLinkedList();
        myTwoWayLinkedList.add("Ferrari");
        assertThat(myTwoWayLinkedList.set(0, "Ferrari2"), equalTo("Ferrari"));
    }
    @Test
    void set_ThrowsExceptionIfIndexIsOutOfBoundsOfList() {
        MyTwoWayLinkedList myTwoWayLinkedList = new MyTwoWayLinkedList();
        // Check if correct message is thrown
        IndexOutOfBoundsException thrown =
                assertThrows(IndexOutOfBoundsException.class,
                        () -> myTwoWayLinkedList.set(5, "testing"),
                        "Expected set(5) to throw, but it didn't");
        assertTrue(thrown.getMessage().contains("Index '5' is out of bounds of list!"));
    }
    //</editor-fold>
}
