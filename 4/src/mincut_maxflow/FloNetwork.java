package mincut_maxflow;

import edu.princeton.cs.algs4.Bag;

public class FloNetwork {
    private final int v;
    private int e;
    private Bag<FloEdge>[] adj;

    @SuppressWarnings("unchecked")
    public FloNetwork(int v) {
        this.v = v;
        this.e = 0;
        this.adj = (Bag<FloEdge>[]) new Bag[v];
        for (int i = 0; i < v; i++) {
            this.adj[i] = new Bag<>();
        }
    }

    public void addEdge(FloEdge e) {
        this.adj[e.from()].add(e);
        this.adj[e.to()].add(e);
        this.e++;
    }

    public Iterable<FloEdge> adj(int vertex) {
        return this.adj[vertex];
    }

    public Iterable<FloEdge> edges() {
        Bag<FloEdge> edges = new Bag<>();
        for (Bag<FloEdge> bag : this.adj) {
            for (FloEdge edge : bag) {
                edges.add(edge);
            }
        }
        return edges;
    }

    public int V() {
        return this.v;
    }

    public int E() {
        return this.e;
    }
}
