import edu.princeton.cs.algs4.Stack;
import shortestpaths.EdgeWeightedDiGraph;

public class DepthFirstSearchOrder {
    private Digraph digraph;
    private Stack<Integer> reversePostOrder;
    private boolean[] marked;

    public DepthFirstSearchOrder(Digraph digraph) {
        this.digraph = digraph;
        this.reversePostOrder = new Stack<>();
        this.marked = new boolean[digraph.getNumVertex()];

        for (int v = 0; v < digraph.getNumVertex(); v++) {
            if (!this.marked[v]) {
                dfs(v);
            }
        }
    }

    public DepthFirstSearchOrder(EdgeWeightedDiGraph edgeWeightedDiGraph) {

    }

    private void dfs(int v) {
        this.marked[v] = true;
        for (int adjacent : this.digraph.adjacentTo(v)) {
            if (!this.marked[adjacent]) {
                dfs(adjacent);
            }
        }
        this.reversePostOrder.push(v);
    }

    public Iterable<Integer> reversePostOrder() {
        return this.reversePostOrder;
    }
}
