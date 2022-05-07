public class KosarajuSharirStrongComponent {
    private Digraph digraph;
    private boolean[] marked;
    private int[] strongComponentId;
    private int numStrongComponents;

    public KosarajuSharirStrongComponent(Digraph digraph) {
        this.digraph = digraph;
        this.marked = new boolean[digraph.getNumVertex()];
        this.strongComponentId = new int[digraph.getNumVertex()];
        this.numStrongComponents = 0;

        DepthFirstSearchOrder reverseDigraphReversePostOrder = new DepthFirstSearchOrder(digraph.reverse());
        for (int v : reverseDigraphReversePostOrder.reversePostOrder()) {
            if (!marked[v]) {
                dfs(v);
                this.numStrongComponents++;
            }
        }
    }

    private void dfs(int v) {
        this.marked[v] = true;
        this.strongComponentId[v] = this.numStrongComponents;

        for (int adj : this.digraph.adjacentTo(v)) {
            if (!this.marked[adj]) {
                dfs(v);
            }
        }
    }

    public boolean isSameComponent(int v, int w) {
        return this.strongComponentId[v] == this.strongComponentId[w];
    }

    public int getNumStrongComponents() {
        return numStrongComponents;
    }
}
