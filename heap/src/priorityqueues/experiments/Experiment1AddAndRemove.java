package priorityqueues.experiments;

import edu.washington.cse373.experiments.AnalysisUtils;
import edu.washington.cse373.experiments.PlotWindow;
import priorityqueues.ArrayHeapMinPQ;
import priorityqueues.ExtrinsicMinPQ;
import priorityqueues.NaiveMinPQ;

import java.util.List;
import java.util.function.LongUnaryOperator;

public class Experiment1AddAndRemove {
    /*
    Note: these constants are good for demonstrating the difference between the heap and naive
    implementations, but not for visualizing the growth of the heap's runtimes.

    Logarithmic growth is very very slow, so you may need to increase both constants by 2-3 orders
    of magnitude to see a proper trend.

    (If you try doing that, we recommend disabling the naive version, e.g., by making its runtime
    method simply return 0.)
    */
    public static final long MAX_MAP_SIZE = 10000;
    public static final long STEP = 100;

    public static void main(String[] args) {
        new Experiment1AddAndRemove().run();
    }

    public void run() {
        List<Long> sizes = AnalysisUtils.range(STEP, MAX_MAP_SIZE, STEP);

        PlotWindow.launch("Experiment 1", "PQ Size", "Average Runtime of add/removeMin (ms)",
                new LongUnaryOperator[]{this::runtime1, this::runtime2},
                new String[]{"runtime1", "runtime2"}, sizes, 1000, .05);
    }

    public long runtime1(long pqSize) {
        ExtrinsicMinPQ<Long> pq = createArrayHeapMinPQ();
        return timeAverageAddAndRemove(pq, pqSize);
    }

    public long runtime2(long pqSize) {
        ExtrinsicMinPQ<Long> pq = createNaiveMinPQ();
        return timeAverageAddAndRemove(pq, pqSize);
    }

    protected ExtrinsicMinPQ<Long> createArrayHeapMinPQ() {
        return new ArrayHeapMinPQ<>();
    }

    protected ExtrinsicMinPQ<Long> createNaiveMinPQ() {
        return new NaiveMinPQ<>();
    }

    /**
     * Adds and removes `size` items to the given priority queue.
     * Returns the sum of the average runtimes (in nanoseconds) of the `add` and `removeMin` calls.
     */
    protected static long timeAverageAddAndRemove(ExtrinsicMinPQ<Long> pq, long size) {
        long start = System.nanoTime();

        for (long i = 0; i < size; i += 1) {
            pq.add(i, i);
        }
        for (long i = 0; i < size; i += 1) {
            pq.removeMin();
        }

        return (System.nanoTime() - start) / size;
    }
}
