package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {

    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 0;
    List<PriorityNode<T>> items;

    private int insertIndex = 0; //index where the next item will be inserted
    private int size = 0;

    //to optimize contains(), use a hash set to track what items are in the heap
    Set<T> itemSet = new HashSet<>();

    //to optimize changePriority() we need a hash map to map
    //items with their indices in the heap
    Map<T, Integer> itemIndexMap = new HashMap<>();

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> temp = items.get(a);
        items.set(a, items.get(b));
        itemIndexMap.put(items.get(b).getItem(), a);
        items.set(b, temp);
        itemIndexMap.put(temp.getItem(), b);
    }

    @Override
    public void add(T item, double priority) {

        /*if we have no space left, we have to resize, but ArrayLists resize for us,
         *so we don't have to make our own resize method
         */

        //start by checking if the heap already contains the item
        if (contains(item)) {
            throw new IllegalArgumentException();
        }

        //add the item to the last slot in the array as well as the hash set
        PriorityNode<T> newItem = new PriorityNode<>(item, priority);
        items.add(newItem);
        itemSet.add(item);
        size++;

        //percolate/heapify up as needed. Heapify up will also add the item to the item-index hash map
        heapifyUp(size - 1);

    }

    public void heapifyUp(int currIndex) {

        int parentIndex = (currIndex - 1) / 2;

        boolean parentIsBiggerThanChild = items.get(parentIndex).getPriority() > items.get(currIndex).getPriority();
        while (parentIsBiggerThanChild) {

            swap(parentIndex, currIndex);
            currIndex = parentIndex;
            parentIndex = (currIndex - 1) / 2;
            if (parentIndex < 0) {
                break;
            }

            parentIsBiggerThanChild = items.get(parentIndex).getPriority() > items.get(currIndex).getPriority();
        }

        //add the item to the hash map
        itemIndexMap.put(items.get(currIndex).getItem(), currIndex);
    }

    @Override
    public boolean contains(T item) {
        return itemSet.contains(item);
    }

    @Override
    public T peekMin() {

        //if there are no items, throw an exception
        if (size == 0) {
            throw new NoSuchElementException();
        }

        //return the very first item
        return items.get(0).getItem();
    }

    @Override
    public T removeMin() {

        //if there are no elements left, throw an exception
        if (size == 0) {
            throw new NoSuchElementException();
        }

        //get the top element and remove it from the hash tables
        PriorityNode<T> min = items.get(0);
        itemSet.remove(min.getItem());
        itemIndexMap.remove(min.getItem());


        //if there are no elements left, just move on. Else, do heapify
        if (size == 1) {
            items.remove(size - 1);
            size--;
            return min.getItem();
        }

        //get the last element and move it to the top. Also remove the last element from the end
        PriorityNode<T> lastElement = items.get(size - 1);
        items.set(0, lastElement);
        items.remove(size - 1);
        size--;

        //percolate/heapify down as needed. Heapfify down will also add the item to the item-index hash map
        heapifyDown(0);


        //return the min item
        return min.getItem();

    }

    public void heapifyDown(int currentIndex) {

        PriorityNode<T> lastElement = items.get(currentIndex);
        //percolate/heapify down
        boolean childIsSmallerThanCurrent;
        double leftPriority = Integer.MAX_VALUE;
        double rightPriority = Integer.MAX_VALUE;
        int leftIndex = currentIndex * 2 + 1;
        int rightIndex = currentIndex * 2 + 2;
        if (leftIndex < this.size()) {
            leftPriority = items.get(leftIndex).getPriority();
        }
        if (rightIndex < this.size()) {
            rightPriority = items.get(rightIndex).getPriority();
        }
        childIsSmallerThanCurrent = lastElement.getPriority() > leftPriority
            || lastElement.getPriority() > rightPriority;

        while (childIsSmallerThanCurrent) {
            //determine which child is smaller
            if (leftPriority <= rightPriority) {
                swap(currentIndex, leftIndex);
                currentIndex = leftIndex;
            } else if (rightPriority < leftPriority) {
                swap(currentIndex, rightIndex);
                currentIndex = rightIndex;
            }

            leftIndex = 2 * currentIndex + 1;
            rightIndex = 2 * currentIndex + 2;
            leftPriority = Integer.MAX_VALUE;
            rightPriority = Integer.MAX_VALUE;
            if (leftIndex < this.size()) {
                leftPriority = items.get(leftIndex).getPriority();
            }
            if (rightIndex < this.size()) {
                rightPriority = items.get(rightIndex).getPriority();
            }
            childIsSmallerThanCurrent = lastElement.getPriority() > leftPriority
                || lastElement.getPriority() > rightPriority;

        }

        itemIndexMap.put(lastElement.getItem(), currentIndex);
    }

    @Override
    public void changePriority(T item, double priority) {

        //if the heap doesn't contain the item we want, just return
        if (!itemSet.contains(item)) {
            throw new NoSuchElementException();
        }

        //access the item's index that we want using the hash map
        //then percolate/heapify up or down as needed
        int currIndex = itemIndexMap.get(item);

        //set the new priority
        items.get(currIndex).setPriority(priority);

        heapifyUp(currIndex);
        heapifyDown(itemIndexMap.get(item));
        //print("itemIndexMap: " + itemIndexMap.toString());

    }

    @Override
    public int size() {
        return size;
    }

    public void printHeap() {
        print(items.toString());
    }

    private void print(String output) {
        System.out.println(output);
    }
}
