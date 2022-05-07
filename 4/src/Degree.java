import edu.princeton.cs.algs4.Bag;

public class Degree {
    private Digraph digraph;

    private int[] inDegree;
    private int[] outDegree;

    private Bag<Integer> sources;
    private Bag<Integer> sinks;

    public Degree(Digraph digraph) {
        this.digraph = digraph;
        this.inDegree = new int[digraph.getNumVertex()];
        this.outDegree = new int[digraph.getNumVertex()];
        this.sources = new Bag<>();
        this.sinks = new Bag<>();
        computeDegrees();
        findSourcesAndSinks();
    }

    private void computeDegrees() {
        for (int v = 0; v < this.digraph.getNumVertex(); v++) {
            for (int adj : this.digraph.adjacentTo(v)) {
                this.inDegree[adj]++;
                this.outDegree[v]++;
            }
        }
    }

    private void findSourcesAndSinks() {
        for (int i = 0; i < this.digraph.getNumVertex(); i++) {
            if (this.inDegree[i] == 0) {
                this.sources.add(i);
            }
            if (this.outDegree[i] == 0) {
                this.sinks.add(i);
            }
        }
    }

    public int inDegree(int v) {
        return this.inDegree[v];
    }

    public int outDegree(int v) {
        return this.outDegree[v];
    }

    public Iterable<Integer> sources() {
        return this.sources;
    }

    public Iterable<Integer> sinks() {
        return this.sinks;
    }

    public boolean isMap() {
        return this.sinks.size() == 0;
    }
}
