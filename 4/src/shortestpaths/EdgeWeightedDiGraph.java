package shortestpaths;

import edu.princeton.cs.algs4.Bag;

public class EdgeWeightedDiGraph {
    private int V;
    private int E;

    private Bag<WeightedDiEdge>[] adj;

    @SuppressWarnings("unchecked")
    public EdgeWeightedDiGraph(int V) {
        this.V = V;
        this.E = 0;
        this.adj = (Bag<WeightedDiEdge>[]) new Bag[V];
        for (int i = 0; i < V; i++) {
            this.adj[i] = new Bag<>();
        }
    }

    public void addEdge(WeightedDiEdge weightedDiEdge) {
        this.adj[weightedDiEdge.from()].add(weightedDiEdge);
        this.E++;
    }

    public Iterable<WeightedDiEdge> adj(int v) {
        return this.adj[v];
    }

    public int V() {
        return this.V;
    }

    public int E() {
        return this.E;
    }

    public Iterable<WeightedDiEdge> edges() {
        Bag<WeightedDiEdge> edges = new Bag<>();
        for (int i = 0; i < this.V; i++) {
            for (WeightedDiEdge weightedDiEdge : this.adj[i]) {
                edges.add(weightedDiEdge);
            }
        }
        return edges;
    }
}
