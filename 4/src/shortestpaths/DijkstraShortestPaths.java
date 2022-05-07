package shortestpaths;

import edu.princeton.cs.algs4.IndexMinPQ;

import java.util.Arrays;

public class DijkstraShortestPaths implements ShortestPaths {
    private double[] weightOfPathTo;
    private WeightedDiEdge[] inLeastWeightPathEdgeTo;

    private EdgeWeightedDiGraph edgeWeightedDiGraph;

    private IndexMinPQ<Double> weightedDiEdgeIndexMinPQ; // because edge details can be identified using distTo[] and edgeTo[], only comparison of weight is needed

    public DijkstraShortestPaths(EdgeWeightedDiGraph edgeWeightedDiGraph, int s) {
        this.edgeWeightedDiGraph = edgeWeightedDiGraph;
        this.weightOfPathTo = new double[edgeWeightedDiGraph.V()];
        this.inLeastWeightPathEdgeTo = new WeightedDiEdge[edgeWeightedDiGraph.V()];

        computeDijkstraSPTFrom(s);
    }

    private void computeDijkstraSPTFrom(int s) {
        Arrays.fill(this.weightOfPathTo, Double.POSITIVE_INFINITY);

        this.weightedDiEdgeIndexMinPQ = new IndexMinPQ<>(this.edgeWeightedDiGraph.V());

        this.weightOfPathTo[s] = 0;
        this.weightedDiEdgeIndexMinPQ.insert(s, 0.0);
        while (!this.weightedDiEdgeIndexMinPQ.isEmpty()) {
            int leastWeightToVertex = weightedDiEdgeIndexMinPQ.delMin();
            for (WeightedDiEdge edge : this.edgeWeightedDiGraph.adj(leastWeightToVertex)) {
                relax(edge);
            }
        }
    }

    private void relax(WeightedDiEdge edge) {
        if (this.weightOfPathTo[edge.to()] > this.weightOfPathTo[edge.from()] + edge.weight()) {
            this.weightOfPathTo[edge.to()] = this.weightOfPathTo[edge.from()] + edge.weight();
            this.inLeastWeightPathEdgeTo[edge.to()] = edge;

            if (this.weightedDiEdgeIndexMinPQ.contains(edge.to())) {
                this.weightedDiEdgeIndexMinPQ.decreaseKey(edge.to(), this.weightOfPathTo[edge.to()]);
            } else {
                this.weightedDiEdgeIndexMinPQ.insert(edge.to(), this.weightOfPathTo[edge.to()]);
            }
        }
    }

    @Override
    public double weightOfPathTo(int v) {
        return this.weightOfPathTo[v];
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
