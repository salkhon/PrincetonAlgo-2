public class Digraph extends Graph {
    public Digraph(int v) {
        super(v);
    }

    @Override
    public void addEdge(int v, int w) {
        super.adjList[v].add(w);
    }

    public Digraph reverse() {
        Digraph reverse = new Digraph(super.adjList.length);
        for (int v = 0; v < super.adjList.length; v++) {
            for (int adj : super.adjList[v]) {
                reverse.addEdge(adj, v);
            }
        }
        return reverse;
    }
}
