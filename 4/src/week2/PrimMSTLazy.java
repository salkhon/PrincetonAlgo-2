package week2;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.UF;

public class PrimMSTLazy implements MST {
    private WeightedGraph weightedGraph;

    private Queue<Edge> mst;

    // processing cache
    private MinPQ<Edge> edgeMinPQ;
    private boolean[] isInTree;

    public PrimMSTLazy(WeightedGraph weightedGraph) {
        this.weightedGraph = weightedGraph;
        this.mst = new Queue<>();

        this.edgeMinPQ = new MinPQ<>();
        this.isInTree = new boolean[weightedGraph.getNumVertex()];

        primMSTLazy();
    }

    private void primMSTLazy() {
        includeVertexAndNonTreeNeighbors(0);

        while (!edgeMinPQ.isEmpty() && this.mst.size() != this.weightedGraph.getNumVertex() - 1) {
            Edge edge = edgeMinPQ.delMin();
            int v = edge.either(), w = edge.other(v);
            if (!isInTree[v] || !isInTree[w]) {
                this.mst.enqueue(edge);
                int treeVertex = isInTree[v] ? w : v;
                includeVertexAndNonTreeNeighbors(treeVertex);
            }
        }
    }

    private void includeVertexAndNonTreeNeighbors(int v) {
        this.isInTree[v] = true;
        for (Edge edge : this.weightedGraph.adj(v)) {
            if (!this.isInTree[edge.other(v)]) {
                this.edgeMinPQ.insert(edge);
            }
        }
    }

    @Override
    public Iterable<Edge> edges() {
        return this.mst;
    }

    @Override
    public double weight() {
        double weight = 0;
        for (Edge edge : this.mst) {
            weight += edge.getWeight();
        }

        return weight;
    }
}
