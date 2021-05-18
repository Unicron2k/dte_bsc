import java.util.AbstractSequentialList;
import java.util.Arrays;
import java.util.ListIterator;

public class MyTwoWayLinkedList<E> extends AbstractSequentialList<E> implements MyList<E> {
    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    public MyTwoWayLinkedList() {
    }

    /**
     * Generates TwoWayLinkedList list with objects in array E[]
     * @param objects Objects to put in new list
     */
    public MyTwoWayLinkedList(E[] objects) {
        this.addAll(Arrays.asList(objects));
    }

    /**
     * @return First element in list || null if list is empty
     */
    public E getFirst() {
        return isEmpty() ? null : head.element;
    }

    /**
     * @return Last element in list || null if list is empty
     */
    public E getLast() {
        return isEmpty() ? null : tail.element;
    }

    /**
     * @return Size of the list
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Add an element at beginning of list
     * @param e The element to add in list
     */
    public void addFirst(E e) {
        Node<E> newHead = new Node<>(e); // Create a new node
        if(head!=null) {
            newHead.next = head; // Link the new node with the head
            head.previous = newHead; //Link old node to new node
            head = newHead; // Set head to point to the new node
        }
        else { head = newHead; }

        size++; // Increase list size

        if (tail == null) { // The new head is the only node in list
            tail = head;
        }
    }

    /**
     * Add an element at end of list
     * @param e The element to add in list
     */
    public void addLast(E e) {
        Node<E> newTail = new Node<>(e); // Create a new node

        if(tail!=null){
            tail.next = newTail; // Set tail to reference new tail
            newTail.previous = tail; //set newTail.previous to reference old tail
            tail = newTail; // Update the reference to tail with the newTail
        } else { tail = newTail; }

        size++; // Increase list size

        if (head == null) { // The new tail is the only node in list
            head = tail;
        }
    }

    /**
     * Add an element to list at given index
     * @param index Index in list to add element to
     * @param e The element to add in list
     */
    @Override
    public void add(int index, E e) {
        if (index <= 0) { // Index smaller than 0 means that we set new head
            addFirst(e);
        } else if (index >= size) { // Index larger than size means that we set new tail
            addLast(e);
        } else { // Index is somewhere in the list

            Node<E> newNode = new Node<>(e); // Create new node with element E
            Node<E> currentNode = getNodeAtIndex(index);

            // unless something very wrong has happened, this should never produce a NullPointerException,
            // as we should always be located in [head+1,tail-1]
            // noinspection ConstantConditions
            Node<E> previousNode = currentNode.previous;

            newNode.previous=previousNode; // Set newNode.previous to point to the new previous node
            newNode.next=currentNode; // set newNode.next to point to the new next node.
            previousNode.next=newNode; // Set previous.next to point to newNode
            currentNode.previous=newNode; // Set currentNode.previous to point to newNode

            size++; // Increase size
        }
    }

    /**
     * Remove the head node and return removed object in removed noode
     * @return Object contained in removed node
     */
    public E removeFirst() {
        if (size == 0) { // No elements in list
            return null;
        } else {
            E temp = head.element; // Keep hold of old element in head
            head = head.next; // Set head to point to next element in list
            head.previous = null; // Ensure head.previous is null
            size--; // Decrease size
            return temp; // Return deleted element
        }
    }

    /**
     * Remove last element in list
     * @return Object contained in removed node
     */
    public E removeLast() {
        if (size == 0)
            return null;
        else if (size == 1) {
            E temp = head.element;
            clear();
            return temp;
        } else {
            E temp = tail.element; // Keep newTail.element tail node element to return it
            tail = tail.previous; // Set tail to previous node
            tail.next = null; // Ensure tail.next is null
            size--; // Decrease size
            return temp; // Return old tail
        }
    }

    /**
     * Remove element at specified index in list
     * May throw IndexOutOfBoundsException
     * @param index Index of element to remove from list
     * @return The element that was removed or null
     */
    @Override
    public E remove(int index) {
        if(checkIndex(index)) {
            if (index == 0)
                return removeFirst();
            else if (index == size - 1)
                return removeLast();
            else {

                Node<E> current = getNodeAtIndex(index);

                // unless something very wrong has happened, this should never produce a NullPointerException,
                // as we should always be located in [head+1,tail-1]
                // noinspection ConstantConditions
                Node<E> next = current.next;
                Node<E> previous = current.previous;

                previous.next = next;
                next.previous = previous;

                size--;
                return current.element;
            }
        }
        else return null;
    }

    /**
     * Override toString() to return elements in the list
     * @return String of elements in the list
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");

        Node<E> current = head;

        for (int i = 0; i < size && current != null; i++, current = current.next) {
            result.append(current.element);
            result.append(", ");
        }

        result.delete(result.length() - 2, result.length() - 1);
        result.append("]");
        return result.toString();
    }

    /**
     * Clear the list
     */
    @Override
    public void clear() {
        size = 0;
        head = tail = null;
    }

    /**
     * Return true if list contains element e
     * @param e Object to look for in list
     * @return boolean
     */
    public boolean contains(Object e) {
        return indexOf(e)!=-1;
    }

    /**
     * Returns the element at given index
     * May throw IndexOutOfBoundsException
     * @param index Index for element to return
     * @return <E>
     */
    @Override
    public E get(int index) {
        Node<E> current = getNodeAtIndex(index);
        return (current!=null?current.element:null);
    }

    /**
     * Check index of specified Object in list
     * @param e Object to look for in list
     * @return Index of match or error value -1 if not found
     */
    @Override
    public int indexOf(Object e) {
        // Return -1 if list is empty
        if (size == 0)
            return -1;

        // Iterate list and return any index of match
        Node<E> current = head;
        for (int i = 0; i < size; i++) {
            if (e.equals(current.element)) // Check for match
                return i;
            current = current.next;
        }

        // Return -1 if no match
        return -1;
    }

    /**
     * Check last index of specified Object in list
     * @param e Object to look for in list
     * @return Last index of match or error value -1 if not found
     */
    @Override
    public int lastIndexOf(Object e) {
        // Return -1 if list is empty
        if (size == 0)
            return -1;

        // Iterate list backwards and return any index of match
        int lastIndex = -1;
        Node<E> current = tail;
        for (int i = size; i > 0; i--) {
            if (e.equals(current.element)) // Check for match
                lastIndex = i;
            current = current.previous;
        }

        // Return -1 if no match
        return lastIndex;
    }

    /**
     * Set/replace element in list at given index.
     * May throw IndexOutOfBoundsException
     * @param index Index to place element in list
     * @param e Element to add in list
     * @return Element that was removed or null
     */
    public E set(int index, E e) {
        // Fetch node currently at index
        Node<E> current = getNodeAtIndex(index);
        if(current != null) {
            E cache = current.element;
            current.element = e;
            return cache; // Return the previous element
        }
        else return null;
    }

    /**
     * Get node at specified index
     * May throw IndexOutOfBoundsException
     * @param index Index of node to return in list
     * @return Node at given index in list or null
     */
    private Node<E> getNodeAtIndex(int index) {
        if (checkIndex(index)) {
            //TODO Optimize code by deciding to traverse from head or tail, ~halves traversal-time
            Node<E> current = head; // Start traversal from current head
            for (int i = 0; i < index; i++)
                current = current.next;
            return current;
        }
        else return null;
    }

    /**
     * Check if specified index is valid
     * @param index Index to check in list
     * @return True if valid, throws IndexOutOfBoundsException if fails
     */
    private boolean checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(String.format("Index '%s' is out of bounds of list!", index));
        }
        else return true;
    }

    /**
     * Creates a new listIterator of type TwoWayListIterator,
     * TwoWayListIterator extends listIterator.
     * @return TwoWayListIterator
     */
    @Override
    public ListIterator<E> listIterator() {
        return new TwoWayListIterator();
    }

    /**
     * Creates a new listIterator of type TwoWayListIterator,
     * located the specified index
     * TwoWayListIterator extends listIterator.
     * @param index Index which the list will initially point to
     * @return TwoWayListIterator
     */
    @Override
    public ListIterator<E> listIterator(int index) {
        return new TwoWayListIterator(index);
    }

    //TODO Add javadoc-comments for TwoWayListIterator
    private class TwoWayListIterator implements ListIterator<E> {
        private Node<E> current = head;
        private int index = 0;

        public TwoWayListIterator() {
        }

        public TwoWayListIterator(int index) {
            this.index = index;
            for (int i = 0; i < index; i++)
                current = current.next;
        }

        @Override
        public boolean hasNext() {
            return (current.next != null);
        }

        @Override
        public E next() {
            if (hasNext()) {
                E e = current.element;
                current = current.next;
                index++;
                return e;
            }
            else return null;
        }

        @Override
        public boolean hasPrevious() {
            return (current.previous != null);
        }

        @Override
        public E previous() {
            if (hasPrevious()) {
                E e = current.element;
                current = current.previous;
                index--;
                return e;
            }
            else return null;
        }

        @Override
        public int nextIndex() {
            if (hasNext()) {
                return index+1;
            }
            return -1;
        }

        @Override
        public int previousIndex() {
            if (hasPrevious())
                return index-1;
            return -1;
        }



        //Not necessary

        //FIXME TwoWayListIterator::remove() NOT IMPLEMENTED
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        //FIXME TwoWayListIterator::set() NOT IMPLEMENTED
        @Override
        public void set(E e) {
            throw new UnsupportedOperationException();
        }

        //FIXME TwoWayListIterator::add() NOT IMPLEMENTED
        @Override
        public void add(E e) {
            throw new UnsupportedOperationException();
        }
    }

    private static class Node<E> {
        E element;
        Node<E> next;
        Node<E> previous;

        public Node(){}

        public Node (E element) {
            this.element = element;
        }
    }
}