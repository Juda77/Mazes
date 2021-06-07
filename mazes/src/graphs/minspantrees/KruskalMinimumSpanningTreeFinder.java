package graphs.minspantrees;
import disjointsets.DisjointSets;
import disjointsets.QuickFindDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        return new QuickFindDisjointSets<>();
        /*
        Disable the line above and enable the one below after you've finished implementing
        your `UnionBySizeCompressingDisjointSets`.
         */
        // return new UnionBySizeCompressingDisjointSets<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {

        // sort edges in the graph in ascending weight order
        List<E> edges = new ArrayList<>(graph.allEdges());
        edges.sort(Comparator.comparingDouble(E::weight));

        /*
        for every vertex in vertices
        disjointSets called makeSet with vertex
         */
        DisjointSets<V> disjointSets = createDisjointSets();
        for (V vertex: graph.allVertices()) {
            disjointSets.makeSet(vertex);
        }

        // Difference from pseudocode: return MST and check that MST exists
        Collection<V> vertices = graph.allVertices();
        List<E> edgesForMST = new ArrayList<>();

        boolean noEdges1Vertex = edges.size() == 0 && vertices.size() == 1;
        boolean noEdgesnoVertices = edges.size() == 0 && vertices.size() == 0;
        //weird edge case
        if (noEdges1Vertex || noEdgesnoVertices) {
            return new MinimumSpanningTree.Success<>(edgesForMST);
        }

        int vertexCount = 1;

        for (E edge : edges) {
            V backVertex = edge.from();
            V outVertex = edge.to();
            if (disjointSets.findSet(backVertex) != disjointSets.findSet(outVertex)) {
                vertexCount++;
                edgesForMST.add(edge);
                // union the two disjoint sets
                disjointSets.union(backVertex, outVertex);
            }
            if (vertexCount == vertices.size()) {
                return new MinimumSpanningTree.Success<>(edgesForMST);
            }
        }
        return new MinimumSpanningTree.Failure<>();

    }
}
