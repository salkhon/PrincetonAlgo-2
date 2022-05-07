package shortestpaths;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.IndexMinPQ;

import java.util.Arrays;

public class MultiSourceDijkstra {
    private EdgeWeightedDigraph edgeWeightedDigraph;
    private int[] sources;

    private DirectedEdge[] edgeTo;
    private double[] distTo;

    private IndexMinPQ<Double> vertexDistPQ;

    public MultiSourceDijkstra(EdgeWeightedDigraph edgeWeightedDigraph, int[] sources) {
        this.edgeWeightedDigraph = edgeWeightedDigraph;
        this.sources = sources;
        this.edgeTo = new DirectedEdge[edgeWeightedDigraph.V()];
        this.distTo = new double[edgeWeightedDigraph.V()];
        this.vertexDistPQ = new IndexMinPQ<>(edgeWeightedDigraph.V());

        computeMultiSourceDijkstra();
    }

    private void computeMultiSourceDijkstra() {
        Arrays.fill(this.distTo, Double.POSITIVE_INFINITY);
        for (int source : this.sources) {
            this.distTo[source] = 0.0;
            this.vertexDistPQ.insert(source, 0.0);
        }

        while (!this.vertexDistPQ.isEmpty()) {
            relax(this.vertexDistPQ.delMin());
        }
    }

    private void relax(int closestVertex) {
        for (DirectedEdge incident : this.edgeWeightedDigraph.adj(closestVertex)) {
            int v = incident.from(), w = incident.to();
            if (this.distTo[w] > this.distTo[v] + incident.weight()) {
                this.distTo[w] = this.distTo[v] + incident.weight();
                this.edgeTo[w] = incident;

                if (this.vertexDistPQ.contains(w)) {
                    this.vertexDistPQ.changeKey(w, this.distTo[w]);
                } else {
                    this.vertexDistPQ.insert(w, this.distTo[w]);
                }
            }
        }
    }
}
