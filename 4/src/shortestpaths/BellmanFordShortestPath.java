package shortestpaths;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.EdgeWeightedDirectedCycle;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

public class BellmanFordShortestPath {
    private EdgeWeightedDigraph weightedDigraph;
    private DirectedEdge[] edgeTo;
    private double[] distTo;

    private Queue<Integer> relaxedVertexQ;
    private boolean[] isVertexOnQ;

    private Iterable<DirectedEdge> negativeCycle;
    private int cycleCheckingIntervalCount;

    public BellmanFordShortestPath(EdgeWeightedDigraph weightedDigraph, int source) {
        this.weightedDigraph = weightedDigraph;
        this.edgeTo = new DirectedEdge[weightedDigraph.V()];
        this.distTo = new double[weightedDigraph.V()];
        Arrays.fill(this.distTo, Double.POSITIVE_INFINITY);
        this.relaxedVertexQ = new Queue<>();
        this.isVertexOnQ = new boolean[weightedDigraph.V()];
        this.cycleCheckingIntervalCount = 0;

        computeBellmanFordSP(source);
    }

    private void computeBellmanFordSP(int source) {
        this.distTo[source] = 0.0;
        this.relaxedVertexQ.enqueue(source);
        this.isVertexOnQ[source] = true;
        while (!this.relaxedVertexQ.isEmpty() && !hasNegativeCycle()) {
            int v = relaxedVertexQ.dequeue();
            this.isVertexOnQ[v] = false;
            relax(v);
        }

    }

    private void relax(int v) {
        for (DirectedEdge directedEdge : this.weightedDigraph.adj(v)) {
            int w = directedEdge.to();
            if (this.distTo[w] > this.distTo[v] + directedEdge.weight()) {
                // successfully relaxed
                this.distTo[w] = this.distTo[v] + directedEdge.weight();
                this.edgeTo[w] = directedEdge;

                if (!this.isVertexOnQ[w]) {
                    this.relaxedVertexQ.enqueue(w);
                    this.isVertexOnQ[w] = true;
                }
            }

            // if cycle is found, the algorithm will get stuck in the cycle
            if (this.cycleCheckingIntervalCount++ % this.weightedDigraph.V() == 0) {
                findCycle();
            }
        }
    }

    private void findCycle() {
        // if edgeTo[] records a cycle, the cycle has to a negative cycle
        EdgeWeightedDigraph sptWithNegCycle = new EdgeWeightedDigraph(this.weightedDigraph.V());
        for (int v = 0; v < this.weightedDigraph.V(); v++) {
            if (this.edgeTo[v] != null) {
                sptWithNegCycle.addEdge(this.edgeTo[v]);
            }
        }

        EdgeWeightedDirectedCycle edgeWeightedDirectedCycle = new EdgeWeightedDirectedCycle(sptWithNegCycle);
        this.negativeCycle = edgeWeightedDirectedCycle.cycle();
    }

    public boolean hasNegativeCycle() {
        return this.negativeCycle != null;
    }

    public Iterable<DirectedEdge> getNegativeCycle() {
        return negativeCycle;
    }
}
