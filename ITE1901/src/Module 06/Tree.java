public interface Tree<E extends Comparable<E>> extends Iterable<E> {
  /**
   * Returns true if the element is in the tree.
   *
   * @param element Element to search for.
   *
   * @return True if element is in tree, false otherwise.
   */
  boolean search(E element);

  /**
   * Insert element o into the binary tree Return true if the element is
   * inserted successfully
   *
   * @param element Element to be inserted into the tree.
   *
   * @return True if element was unique, false if element was duplicate.
   */
  boolean insert(E element);

  /**
   * Delete the specified element from the tree Return true if the element is
   * deleted successfully
   */
  boolean delete(E e);

  /** In-order traversal from the root. */
  void inOrder();

  /** Post-order traversal from the root. */
  void postOrder();

  /** Pre-order traversal from the root. */
  void preOrder();

  /**
   * Get the number of nodes in the tree.
   *
   * @return Number of nodes in the tree.
   */
  int getSize();

  /**
   * Checks whether the tree is empty or not.
   *
   * @return True if empty, false otherwise.
   */
  default boolean isEmpty() {
    return getSize() == 0;
  }

  /** Return an iterator to traverse elements in the tree */
  java.util.Iterator<E> iterator();
}
