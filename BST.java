package lab6;
import java.util.Collections;

public class BST<E> implements Tree<E> {
  protected TreeNode<E> root;
  protected int size = 0;
  protected java.util.Comparator<E> c;

  /** Create a default BST with a natural order comparator */
  public BST() {
    this.c = (e1, e2) -> ((Comparable<E>) e1).compareTo(e2);
  }

  /** Create a BST with a specified comparator */
  public BST(java.util.Comparator<E> c) {
    this.c = c;
  }

  /** Create a binary tree from an array of objects */
  public BST(E[] objects) {
    this.c = (e1, e2) -> ((Comparable<E>) e1).compareTo(e2);
    for (int i = 0; i < objects.length; i++)
      add(objects[i]);
  }

  @Override /** Returns true if the element is in the tree */
  public boolean search(E e) {
    TreeNode<E> current = root; // Start from the root

    while (current != null) {
      if (c.compare(e, current.element) < 0) {
        current = current.left;
      } else if (c.compare(e, current.element) > 0) {
        current = current.right;
      } else // element matches current.element
        return true; // Element is found
    }

    return false;
  }

  @Override /**
             * Insert element e into the binary tree
             * Return true if the element is inserted successfully
             */
  public boolean insert(E e) {
    if (root == null)
      root = createNewNode(e); // Create a new root
    else {
      // Locate the parent node
      TreeNode<E> parent = null;
      TreeNode<E> current = root;
      while (current != null)
        if (c.compare(e, current.element) < 0) {
          parent = current;
          current = current.left;
        } else if (c.compare(e, current.element) > 0) {
          parent = current;
          current = current.right;
        } else
          return false; // Duplicate node not inserted

      // Create the new node and attach it to the parent node
      if (c.compare(e, parent.element) < 0)
        parent.left = createNewNode(e);
      else
        parent.right = createNewNode(e);
    }

    size++;
    return true; // Element inserted successfully
  }

  protected TreeNode<E> createNewNode(E e) {
    return new TreeNode<>(e);
  }

  @Override /** Inorder traversal from the root */
  public void inorder() {
    inorder(root);
  }

  /** Inorder traversal from a subtree */
  protected void inorder(TreeNode<E> root) {
    if (root == null)
      return;
    inorder(root.left);
    System.out.print(root.element + " ");
    inorder(root.right);
  }

  @Override /** Postorder traversal from the root */
  public void postorder() {
    postorder(root);
  }

  /** Postorder traversal from a subtree */
  protected void postorder(TreeNode<E> root) {
    if (root == null)
      return;
    postorder(root.left);
    postorder(root.right);
    System.out.print(root.element + " ");
  }

  @Override /** Preorder traversal from the root */
  public void preorder() {
    preorder(root);
  }

  /** Preorder traversal from a subtree */
  protected void preorder(TreeNode<E> root) {
    if (root == null)
      return;
    System.out.print(root.element + " ");
    preorder(root.left);
    preorder(root.right);
  }

  /**
   * This inner class is static, because it does not access
   * any instance members defined in its outer class
   */
  public static class TreeNode<E> {
    public E element;
    public TreeNode<E> left;
    public TreeNode<E> right;

    public TreeNode(E e) {
      element = e;
    }
  }

  @Override /** Get the number of nodes in the tree */
  public int getSize() {
    return size;
  }

  /** Returns the root of the tree */
  public TreeNode<E> getRoot() {
    return root;
  }

  /** Returns a path from the root leading to the specified element */
  public java.util.ArrayList<TreeNode<E>> path(E e) {
    java.util.ArrayList<TreeNode<E>> list = new java.util.ArrayList<>();
    TreeNode<E> current = root; // Start from the root

    while (current != null) {
      list.add(current); // Add the node to the list
      if (c.compare(e, current.element) < 0) {
        current = current.left;
      } else if (c.compare(e, current.element) > 0) {
        current = current.right;
      } else
        break;
    }

    return list; // Return an array list of nodes
  }

  @Override /**
             * Delete an element from the binary tree.
             * Return true if the element is deleted successfully
             * Return false if the element is not in the tree
             */
  public boolean delete(E e) {
    // Locate the node to be deleted and also locate its parent node
    TreeNode<E> parent = null;
    TreeNode<E> current = root;
    while (current != null) {
      if (c.compare(e, current.element) < 0) {
        parent = current;
        current = current.left;
      } else if (c.compare(e, current.element) > 0) {
        parent = current;
        current = current.right;
      } else
        break; // Element is in the tree pointed at by current
    }

    if (current == null)
      return false; // Element is not in the tree

    // Case 1: current has no left child
    if (current.left == null) {
      // Connect the parent with the right child of the current node
      if (parent == null) {
        root = current.right;
      } else {
        if (c.compare(e, parent.element) < 0)
          parent.left = current.right;
        else
          parent.right = current.right;
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
      if (parentOfRightMost.right == rightMost)
        parentOfRightMost.right = rightMost.left;
      else
        // Special case: parentOfRightMost == current
        parentOfRightMost.left = rightMost.left;
    }

    size--; // Reduce the size of the tree
    return true; // Element deleted successfully
  }

  @Override /** Obtain an iterator. Use inorder. */
  public java.util.Iterator<E> iterator() {
    return new InorderIterator();
  }

  /** Obtain an iterator. Use preorder. */
  public java.util.Iterator<E> preorderiterator() {
    return new PreorderIterator();
  }

  // Inner class InorderIterator
  private class InorderIterator implements java.util.Iterator<E> {
    // Store the elements in a list
    private java.util.ArrayList<E> list = new java.util.ArrayList<>();
    private int current = 0; // Point to the current element in list

    public InorderIterator() {
      inorder(); // Traverse binary tree and store elements in list
    }

    /** Inorder traversal from the root */
    private void inorder() {
      inorder(root);
    }

    /** Inorder traversal from a subtree */
    private void inorder(TreeNode<E> root) {
      if (root == null)
        return;
      inorder(root.left);
      list.add(root.element);
      inorder(root.right);
    }

    @Override /** More elements for traversing? */
    public boolean hasNext() {
      if (current < list.size())
        return true;

      return false;
    }

    @Override /** Get the current element and move to the next */
    public E next() {
      return list.get(current++);
    }

    @Override // Remove the element returned by the last next()
    public void remove() {
      if (current == 0) // next() has not been called yet
        throw new IllegalStateException();

      delete(list.get(--current));
      list.clear(); // Clear the list
      inorder(); // Rebuild the list
    }
  }

  // Inner class PreorderIterator
  private class PreorderIterator implements java.util.Iterator<E> {
    // Store the elements in a list
    private java.util.ArrayList<E> list = new java.util.ArrayList<>();
    private int current = 0; // Point to the current element in list

    public PreorderIterator() {
      preorder(); // Traverse binary tree and store elements in list
    }

    /** Preorder traversal from the root */
    private void preorder() {
      preorder(root);
    }

    /** Preorder traversal from a subtree */
    private void preorder(TreeNode<E> root) {
      if (root == null)
        return;
      list.add(root.element);
      preorder(root.left);
      preorder(root.right);
    }

    @Override /** More elements for traversing? */
    public boolean hasNext() {
      if (current < list.size())
        return true;
      
      return false;
    }

    @Override /** Get the current element and move to the next */
    public E next() {
      return list.get(current++);
    }

    @Override // Remove the element returned by the last next()
    public void remove() {
      if (current == 0) // next() has not been called yet
        throw new IllegalStateException();

      delete(list.get(--current));
      list.clear(); // Clear the list
      preorder(); // Rebuild the list
    }
  }

  @Override /** Remove all elements from the tree */
  public void clear() {
    root = null;
    size = 0;
  }

  public static void main(String[] args) {
    // Create a new tree
    var bst = new BST<Integer>();

    // add 4 elements
    for (int i = 1; i < 5; i++)
      bst.insert(i);
    System.out.print("Initial tree: ");
    bst.inorder();
    
    // add 4 more
    for (int i = 1; i < 5; i++)
      bst.insert(i + 10);
    System.out.print("\nTree after modification: ");
    bst.inorder();

    // remove 3 elements
    for (int i = 1; i < 4; i++)
      bst.remove(i);
    System.out.print("\nTree after removal: ");
    bst.inorder();
    
    // search for element 10
    System.out.print("\nTree contains 10: " + bst.search(10));

    // Creating a new tree that will contain my kean id
    var idbst = new BST<Integer>();

    idbst.insert(0);
    idbst.insert(9);
    idbst.insert(8);
    idbst.insert(6);
    idbst.insert(5);

    // Displaying the tree (inorder traversal)
    System.out.print("\nTree containing my kean id: " );
    idbst.inorder();

    // Deleting the last 3 digits (kean id order)
    idbst.remove(8);
    idbst.remove(5);
    idbst.remove(6);

    // Displaying the tree after removing the elements (inorder traversal)
    System.out.print("\nTree containing my kean id (after removal): " );
    idbst.inorder();

    System.out.println();
    System.out.println("Value of maximum element in the tree is: "+Collections.max(bst)); 
    System.out.println("Value of minimum element in the tree is: "+Collections.min(bst));
    System.out.println("Value 10 is in the tree has a frequency: "+Collections.frequency(bst, 10));
    System.out.println("Value 4 is in the tree has a frequency: "+Collections.frequency(bst, 4));

    BST<Integer> bst2 = new BST<>();
    bst2.insert(6);
    bst2.insert(3);
    bst2.insert(8);
    bst2.insert(4);
    bst2.insert(9);
    bst2.insert(7);

    BST<Integer>.PreorderIterator iterator = bst2.new PreorderIterator();

    // Testing preorder iterator
    System.out.println("\nPreorder iterator:");
    
    while (iterator.hasNext())
      System.out.print(iterator.next() + " ");
    }
}