package week2;

import edu.princeton.cs.algs4.MinPQ;

public class PrimMSTEager {
    private WeightedGraph weightedGraph;

    public PrimMSTEager(WeightedGraph weightedGraph) {
        this.weightedGraph = weightedGraph;
        primEager();
    }

    private void primEager() {
        MinPQ<Integer> vertexPQ = new MinPQ<>();
        Edge[] shortestEdgeToVertexFromTree = new Edge[this.weightedGraph.getNumVertex()];
        boolean[] inTree = new boolean[this.weightedGraph.getNumVertex()];


    }
}
