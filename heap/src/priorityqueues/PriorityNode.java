package priorityqueues;

/**
 * A Plain Old Java Object (POJO) used by the priority queues in this package to associate items
 * with their extrinsic priorities.
 */
class PriorityNode<T> {
    private final T item;
    private double priority;

    PriorityNode(T e, double p) {
        this.item = e;
        this.priority = p;
    }

    T getItem() {
        return this.item;
    }

    double getPriority() {
        return this.priority;
    }

    void setPriority(double priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "PriorityNode{" +
            "item=" + item +
            ", priority=" + priority +
            '}';
    }
}
