package priorityqueues;

import priorityqueues.ArrayHeapMinPQ;

public class MyMinPQTests {

    public static void main(String[] args) {
        ArrayHeapMinPQ<Integer> testQ = new ArrayHeapMinPQ<>();
        for (int i = 10; i >= 0; i--) {
            testQ.add(i, i);
        }

        testQ.changePriority(1,20);










        testQ.printHeap();

    }

}
