public class TwoColor {
    private Graph graph;
    private boolean[] marked;
    private boolean[] color;
    private boolean isBipartite;

    public TwoColor(Graph graph) {
        this.graph = graph;
        this.marked = new boolean[graph.getNumVertex()];
        this.color = new boolean[graph.getNumVertex()];
        this.isBipartite = true;
        for (int i = 0; i < graph.getNumVertex(); i++) {
            if (!marked[i]) {
                dfs(i);
            }
        }
    }

    private void dfs(int v) {
        this.marked[v] = true;
        for (int adj : this.graph.adjacentTo(v)) {
            if (!this.marked[adj]) {
                this.color[adj] = !this.color[v];
                dfs(adj);
            } else if (this.color[adj] == this.color[v]) {
                this.isBipartite = false;
            }
        }
    }

    public boolean isBipartite() {
        return this.isBipartite;
    }
}
