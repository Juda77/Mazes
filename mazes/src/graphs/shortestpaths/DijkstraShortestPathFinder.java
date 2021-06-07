package graphs.shortestpaths;
import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.ArrayHeapMinPQ;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        //return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
         return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {

        Set<V> visited = new HashSet<>(); // holds the visited vertices
        Map<V, E> edgeTo = new HashMap<>(); // holds the previous vertex in the shortest path

        // for each and every vertex, track the distance from the start vertex
        Map<V, Double> distTo = new HashMap<>();

        // this tracks which is the minimum neighboring vertex
        ArrayHeapMinPQ<V> unvisited =  (ArrayHeapMinPQ<V>) this.createMinPQ();

        // preparation before the loop
        unvisited.add(start, 0.0);
        distTo.put(start, 0.0);

        // while there are more unvisited vertices
        while (!unvisited.isEmpty()) {

            // move the current vertex from the unvisited set to the visited set
            V currVertex = unvisited.removeMin();
            if (Objects.equals(currVertex, end)) {
                return edgeTo;
            }
            visited.add(currVertex);

            // iterate through each outgoing edge
            for (E edge : graph.outgoingEdgesFrom(currVertex)) {

                // get the current edge's vertex
                V nextVertex = edge.to();

                // if the vertex is one we've already visited, skip it
                // also skips if there's a self-loop
                if (visited.contains(nextVertex)) {
                    continue;
                }

                // get the next vertex's dist from start by adding the
                // current vertex's dist from start to the outgoing edge's dist/weight
                double weight = edge.weight();
                double nextVertexDistFromStart = distTo.get(currVertex) + weight;

                // compare the next vertex's dist from start with whatever the previously
                // recording dist from start was. If the node was never touched yet, the
                // previously recorded dist from start is infinity
                double nextVertexPrevDistFromStart = Double.POSITIVE_INFINITY;
                if (distTo.containsKey(nextVertex)) {
                    nextVertexPrevDistFromStart = distTo.get(nextVertex);
                }

                // choose the shortest distance to the current next vertex
                // also update the edge if needed
                if (nextVertexDistFromStart < nextVertexPrevDistFromStart) {
                    distTo.put(nextVertex, nextVertexDistFromStart);
                    edgeTo.put(nextVertex, edge);
                }

                // add the next vertex to the unvisited list if it hasn't already been visited
                if (!unvisited.contains(nextVertex)) {
                    unvisited.add(nextVertex, distTo.get(nextVertex));
                } else {
                    // if we updated the next vertex's weight
                    unvisited.changePriority(nextVertex, distTo.get(nextVertex));
                }
            }
        }
        return edgeTo;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {

        ShortestPath<V, E> sp = null;

        // case where the start and end vertex are the same
        if (Objects.equals(start, end)) {
            sp = new ShortestPath.SingleVertex<>(start);
            return sp;
        }

        // case where there is no path from start to end
        if (!spt.containsKey(end)) {
            sp = new ShortestPath.Failure<>();
            return sp;
        }

        // case where there is a legit path
        List<E> edgesOfPath = new ArrayList<>();
        V currVertex = end;
        while (!Objects.equals(currVertex, start)) {
            E edgeToCurrVertex = spt.get(currVertex);
            edgesOfPath.add(edgeToCurrVertex);
            currVertex = edgeToCurrVertex.from();
        }

        Collections.reverse(edgesOfPath);
        sp = new ShortestPath.Success<>(edgesOfPath);
        return sp;
    }

}
