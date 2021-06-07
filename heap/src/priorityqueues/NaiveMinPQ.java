package priorityqueues;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * A very basic implementation of the ExtrinsicMinPQ. Operations have very poor performance, but
 * the implementation is correct with one exception: The add method should throw an exception if
 * the item already exists, but doing so makes the add method so slow that the class becomes
 * difficult to use for testing.
 */
public class NaiveMinPQ<T> implements ExtrinsicMinPQ<T> {

    private List<PriorityNode<T>> items;

    public NaiveMinPQ() {
        items = new ArrayList<>();
    }

    // This method does not throw the proper exception.
    @Override
    public void add(T item, double priority) {
        this.items.add(new PriorityNode<>(item, priority));
    }

    @Override
    public boolean contains(T item) {
        return findNode(item).isPresent();
    }

    @Override
    public T peekMin() {
        if (size() == 0) {
            throw new NoSuchElementException("PQ is empty");
        }
        int minIndex = findIndexOfMin();
        return this.items.get(minIndex).getItem();
    }

    @Override
    public T removeMin() {
        if (size() == 0) {
            throw new NoSuchElementException("PQ is empty");
        }
        int minIndex = findIndexOfMin();
        return this.items.remove(minIndex).getItem();
    }

    @Override
    public void changePriority(T item, double priority) {
        findNode(item)
            .orElseThrow(() -> new NoSuchElementException("PQ does not contain " + item))
            .setPriority(priority);
    }

    @Override
    public int size() {
        return items.size();
    }

    private Optional<PriorityNode<T>> findNode(T item) {
        return this.items.stream()
            .filter(node -> node.getItem().equals(item))
            .findFirst();
    }

    private int findIndexOfMin() {
        // iterate through each index to find the one with the min-priority item
        return IntStream.range(0, this.items.size()).boxed()
            .min(Comparator.comparingDouble(i -> this.items.get(i).getPriority()))
            .orElseThrow();
    }

}
