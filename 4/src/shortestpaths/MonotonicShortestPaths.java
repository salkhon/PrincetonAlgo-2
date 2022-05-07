package shortestpaths;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.MinPQ;

import java.util.Comparator;

public class MonotonicShortestPaths {
    private EdgeWeightedDigraph edgeWeightedDigraph;

    private DirectedEdge[] edgeToAsc;
    private double[] distToAsc;

    private DirectedEdge[] edgeToDesc;
    private double[] distToDesc;

    private IndexMinPQ<Double> vertexDistPQ;

    public MonotonicShortestPaths(EdgeWeightedDigraph edgeWeightedDigraph, int source) {
        this.edgeWeightedDigraph = edgeWeightedDigraph;
        this.edgeToAsc = new DirectedEdge[edgeWeightedDigraph.V()];
        this.distToAsc = new double[edgeWeightedDigraph.V()];
        this.edgeToDesc = new DirectedEdge[edgeWeightedDigraph.V()];
        this.distToDesc = new double[edgeWeightedDigraph.V()];
        computeMonotonicSP(source);
    }

    private void computeMonotonicSP(int source) {
        MinPQ<DirectedEdge> ascendingEdgesPQ = new MinPQ<>(Comparator.comparingDouble(DirectedEdge::weight));
        for (DirectedEdge directedEdge : this.edgeWeightedDigraph.edges()) {
            ascendingEdgesPQ.insert(directedEdge);
        }

        while (!ascendingEdgesPQ.isEmpty()) {
            relax(ascendingEdgesPQ.delMin());
        }


    }

    private void relax(DirectedEdge delMin) {

    }
}
