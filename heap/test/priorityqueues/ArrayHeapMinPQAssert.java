package priorityqueues;

import java.util.List;

/**
 * An implementation of AbstractHeapMinPQAssert that extracts the proper fields from an
 * ArrayHeapMinPQ implementation.
 */
public class ArrayHeapMinPQAssert<T> extends AbstractHeapMinPQAssert<T> {

    public ArrayHeapMinPQAssert(ArrayHeapMinPQ<T> actual) {
        super(actual, ArrayHeapMinPQAssert.class);
    }

    @Override
    protected int extractStartIndex(ExtrinsicMinPQ<T> actual) {
        return ArrayHeapMinPQ.START_INDEX;
    }

    @Override
    protected List<PriorityNode<T>> extractHeap(ExtrinsicMinPQ<T> actual) {
        return ((ArrayHeapMinPQ<T>) actual).items;
    }
}
