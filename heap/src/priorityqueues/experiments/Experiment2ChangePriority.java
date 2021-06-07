package priorityqueues.experiments;

import edu.washington.cse373.experiments.AnalysisUtils;
import edu.washington.cse373.experiments.PlotWindow;
import priorityqueues.ArrayHeapMinPQ;
import priorityqueues.ExtrinsicMinPQ;
import priorityqueues.NaiveMinPQ;

import java.util.List;
import java.util.Random;
import java.util.function.LongUnaryOperator;

public class Experiment2ChangePriority {
    public static final long MAX_MAP_SIZE = 10000;
    public static final long STEP = 100;

    public static void main(String[] args) {
        new Experiment2ChangePriority().run();
    }

    public void run() {
        List<Long> sizes = AnalysisUtils.range(STEP, MAX_MAP_SIZE, STEP);

        PlotWindow.launch("Experiment 2", "PQ Size", "Average Runtime of changePriority (ns)",
                new LongUnaryOperator[]{this::runtime1, this::runtime2},
                new String[]{"runtime1", "runtime2"}, sizes, 1000, .05);
    }

    public long runtime1(long pqSize) {
        ExtrinsicMinPQ<Long> pq = createArrayHeapMinPQ();
        return timeAverageChangePriority(pq, pqSize);
    }

    public long runtime2(long pqSize) {
        ExtrinsicMinPQ<Long> pq = createNaiveMinPQ();
        return timeAverageChangePriority(pq, pqSize);
    }

    protected ExtrinsicMinPQ<Long> createArrayHeapMinPQ() {
        return new ArrayHeapMinPQ<>();
    }

    protected ExtrinsicMinPQ<Long> createNaiveMinPQ() {
        return new NaiveMinPQ<>();
    }

    /**
     * Adds `size` items to the given priority queue, then randomly changes the priority of each.
     * Returns the average runtime (in nanoseconds) of the `changePriority` calls.
     */
    protected static long timeAverageChangePriority(ExtrinsicMinPQ<Long> pq, long size) {
        for (long i = 0; i < size; i += 1) {
            pq.add(i, i);
        }
        Random r = new Random(size);

        long start = System.nanoTime();

        for (long i = 0; i < size; i += 1) {
            pq.changePriority(i, r.nextDouble() * size);
        }

        return (System.nanoTime() - start) / size;
    }
}
