package week2;

import edu.princeton.cs.algs4.Bag;

public class WeightedGraph {
    private int numVertex;
    private int numEdge;

    private Bag<Edge>[] incident;

    private Bag<Edge> allEdges;

    @SuppressWarnings("unchecked")
    public WeightedGraph(int numVertex) {
        this.numVertex = numVertex;
        this.numEdge = 0;
        this.incident = (Bag<Edge>[]) new Bag[numVertex];
        for (int v = 0; v < numVertex; v++) {
            this.incident[v] = new Bag<>();
        }
        this.allEdges = new Bag<>();
    }

    public void addEdge(Edge edge) {
        this.numEdge++;
        int v = edge.either();
        int w = edge.other(v);
        this.incident[v].add(edge);
        this.incident[w].add(edge);

        this.allEdges.add(edge);
    }

    public Iterable<Edge> adj(int v) {
        return this.incident[v];
    }

    public Iterable<Edge> edge() {
        return this.allEdges;
    }

    public int getNumVertex() {
        return numVertex;
    }

    public int getNumEdge() {
        return numEdge;
    }
}
