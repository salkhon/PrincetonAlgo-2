package shortestpaths;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Topological;

import java.util.Arrays;

public class TopologicalShortestPath implements ShortestPaths {
    private double[] distTo;
    private DirectedEdge[] edgeTo;

    private EdgeWeightedDigraph edgeWeightedDigraph;

    public TopologicalShortestPath(EdgeWeightedDigraph edgeWeightedDigraph, int source) {
        this.edgeWeightedDigraph = edgeWeightedDigraph;
        this.distTo = new double[edgeWeightedDigraph.V()];
        this.edgeTo = new DirectedEdge[edgeWeightedDigraph.V()];

        computeTopologicalOrderedShortestPaths(source);
    }

    private void computeTopologicalOrderedShortestPaths(int s) {
        Topological topological = new Topological(this.edgeWeightedDigraph);
        Arrays.fill(this.distTo, Double.POSITIVE_INFINITY);

        this.distTo[s] = 0.0;
        for (int v : topological.order()) {
            for (DirectedEdge incidentEdge : this.edgeWeightedDigraph.adj(v)) {
                relax(incidentEdge);
            }
        }
    }

    private void relax(DirectedEdge incidentEdge) {
        int from = incidentEdge.from(), to = incidentEdge.to();
        if (this.distTo[to] > this.distTo[from] + incidentEdge.weight()) {
            this.distTo[to] = this.distTo[from] + incidentEdge.weight();
            this.edgeTo[to] = incidentEdge;
        }
    }

    @Override
    public double weightOfPathTo(int v) {
        return 0;
    }

    @Override
    public Iterable<WeightedDiEdge> pathTo(int v) {
        return null;
    }

    @Override
    public boolean hasPathTo(int v) {
        return false;
    }
}
