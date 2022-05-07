import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

public class BreadthFirstPaths {
    private Graph graph;
    private int source;
    private int[] edgeTo;
    private int[] distTo;

    public BreadthFirstPaths(Graph graph, int source) {
        this.graph = graph;
        this.source = source;
        this.edgeTo = new int[this.graph.getNumVertex()];
        this.distTo = new int[this.graph.getNumVertex()];

        BFS();
    }

    private void BFS() {
        Queue<Integer> vertexQueue = new Queue<>();
        vertexQueue.enqueue(this.source);
        int vertex;
        do {
            vertex = vertexQueue.dequeue();
            for (int adjacent : this.graph.adjacentTo(vertex)) {
                if (!isMarked(adjacent)) {
                    this.edgeTo[adjacent] = vertex;
                    this.distTo[adjacent] = this.distTo[vertex] + 1;
                    vertexQueue.enqueue(adjacent);
                }
            }
        } while (!vertexQueue.isEmpty());
    }

    private boolean isMarked(int vertex) {
        boolean isM = true;
        if (vertex != this.source) {
            isM = this.distTo[vertex] > 0;
        }
        return isM;
    }

    public boolean hasPathTo(int vertex) {
        return isMarked(vertex);
    }

    public Iterable<Integer> pathTo(int vertex) {
        Stack<Integer> pathStack = new Stack<>();
        if (hasPathTo(vertex)) {
            for (int v = vertex; v != this.source; v = edgeTo[v]) {
                pathStack.push(v);
            }
            pathStack.push(this.source);
        }
        return pathStack;
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
        BreadthFirstPaths breadthFirstPaths = new BreadthFirstPaths(graph, 0);
        
        for (int v : breadthFirstPaths.pathTo(3)) {
            System.out.print(v + " -> ");
        }
    }
}
