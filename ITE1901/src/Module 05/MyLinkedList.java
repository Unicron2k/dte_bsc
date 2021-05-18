import java.util.Arrays;

public class MyLinkedList<E> implements MyList<E> {
    /** Pointer to head node of the list */
    private Node<E> head;

    /** Pointer to tail node of the list */
    private Node<E> tail;
    private int size = 0; // Number of elements in the list

    /** Create an empty list */
    public MyLinkedList() {
    }

    /** Create a list from an array of objects */
    public MyLinkedList(E[] objects) {
        this.addAll(Arrays.asList(objects));
    }

    /** Return the head element in the list */
    public E getFirst() {
        return isEmpty() ? null : head.element;
    }

    /** Return the last element in the list */
    public E getLast() {
        return isEmpty() ? null : tail.element;
    }

    /** Return the number of elements in this list */
    @Override
    public int size() {
        return size;
    }

    /** Add an element to the beginning of the list */
    public void addFirst(E e) {
        Node<E> newHead = new Node<>(e); // Create a new node
        newHead.next = head; // Link the new node with the head
        head = newHead; // Set head to point to the new node
        size++; // Increase list size

        if (tail == null) { // The new head is the only node in list
            tail = head;
        }
    }

    /** Add an element to the end of the list */
    public void addLast(E e) {
        Node<E> newTail = new Node<>(e); // Create a new node
        if (tail == null) { // The new tail is the only node in list
            head = tail = newTail;
        } else {
            tail.next = newTail; // Now old tail node points to new tail
            tail = tail.next; // Tail now points to the new tail node
        }

        size++; // Increase size
    }

    /**
     * Add a new element at the specified index
     * in this list. The index of the head element is 0
     */
    @Override
    public void add(int index, E e) {
        if (index <= 0) { // Index smaller than 0 means that we set new head
            addFirst(e);
        } else if (index >= size) { // Index larger than size means that we set new tail
            addLast(e);
        } else { // Index is somewhere in the list
            Node<E> current = getNodeAtIndex(--index);
            Node<E> temp = current.next; // Create temporary node to keep next element in list
            current.next = new Node<>(e); // Set current.next to new node
            current.next.next = temp; // New elements next post to element after it in list
            size++; // Increase size
        }
    }

    /**
     * Remove the head node and
     * return the object that is contained in the removed node.
     */
    public E removeFirst() {
        if (isEmpty()) { // No elements in list
            return null;
        } else {
            Node<E> temp = head; // Keep hold of old head
            head = head.next; // Set head to point to next element in list
            size--; // Decrease size
            if (head == null) { // If there were only one element in list then tail needs to point to null
                tail = null;
            }
            return temp.element; // Return deleted element
        }
    }

    /**
     * Remove the last node and
     * return the object that is contained in the removed node.
     */
    public E removeLast() {
        if (isEmpty()) { // No elements in list
            return null;
        } else if (size == 1) { // Only one element so we clear list and return head
            E temp = head.element;
            clear();
            return temp;
        } else { // Bigger list requires more work
            Node<E> current = getNodeAtIndex(size - 2);
            E temp = tail.element; // Keep current tail node element to return it
            tail = current; // Set next to last element as tail
            tail.next = null; // New tail needs to point to null instead of old tail
            size--; // Decrease size
            return temp; // Return old tail
        }
    }

    /**
     * Remove the element at the specified position in this
     * list. Return the element that was removed from the list.
     */
    @Override
    public E remove(int index) {
        checkIndex(index);

        if (index == 0) { // Remove first
            return removeFirst();
        } else if (index == size - 1) { // Remove last
            return removeLast();
        } else { // Remove somewhere in the middle
            Node<E> previous = getNodeAtIndex(--index);
            Node<E> current = previous.next; // Get the node at index
            previous.next = current.next; // Join list together, node before index points to node after index
            size--; // Remove size
            return current.element; // Return node at removed index
        }
    }

    /** Override toString() to return elements in the list */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");

        Node<E> current = head;

        for (int i = 0; i < size && current != null; i++, current = current.next) {
            result.append(current.element);
            result.append(", ");
        }

        result.deleteCharAt(result.length() - 1);
        result.append("]");
        return result.toString();
    }

    /** Clear the list */
    @Override
    public void clear() {
        size = 0;
        head = tail = null;
    }

    /** Return true if this list contains the element e */
    @Override
    public boolean contains(Object e) {
        // Return false if list is empty
        if (size == 0)
            return false;

        // Iterate list to find e in list
        Node<E> current = head;
        for (int i = 0; i < size; i++) {
            if (e.equals(current.element)) // Check for match
                return true;
            current = current.next; // Go to next node
        }
        return false; // false if list !contain e
    }

    /** Return the element at the specified index */
    @Override
    public E get(int index) {
        // Throw exception if index not in list
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(String.format("Index '%s' is out of bounds of list!", index));

        // Iterate list to given index and return its element
        Node<E> current = head;
        for (int i = 0; i < index; i++)
            current = current.next;
        return current.element;
    }

    /**
     * Return the index of the head matching element in
     * this list. Return - 1 if no match.
     */
    @Override
    public int indexOf(Object e) {
        Node<E> current = head;
        for (int i = 0; i < size; i++) {
            if (e.equals(current.element)) // Check for match
                return i;
            current = current.next; // Go to next node
        }
        return -1; // error value -1 if no match
    }

    /**
     * Return the index of the last matching element in
     * this list. Return - 1 if no match.
     */
    @Override
    public int lastIndexOf(E e) {
        int lastIndex = -1;

        // Iterate list to size and return index of last match
        Node<E> current = head;
        for (int i = 0; i < size; i++) {
            if (e.equals(current.element)) // Check for match
                lastIndex = i;
            current = current.next;
        }
        return lastIndex; // -1 or index of the last match
    }

    /**
     * Replace the element at the specified position
     * in this list with the specified element.
     */
    @Override
    public E set(int index, E e) {
        // Throw exception if index not in list
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(String.format("Index '%s' is out of bounds of list!", index));

        // Iterate list to given index and change its element
        Node<E> current = head;
        for (int i = 0; i < index; i++)
            current = current.next;
        E cache = current.element;
        current.element = e;
        return cache; // return the previous element
    }

    /** Get node at specified index */
    private Node<E> getNodeAtIndex(int index) {
        Node<E> current = head;
        for (int i = 0; i < index; i++) { // Go till the node at index
            current = current.next;
        }
        return current;
    }

    /** Check if specified index is valid */
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /** Override iterator() defined in Iterable */
    @Override
    public java.util.Iterator<E> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements java.util.Iterator<E> {
        private Node<E> current = head; // Current index

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public E next() {
            E e = current.element;
            current = current.next;
            return e;
        }

        @Override
        public void remove() {
            System.out.println("Implementation left as an exercise");
        }
    }

    private static class Node<E> {
        E element;
        Node<E> next;

        public Node(E element) {
            this.element = element;
        }
    }
}