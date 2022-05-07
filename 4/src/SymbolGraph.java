import edu.princeton.cs.algs4.In;

import java.io.File;
import java.util.*;

public class SymbolGraph {
    private Map<String, Integer> vertexIds; // name to id
    private String[] vertices; // id to vertex

    private Graph graph;

    // no need for separate Map of string to bag of int just to distinguish between movies and actors
    // can just store movies and actors on separate arrays of string or int
    // plus the first method introduces an extra lgN factor for querying adj

    public SymbolGraph(String fileName, String delimiter) {
        this.vertexIds = new TreeMap<>();
        List<String[]> inputLineSplits = getInputAndPrepareMap(fileName, delimiter);

        this.graph = new Graph(this.vertexIds.size());
        prepareGraphByLineSplits(inputLineSplits);
    }

    private void prepareGraphByLineSplits(List<String[]> inputLineSplits) {
        int movieId;
        int actorId;
        for (String[] splits : inputLineSplits) {
            movieId = this.vertexIds.get(splits[0]);
            for (int i = 1; i < splits.length; i++) {
                actorId = this.vertexIds.get(splits[i]);
                this.graph.addEdge(movieId, actorId);
            }
        }
    }

    private List<String[]> getInputAndPrepareMap(String fileName, String delimiter) {
        In in = new In(new File(fileName));
        List<String[]> inputLineSplits = new ArrayList<>();
        List<String> vertices = new ArrayList<>();

        String line;
        String[] splits;
        while ((line = in.readLine()) != null) {
            splits = line.split(delimiter);
            inputLineSplits.add(splits);
            if (this.vertexIds.putIfAbsent(splits[0], this.vertexIds.size()) == null) {
                vertices.add(splits[0]);
            }
            for (int i = 1; i < splits.length; i++) {
                if (this.vertexIds.putIfAbsent(splits[i], this.vertexIds.size()) == null) {
                    vertices.add(splits[i]);
                }
            }
        }
        this.vertices = vertices.toArray(new String[0]);
        return inputLineSplits;
    }

    public boolean contains(String key) {
        return this.vertexIds.containsKey(key);
    }

    public int index(String key) {
        return this.vertexIds.getOrDefault(key, -1);
    }

    public String name(int v) {
        return v < this.vertices.length ?
                this.vertices[v] : null;
    }

    public Graph getGraph() {
        return graph;
    }

    // test client
    public static void main(String[] args) {
        Integer integer = 5;
        Random random = new Random();
        List<Integer> integers = new ArrayList<>();
        integers.sort((o1, o2) -> 0);
        int[][] ints = new int[3][];
        SymbolGraph symbolGraph = new SymbolGraph("movies.txt", "/");
        Graph graph = symbolGraph.getGraph();
        BreadthFirstPaths breadthFirstPaths = new BreadthFirstPaths(graph, symbolGraph.index("Bacon, Kevin"));
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter Actor Name: ");
            String actor = scanner.nextLine();
            if (symbolGraph.contains(actor) && breadthFirstPaths.hasPathTo(symbolGraph.index(actor))) {
                Iterable<Integer> kevinBaconPath = breadthFirstPaths.pathTo(symbolGraph.index(actor));
                int length = 0;
                for (int v : kevinBaconPath) {
                    length++;
                }
                System.out.println("Kevin Bacon Number: " + (length - 1) / 2);
                System.out.println("Kevin Bacon Path: ");
                for (int v : kevinBaconPath) {
                    System.out.print(symbolGraph.name(v) + " --> ");
                }
                System.out.println();
            } else {
                System.out.println("No path found for " + actor);
            }
        }

    }
}
