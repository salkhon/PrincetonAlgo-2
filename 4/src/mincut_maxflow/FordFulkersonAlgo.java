package mincut_maxflow;

import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class FordFulkersonAlgo {
    private FloNetwork flowNetwork;
    private int s;
    private int t;

    private double maxFlow;

    // augmenting path search cache
    private boolean[] marked;
    private FloEdge[] edgeTo;

    public FordFulkersonAlgo(FloNetwork flowNetwork, int s, int t) {
        this.flowNetwork = flowNetwork;
        this.s = s;
        this.t = t;
        this.maxFlow = 0;

        this.marked = new boolean[flowNetwork.V()];
        this.edgeTo = new FloEdge[flowNetwork.V()];

        computeMaxFlowNetwork();

    }

    private void computeMaxFlowNetwork() {
        while (hasAugmentingPath()) {
            double bottleneck = Double.POSITIVE_INFINITY;

            // computing bottleneck
            for (int v = this.t; v != this.s; v = this.edgeTo[v].other(v)) {
                bottleneck = Math.min(bottleneck, this.edgeTo[v].residualCapacityTo(v));
            }

            // augmenting path in bottleneck amount
            for (int v = this.t; v != this.s; v = this.edgeTo[v].other(v)) {
                this.edgeTo[v].addResidualFlowTo(v, bottleneck);
            }

            this.maxFlow += bottleneck;
        }
    }

    private boolean hasAugmentingPath() {
        Arrays.fill(this.marked, false);

        // dfs
        this.marked[this.s] = true;
        dfs(this.s);

        return this.marked[this.t];
    }

    private void dfs(int vertex) {
        for (FloEdge incidentEdge : this.flowNetwork.adj(vertex)) {
            if (this.marked[this.t]) {
                break;
            }

            // considering not only forward, but also backward edges
            int to = incidentEdge.other(vertex);

            // finding augmenting path
            if (incidentEdge.residualCapacityTo(to) > 0 &&
                    !this.marked[to]) {
                this.edgeTo[to] = incidentEdge;
                this.marked[to] = true;
                dfs(to);
            }
        }
    }

    public boolean inCut(int v) {
        return this.marked[v];
    }

    public double getMaxFlow() {
        return this.maxFlow;
    }
}
