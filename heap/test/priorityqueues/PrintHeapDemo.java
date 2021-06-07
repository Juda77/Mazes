package priorityqueues;

import java.util.List;

/**
 * Some methods that print nice representations of heaps.
 *
 * Created by hug.
 */
public class PrintHeapDemo {

    /**
     * Prints out a vey basic drawing of the given array of Objects.
     * You're welcome to copy and paste code from this method into your code, along with a citation.
     */
    public static void printSimpleHeapDrawing(List<?> heap, int startIndex) {
        int depth = ((int) (Math.log(heap.size() - startIndex + 1) / Math.log(2)));
        int level = 0;
        int itemsUntilNext = (int) Math.pow(2, level);
        for (int j = 0; j < depth; j++) {
            System.out.print(" ");
        }

        for (int i = 1; i < heap.size(); i++) {
            System.out.printf("%s ", heap.get(i));
            if (i == itemsUntilNext) {
                System.out.println();
                level++;
                itemsUntilNext += Math.pow(2, level);
                depth--;
                for (int j = 0; j < depth; j++) {
                    System.out.print(" ");
                }
            }
        }
        System.out.println();
    }

    /**
     * Prints out a drawing of the given array of Objects assuming.
     * You're welcome to copy and paste code from this method into your, along code with a citation.
     */
    public static void printFancyHeapDrawing(List<?> heap, int startIndex) {
        String drawing = fancyHeapDrawingHelper(heap, startIndex, startIndex, new StringBuilder()).toString();
        System.out.println(drawing);
    }

    private static StringBuilder fancyHeapDrawingHelper(List<?> heap, int startIndex, int index, StringBuilder soFar) {
        if (index >= heap.size() || heap.get(index) == null) {
            return new StringBuilder();
        } else {
            StringBuilder toReturn = new StringBuilder();
            int rightChild = (index - startIndex + 1) * 2 + startIndex;
            StringBuilder nextLevel = new StringBuilder(soFar).append("        ");
            toReturn.append(fancyHeapDrawingHelper(heap, startIndex, rightChild, nextLevel));
            if (rightChild < heap.size() && heap.get(rightChild) != null) {
                toReturn.append(soFar).append("    /");
            }
            toReturn.append("\n").append(soFar).append(heap.get(index)).append("\n");
            int leftChild = (index - startIndex + 1) * 2 + startIndex - 1;
            if (leftChild < heap.size() && heap.get(leftChild) != null) {
                toReturn.append(soFar).append("    \\");
            }
            toReturn.append(fancyHeapDrawingHelper(heap, startIndex, leftChild, nextLevel));
            return toReturn;
        }
    }

    public static void main(String[] args) {
        List<Integer> example = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        printSimpleHeapDrawing(example, 1);
        printFancyHeapDrawing(example, 1);
    }
}
