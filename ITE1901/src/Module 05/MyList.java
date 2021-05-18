import java.util.Collection;

public interface MyList<E> extends Collection<E> {
    /** Add a new element at the end of this list */
    @Override
    default boolean add(E e) {
        add(size(), e);
        return true;
    }

    /** Add a new element at the specified index in this list */
    void add(int index, E e);

    /** Clear the list */
    void clear();

    /** Return true if this list contains the element */
    boolean contains(Object e);

    /** Return the element from this list at the specified index */
    E get(int index);

    /**
     * Return the index of the first matching element in this list.
     * Return - 1 if no match.
     */
    int indexOf(Object e);

    /**
     * Return the index of the last matching element in this list
     * Return - 1 if no match.
     */
    int lastIndexOf(E e);

    /**
     * Remove the element at the specified position in this list
     * Shift any subsequent elements to the left.
     * Return the element that was removed from the list.
     */
    E remove(int index);

    /**
     * Replace the element at the specified position in this list
     * with the specified element and returns the new set.
     */
    E set(int index, E e);

    /** Return the number of elements in this list */
    int size();

    /** Return true if this list contains no elements */
    @Override
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Remove the first occurrence of the element e
     * from this list. Shift any subsequent elements to the left.
     * Return true if the element is removed.
     */
    @Override
    default boolean remove(Object e) {
        if (indexOf(e) >= 0) {
            remove(indexOf(e));
            return true;
        }

        return false;
    }

    @Override
    default boolean containsAll(Collection<?> c) {
        if (c == null) {
            return false;
        }

        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }

        return true;
    }

    @Override
    default boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            return false;
        }

        for (E object : c) {
            add(object);
        }

        return true;
    }

    @Override
    default boolean removeAll(Collection<?> c) {
        if (c == null) {
            return false;
        }

        boolean removed = false;

        for (Object element : c) {
            if (contains(element)) {
                remove(element);
                removed = true;
            }
        }

        return removed;
    }

    @Override
    default boolean retainAll(Collection<?> c) {
        if (c == null) {
            return false;
        }

        boolean removed = false;

        for (Object element : c) {
            if (!contains(element)) {
                remove(element);
                removed = true;
            }
        }

        return removed;
    }

    @Override
    default Object[] toArray() {
        Object[] objects = new Object[size()];
        return toArray(objects);
    }

    @Override
    default <T> T[] toArray(T[] array) {
        if (array.length != size()) {
            throw new IllegalArgumentException("Array size does not match");
        }

        for (int i = 0; i < size(); i++) {
            array[i] = (T) get(i);
        }

        return array;
    }
}