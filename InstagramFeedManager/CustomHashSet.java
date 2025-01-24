import java.util.Iterator;
import java.util.NoSuchElementException;

public class CustomHashSet<T> implements Iterable<T> {
    // Node class used to store individual elements in hash buckets
    private static class Node<T> {
        final T value;
        Node<T> next;

        Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<T>[] table; // Array of nodes representing the hash table
    private int size; // Number of elements in the hash set
    private static final int INITIAL_CAPACITY = 16; // Initial capacity of the hash table

    public CustomHashSet() {
        table = new Node[INITIAL_CAPACITY]; // Initialize table with the initial capacity
        size = 0;
    }

    // Adds an element to the set if it is not already present
    public boolean add(T element) {
        int index = getIndex(element); // Compute the index for this element
        Node<T> current = table[index]; // Get the first node in the bucket at the calculated index
        while (current != null) {
            if (current.value.equals(element)) {
                return false; // Element already exists, do not add
            }
            current = current.next; // Move to the next node in the bucket
        }
        table[index] = new Node<>(element, table[index]); // Insert new node at the beginning of the bucket
        size++; // Increment size of the hash set
        return true;
    }

    // Checks if the set contains the specified element
    public boolean contains(T element) {
        int index = getIndex(element); // Compute the index for this element
        Node<T> current = table[index]; // Get the first node in the bucket at the calculated index
        while (current != null) {
            if (current.value.equals(element)) {
                return true; // Element found
            }
            current = current.next; // Move to the next node in the bucket
        }
        return false; // Element not found
    }

    // Removes the specified element from the set
    public boolean remove(T element) {
        int index = getIndex(element); // Compute the index for this element
        Node<T> current = table[index]; // Get the first node in the bucket at the calculated index
        Node<T> previous = null; // Keep track of the previous node to manage deletions
        while (current != null) {
            if (current.value.equals(element)) {
                if (previous == null) {
                    table[index] = current.next; // Remove first node
                } else {
                    previous.next = current.next; // Remove current node by updating the previous node's next reference
                }
                size--; // Decrement size of the hash set
                return true;
            }
            previous = current;
            current = current.next; // Move to the next node in the bucket
        }
        return false; // Element not found
    }

    // Computes the hash table index for the specified element
    private int getIndex(T element) {
        return element.hashCode() % table.length; // Use modulo operator to fit index within table bounds
    }

    // Provides an iterator for traversing elements in the hash set
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int bucketIndex = -1; // Current index in the hash table
            private Node<T> current = null; // Current node in the traversal

            {
                // Initialize to the first element
                advance();
            }

            // Advances the iterator to the next element
            private void advance() {
                if (current != null && current.next != null) {
                    current = current.next; // Move to the next node in the current bucket
                    return;
                }

                // Move to the next non-empty bucket
                while (++bucketIndex < table.length) {
                    if (table[bucketIndex] != null) {
                        current = table[bucketIndex]; // Set current to the first node in the new bucket
                        return;
                    }
                }
                current = null; // No more elements to iterate over
            }

            // Checks if there is a next element in the set
            @Override
            public boolean hasNext() {
                return current != null;
            }

            // Returns the next element in the set
            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T value = current.value;
                advance(); // Move to the next element
                return value;
            }
        };
    }

    // Checks if the set is empty
    public boolean isEmpty() {
        return size == 0;
    }
}
