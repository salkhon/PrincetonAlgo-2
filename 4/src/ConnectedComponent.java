public class ConnectedComponent {
    private Graph graph;

    private boolean[] marked;
    private int[] componentId;
    private int numConnectedComponents;

    public ConnectedComponent(Graph graph) {
        this.graph = graph;
        this.marked = new boolean[graph.getNumVertex()];
        this.componentId = new int[graph.getNumVertex()];

        this.numConnectedComponents = 0;
        for (int i = 0; i < graph.getNumVertex(); i++) {
            if (!this.marked[i]) {
                DFS(i);
                this.numConnectedComponents++;
            }
        }
    }

    private void DFS(int vertex) {
        this.marked[vertex] = true;
        this.componentId[vertex] = this.numConnectedComponents;
        for (int adjacent : this.graph.adjacentTo(vertex)) {
            if (!marked[adjacent]) {
                DFS(adjacent);
            }
        }
    }

    public int numberOfConnectedComponents() {
        return this.numConnectedComponents;
    }

    public boolean isConnectedTo(int v, int w) {
        return this.componentId[v] == this.componentId[w];
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

        ConnectedComponent connectedComponent = new ConnectedComponent(graph);
        System.out.println("Number of connected components: " + connectedComponent.numberOfConnectedComponents());
        System.out.println("is " + 5 + " connected to " + 7 + " ? " + connectedComponent.isConnectedTo(5, 7));
        System.out.println("is " + 3 + " connected to " + 0 + " ? " + connectedComponent.isConnectedTo(3, 0));


        System.out.println("Component Id:");
        for (int v = 0; v < graph.getNumVertex(); v++) {
            System.out.println(v + " : " + connectedComponent.componentId[v]);
        }
    }
}
