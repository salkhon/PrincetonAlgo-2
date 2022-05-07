import edu.princeton.cs.algs4.Stack;

public class DepthFirstPaths {
    private Graph graph;
    private int start;

    private boolean[] visited;
    private int[] edgeTo;

    public DepthFirstPaths(Graph graph, int start) {
        this.graph = graph;
        this.start = start;
        this.visited = new boolean[graph.getNumVertex()];
        this.edgeTo = new int[graph.getNumVertex()];
        DFS(start);
//        DFSIter();
    }

    private void DFS(int v) {
        this.visited[v] = true;
        for (int adjacentVertex : this.graph.adjacentTo(v)) {
            if (!visited[adjacentVertex]) {
                DFS(adjacentVertex);
                this.edgeTo[adjacentVertex] = v;
            }
        }
    }

    private void DFSIter() {
        Stack<Integer> vertexStack = new Stack<>();
        vertexStack.push(this.start);
        int v;
        while (!vertexStack.isEmpty()) {
            v = vertexStack.pop();
            this.visited[v] = true;
            for (int adj : this.graph.adjacentTo(v)) {
                if (!this.visited[adj]) {
                    vertexStack.push(adj);
                    this.edgeTo[adj] = v;
                }
            }
        }
    }

    public boolean hasPathTo(int v) {
        return this.visited[v];
    }

    public Iterable<Integer> pathTo(int v) {
        Stack<Integer> pathToV = null;
        if (hasPathTo(v)) {
            pathToV = new Stack<>();
            for (int vertex = v; vertex != this.start; vertex = this.edgeTo[vertex]) {
                pathToV.push(vertex);
            }
            pathToV.push(this.start);
        }
        return pathToV;
    }

    // client
    public static void main(String[] args) {
        Graph graph = new Graph(13);
        graph.addEdge(0, 5);
        graph.addEdge(4, 3);
        graph.addEdge(0, 1);
        graph.addEdge(9, 12);
        graph.addEdge(6, 4);
        graph.addEdge(5, 4);
        graph.addEdge(0, 2);
        graph.addEdge(11, 12);
        graph.addEdge(9, 10);
        graph.addEdge(0, 6);
        graph.addEdge(7, 8);
        graph.addEdge(9, 11);
        graph.addEdge(5, 3);
        DepthFirstPaths depthFirstPaths = new DepthFirstPaths(graph, 0);
        for (int v : depthFirstPaths.pathTo(3)) {
            System.out.print(v + " -> ");
        }

        System.out.println();
        System.out.println("edgeTo: ");
        for (int i = 0; i < graph.getNumVertex(); i++) {
            System.out.println(depthFirstPaths.edgeTo[i] + " -> " + i);
        }
    }
}
