package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers; // array representing the disjoint sets
    Map<T, Integer> mapping; // maps each item to their id
    int id; // the representative that the next item will be mapped to

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {
        pointers = new ArrayList<>();
        mapping = new HashMap<>();
        id = 0;
    }

    @Override
    public void makeSet(T item) {
        mapping.put(item, id);
        pointers.add(-1);
        id++;
    }

    @Override
    public int findSet(T item) {

        // item is not in the disjoint sets
        if (!mapping.containsKey(item)) {
            throw new IllegalArgumentException();
        }

        // iterate pointers until we get to the root of the set
        // Note: also do path compression
        int startIndex = mapping.get(item);
        int headIndex = traversePointersAndCompress(startIndex);
        return headIndex;
    }

    public int traversePointersAndCompress(int currIndex) {
        if (pointers.get(currIndex) < 0) { // found the head of set(heads hold negative values)
            return currIndex;
        }
        int nextIndex = pointers.get(currIndex);
        int headIndex = traversePointersAndCompress(nextIndex);
        pointers.set(currIndex, headIndex);
        return headIndex;
    }

    @Override
    public boolean union(T item1, T item2) {

        // firstly, check if we even have item1 and item2
        if (!mapping.containsKey(item1) || !mapping.containsKey(item2)) {
            throw new IllegalArgumentException();
        }
        int set1HeadIndex = findSet(item1);
        int set2HeadIndex = findSet(item2);
        if (set1HeadIndex == set2HeadIndex) {
            return false;
        }
        int set1Value = pointers.get(set1HeadIndex);
        int set2Value = pointers.get(set2HeadIndex);

        if (Math.abs(set1Value) > Math.abs(set2Value)
            || set1Value == set2Value && set1HeadIndex < set2HeadIndex) {
            // set the new set's weight and make set 2's head point to set 1's head
            int newSetWeight = pointers.get(set2HeadIndex) + pointers.get(set1HeadIndex);
            pointers.set(set2HeadIndex, set1HeadIndex);
            pointers.set(set1HeadIndex, newSetWeight);

        } else if (Math.abs(set1Value) < Math.abs(set2Value)
            || set1Value == set2Value && set1HeadIndex > set2HeadIndex) {
            // set the new set's weight and make set 1's head point to set 2's head
            int newSetWeight = pointers.get(set2HeadIndex) + pointers.get(set1HeadIndex);
            pointers.set(set1HeadIndex, set2HeadIndex);
            pointers.set(set2HeadIndex, newSetWeight);
        }

        return true;
    }
}
