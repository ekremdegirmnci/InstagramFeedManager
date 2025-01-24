import java.util.ArrayList;
import java.util.List;

public class CustomPriorityQueue {
    private List<Post> heap; // List to store the elements of the heap

    // Constructor initializes the heap as an empty ArrayList
    public CustomPriorityQueue() {
        heap = new ArrayList<>();
    }

    // Adds a new post to the priority queue
    public void add(Post post) {
        heap.add(post); // Add the new post to the end of the list
        int currentIndex = heap.size() - 1; // Start with the last element added
        // Bubble up the added element to maintain heap property
        while (currentIndex > 0) {
            int parentIndex = (currentIndex - 1) / 2; // Find the parent's index
            // Compare the current post with its parent; bubble up if necessary
            if (compare(heap.get(currentIndex), heap.get(parentIndex)) > 0) {
                swap(currentIndex, parentIndex); // Swap if current is greater than parent
                currentIndex = parentIndex; // Move up to the parent
            } else {
                break; // If not greater, stop the loop
            }
        }
    }

    // Removes and returns the highest priority post from the priority queue
    public Post poll() {
        if (heap.isEmpty()) return null; // If the heap is empty, return null
        Post result = heap.get(0); // Get the root of the heap
        Post lastItem = heap.remove(heap.size() - 1); // Remove the last item
        if (!heap.isEmpty()) {
            heap.set(0, lastItem); // Set the last item as the new root
            heapify(0); // Heapify from the root down to maintain heap property
        }
        return result; // Return the original root
    }

    // Helper method to reorganize the heap from a given index down to maintain the heap property
    private void heapify(int index) {
        int largest = index; // Start with the current index as the largest
        int leftIndex = 2 * index + 1; // Calculate left child index
        int rightIndex = 2 * index + 2; // Calculate right child index

        // Compare the current node with its left child
        if (leftIndex < heap.size() && compare(heap.get(leftIndex), heap.get(largest)) > 0) {
            largest = leftIndex;
        }
        // Compare the current (or left child) with the right child
        if (rightIndex < heap.size() && compare(heap.get(rightIndex), heap.get(largest)) > 0) {
            largest = rightIndex;
        }

        // If the largest is not the current index, swap and continue heapifying
        if (largest != index) {
            swap(index, largest);
            heapify(largest);
        }
    }

    // Swaps two elements in the heap
    private void swap(int i, int j) {
        Post temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    // Compares two posts based on like count and post ID
    private int compare(Post p1, Post p2) {
        // Compare based on like count; if equal, compare by post ID
        if (p1.getLikeCount() != p2.getLikeCount()) {
            return p1.getLikeCount() > p2.getLikeCount() ? 1 : -1;
        }
        return p1.getPostId().compareTo(p2.getPostId());
    }

    // Checks if the priority queue is empty
    public boolean isEmpty() {
        return heap.isEmpty();
    }
}
