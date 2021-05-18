import java.util.ArrayList;
import java.util.LinkedList;

public class BinarySearchTree<E extends Comparable<E>> implements Tree<E> {
  /** Root node for the tree. */
  private TreeNode<E> root;

  /** Size of the tree, number of nodes. */
  private int size = 0;

  /** Create a default binary tree. */
  BinarySearchTree() {
  }

  /**
   * Create a binary tree from an array of objects.
   *
   * @param objects Array of objects to use in creation of tree.
   */
  BinarySearchTree(E[] objects) {
    for (E object : objects) {
      insert(object);
    }
  }

  /////////////////////////////////////////////////////////////////////////////
  //                            Interface Methods                            //
  /////////////////////////////////////////////////////////////////////////////

  public boolean search(E element) {
    TreeNode<E> current = root; // Start from the root

    while (current != null) {
      if (element.equals(current.element)) {
        return true; // Found element searched for
      } else if (element.compareTo(current.element) < 0) {
        current = current.left; // Search element is smaller than current node's element, go left
      } else {
        current = current.right; // Search element is bigger than current node's element, go right
      }
    }

    return false; // Element not found in tree
  }

  public boolean insert(E element) {
    if (root == null) { // First element in Tree.
      root = createNewNode(element);
    } else {
      // Locate the parent node
      TreeNode<E> parent = null;
      TreeNode<E> current = root;

      while (current != null) { // Go through the tree till we reach appropriate leaf node.
        parent = current;

        if (element.equals(current.element)) {
          return false; // Duplicates not inserted
        } else if (element.compareTo(current.element) < 0) {
          current = current.left; // Inserted element is smaller than current node's element, go left
        } else {
          current = current.right; // Inserted element is bigger than current node's element, go right
        }
      }

      // Create the new node and attach it to the parent node
      if (element.compareTo(parent.element) < 0) {
        parent.left = createNewNode(element);
      } else {
        parent.right = createNewNode(element);
      }
    }

    size++;
    return true; // Element inserted
  }

  public boolean delete(E element) {
    // Locate the node to be deleted and also locate its parent node
    TreeNode<E> parent = null;
    TreeNode<E> current = root;

    while (current != null) {
      if (element.equals(current.element)) {
        break; // Found the node
      }

      // Update parent, and go left if deletion element is smaller than current element, right otherwise
      parent = current;
      current = element.compareTo(current.element) < 0 ? current.left: current.right;
    }

    if (current == null) {
      return false; // Element is not in the tree, nothing to remove.
    }

    if (current.left == null) {
      // Case 1: current has no left children
      if (parent == null) { // We delete root, simply replace root with right child
        root = current.right;
      } else { // Connect the parent with the right child
        if (element.compareTo(parent.element) < 0) {
          parent.left = current.right;
        } else {
          parent.right = current.right;
        }
      }
    } else {
      // Case 2: The current node has a left child
      // Locate the rightmost node in the left subtree of
      // the current node and also its parent
      TreeNode<E> parentOfRightMost = current;
      TreeNode<E> rightMost = current.left;

      while (rightMost.right != null) {
        parentOfRightMost = rightMost;
        rightMost = rightMost.right; // Keep going to the right
      }

      // Replace the element in current by the element in rightMost
      current.element = rightMost.element;

      // Eliminate rightmost node
      if (parentOfRightMost.right == rightMost) {
        parentOfRightMost.right = rightMost.left;
      } else { // Special case: parentOfRightMost == current
        parentOfRightMost.left = rightMost.left;
      }
    }

    size--;
    return true; // Element removed
  }

  public void inOrder() {
    traverse(root, Traversal.IN_ORDER);
  }

  public void postOrder() {
    traverse(root, Traversal.POST_ORDER);
  }

  public void preOrder() {
    traverse(root, Traversal.PRE_ORDER);
  }

  public java.util.Iterator<E> iterator() {
    return new InOrderIterator();
  }

  public int getSize() {
    return size;
  }

  /////////////////////////////////////////////////////////////////////////////
  //                           Additional Methods                            //
  /////////////////////////////////////////////////////////////////////////////

  /** Remove all elements from the tree */
  void clear() {
    root = null;
    size = 0;
  }

  /**
   * Returns the root of the tree.
   *
   * @return Root node of the tree.
   */
  private TreeNode<E> getRoot() {
    return root;
  }

  /**
   * Return the height of this binary tree. Height is the number of the nodes in
   * the longest path of the tree.
   *
   * @return Height og tree.
   */
  int height() {
    return height(getRoot());
  }

  /**
   * Method for traversing the tree and displaying its nodes in a Depth First
   * manner.
   */
  void depthFirstTraversal() {
    // First add the root to queue
    // While queue is not empty
    //     Pop last element from queue.
    //     Print the popped element.
    //     Add its right and left children to stack.

    traversalHelper(TraversalType.BREADTH_FIRST);
  }

  /**
   * Method for traversing the tree and displaying its nodes in a Breadth First
   * manner.
   */
  void breadthFirstTraversal() {
    // First add the root to queue
    // While queue is not empty
    //     Pop first element from queue.
    //     Print the popped element.
    //     Add its right and left children to stack.

    traversalHelper(TraversalType.DEPTH_FIRST);
  }

  /////////////////////////////////////////////////////////////////////////////
  //                             Helper Methods                              //
  /////////////////////////////////////////////////////////////////////////////

  /**
   * Helper method to create a TreeNode for a given element.
   *
   * @param element Element to be put into the TreeNode
   *
   * @return A TreeNode with specified element inside it
   */
  private TreeNode<E> createNewNode(E element) {
    return new TreeNode<>(element);
  }

  /**
   * Helper method for general traversal from a subtree.
   *
   * @param node Which node to start traversal from.
   * @param mode Traversal mode, In-oder, Pre-oder or Post-order.
   */
  private void traverse(TreeNode<E> node, Traversal mode) {
    if (node == null) {
      return;
    }

    if (mode == Traversal.PRE_ORDER) {
      System.out.print(node.element + " ");
    }
    traverse(node.left, mode);
    if (mode == Traversal.IN_ORDER) {
      System.out.print(node.element + " ");
    }
    traverse(node.right, mode);
    if (mode == Traversal.POST_ORDER) {
      System.out.print(node.element + " ");
    }
  }

  /**
   * Helper method for traversing the tree in Breadth First and Depth First manner.
   *
   * @param type Defines whether to do Depth First or Breadth First manner.
   */
  private void traversalHelper(TraversalType type) {
    if (getRoot() == null) {
      return;
    }

    LinkedList<TreeNode<E>> queue = new LinkedList<>();
    TreeNode<E> node;
    queue.add(getRoot());

    while (!queue.isEmpty()) {
      node = type == TraversalType.BREADTH_FIRST ? queue.removeFirst() : queue.removeLast();

      System.out.print(node.element + " ");

      if (node.left != null) {
        queue.add(node.left);
      }
      if (node.right != null) {
        queue.add(node.right);
      }
    }
  }

  /**
   * Helper method for the recursive method, hidden as private since it's not
   * meant to be used elsewhere.
   *
   * @param node Which node to count height for.
   *
   * @return Height of tree from given node.
   */
  private int height(TreeNode node) {
    return node == null ? 0 : 1 + Math.max(height(node.left), height(node.right));
  }

  /////////////////////////////////////////////////////////////////////////////
  //                             Private Classes                             //
  /////////////////////////////////////////////////////////////////////////////

  static class TreeNode<T extends Comparable<T>> {
    T element;
    TreeNode<T> left;
    TreeNode<T> right;

    TreeNode(T element) {
      this.element = element;
    }
  }

  enum TraversalType {
    BREADTH_FIRST,
    DEPTH_FIRST
  }

  private class InOrderIterator implements java.util.Iterator<E> {
    // Store the elements in a list
    private ArrayList<E> list = new ArrayList<>();

    private int current = 0; // Point to the current element in list

    InOrderIterator() {
      inOrder(); // Traverse binary tree and store elements in list
    }

    /** In-order traversal from the root */
    private void inOrder() {
      inOrder(getRoot());
    }

    /** In-order traversal from a subtree */
    private void inOrder(TreeNode<E> root) {
      if (root == null) {
        return;
      }
      inOrder(root.left);
      list.add(root.element);
      inOrder(root.right);
    }

    /** Next element for traversing? */
    public boolean hasNext() {
      return current < list.size();
    }

    /** Get the current element and move cursor to the next. */
    public E next() {
      return list.get(current++);
    }

    /** Remove the current element and refresh the list. */
    public void remove() {
      delete(list.get(current)); // Delete the current element
      list.clear(); // Clear the list
      inOrder(); // Rebuild the list
    }
  }

  private enum Traversal {
    IN_ORDER, POST_ORDER, PRE_ORDER
  }
}
