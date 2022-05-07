import edu.princeton.cs.algs4.Queue;

public class QBasedTopologicalSort {
    private Digraph digraph;
    private int[] inDegrees;
    private int[] outDegrees;

    private Queue<Integer> topologicalOrder;

    public QBasedTopologicalSort(Digraph digraph) {
        this.digraph = digraph;
        this.topologicalOrder = new Queue<>();
        this.inDegrees = new int[digraph.getNumVertex()];
        this.outDegrees = new int[digraph.getNumVertex()];

        prepareDegreeArrays();

        topologicalSort();
    }

    private void prepareDegreeArrays() {
        for (int i = 0; i < this.digraph.getNumVertex(); i++) {
            for (int adj : this.digraph.adjacentTo(i)) {
                this.outDegrees[i]++;
                this.inDegrees[adj]++;
            }
        }
    }

    public void topologicalSort() {
        Queue<Integer> minimalVertices = new Queue<>();
        // enqueue minimal elements
        for (int i = 0; i < this.digraph.getNumVertex(); i++) {
            if (this.inDegrees[i] == 0) {
                minimalVertices.enqueue(i);
                this.topologicalOrder.enqueue(i);
            }
        }

        while (!minimalVertices.isEmpty()) {
            int minimal = minimalVertices.dequeue();
            for (int adj : this.digraph.adjacentTo(minimal)) {
                this.inDegrees[adj]--;
                if (this.inDegrees[adj] == 0) {
                    // new minimal
                    minimalVertices.enqueue(adj);
                    this.topologicalOrder.enqueue(adj);
                }
            }
        }
    }

    public Iterable<Integer> getTopologicalOrder() {
        return this.topologicalOrder;
    }
}
