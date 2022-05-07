package week2;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.UF;

public class KruskalMinSpanningTree implements MST {
    private WeightedGraph weightedGraph;

    private Bag<Edge> mst;

    public KruskalMinSpanningTree(WeightedGraph weightedGraph) {
        this.weightedGraph = weightedGraph;
        this.mst = new Bag<>();
        kruskalMST();
    }

    private void kruskalMST() {
        MinPQ<Edge> edgeMinPQ = new MinPQ<>();
        for (Edge edge : this.weightedGraph.edge()) {
            edgeMinPQ.insert(edge);
        }

        UF unionFind = new UF(weightedGraph.getNumVertex());

        for (Edge edge; !edgeMinPQ.isEmpty();) {
            edge = edgeMinPQ.delMin();
            int v = edge.either();
            int w = edge.other(v);
            if (unionFind.find(v) != unionFind.find(w)) {
                this.mst.add(edge);
                unionFind.union(v, w);
            }
        }
    }

    @Override
    public Iterable<Edge> edges() {
        return this.mst;
    }

    @Override
    public double weight() {
        double weight = 0;
        for (Edge edge : this.mst) {
            weight += edge.getWeight();
        }
        return weight;
    }
}
