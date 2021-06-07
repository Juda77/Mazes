package priorityqueues;

import org.assertj.core.api.AbstractObjectAssert;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * An assert class for your array heap that provides an assert method to check the heap invariant.
 */
public abstract class AbstractHeapMinPQAssert<T>
        extends AbstractObjectAssert<AbstractHeapMinPQAssert<T>, ExtrinsicMinPQ<T>> {

    public AbstractHeapMinPQAssert(ExtrinsicMinPQ<T> actual, Class<?> selfType) {
        super(actual, selfType);
    }

    protected abstract int extractStartIndex(ExtrinsicMinPQ<T> actual);
    protected abstract List<PriorityNode<T>> extractHeap(ExtrinsicMinPQ<T> actual);

    public AbstractHeapMinPQAssert<T> isValid() {
        String message = getErrorMessageIfInvalid();
        if (message != null) {
            this.as("invariant check")
                .failWithMessage(message);
        }
        return this;
    }

    protected String getErrorMessageIfInvalid() {
        List<PriorityNode<T>> heap = extractHeap(this.actual);
        int startIndex = extractStartIndex(this.actual);
        return getErrorMessageIfInvalid(heap, startIndex, startIndex + this.actual.size());
    }

    static <T> String getErrorMessageIfInvalid(List<PriorityNode<T>> heap, int startIndex, int endIndex) {
        if (heap.size() < endIndex) {
            return String.format("Heap's internal ArrayList size (%d) is too small for heap's contents? " +
                    "(Expected at least %d.)",
                heap.size(), endIndex);
        }

        Map<Integer, String> brokenIndices = new TreeMap<>();
        for (int i = startIndex; i < endIndex; i++) {
            String message = checkIndex(heap, i, startIndex, endIndex);
            if (message != null) {
                brokenIndices.put(i, message);
            }
        }
        if (brokenIndices.isEmpty()) {
            return null;
        }
        StringBuilder b = new StringBuilder();
        String nl = System.lineSeparator();
        b.append("Heap invariants broken at:").append(nl);
        for (Map.Entry<Integer, String> entry : brokenIndices.entrySet()) {
            b.append("  - index ").append(entry.getKey())
                .append(" (").append(entry.getValue()).append(")").append(nl);
        }
        return b.toString().stripTrailing();
    }

    private static final int NUM_CHILDREN = 2;
    private static <T> String checkIndex(List<PriorityNode<T>> heap, int parentIndex, int startIndex, int endIndex) {
        PriorityNode<T> parentNode = heap.get(parentIndex);
        int firstChildIndex = (parentIndex - startIndex + 1) * NUM_CHILDREN + startIndex - 1;
        int lastChildIndex = Math.min(NUM_CHILDREN + firstChildIndex, endIndex);
        if (parentNode == null ||
            childrenOfIndexAreInvalid(heap, parentNode, firstChildIndex, lastChildIndex)) {

            List<PriorityNode<T>> children = (firstChildIndex < endIndex)
                ? heap.subList(firstChildIndex, lastChildIndex)
                : List.of();
            return "is " + parentNode + "; has children: " + children.toString();
        }

        return null;
    }

    private static <T> boolean childrenOfIndexAreInvalid(
        List<PriorityNode<T>> heap,
        PriorityNode<T> parentNode,
        int firstChildIndex,
        int lastChildIndex) {
        for (int childIndex = firstChildIndex; childIndex < lastChildIndex; childIndex++) {
            PriorityNode<T> childNode = heap.get(childIndex);

            if (childNode == null || parentNode.getPriority() > childNode.getPriority()) {
                return true;
            }
        }
        return false;
    }
}
