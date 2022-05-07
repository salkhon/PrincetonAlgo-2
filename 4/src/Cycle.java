public class Cycle {
    private Graph graph;
    private boolean[] marked;
    private boolean hasCycle;

    public Cycle(Graph graph) {
        this.graph = graph;
        this.marked = new boolean[graph.getNumVertex()];
        for (int i = 0; i < graph.getNumVertex(); i++) {
            if (!this.marked[i]) {
                dfs(i, i);
            }
        }
    }

    private void dfs(int v, int predecessor) {
        this.marked[v] = true;
        for (int adj : this.graph.adjacentTo(v)) {
            if (!marked[adj]) {
                dfs(adj, v);
            } else if (adj != predecessor) {
                this.hasCycle = true; // could terminate search here
            }
        }
    }

    public boolean hasCycle() {
        return hasCycle;
    }
}
