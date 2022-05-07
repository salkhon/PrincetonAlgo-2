/*
* The eccentricity of a vertex v is the the length of the shortest path from that vertex to the furthest vertex from v.
* The diameter of a graph is the maximum eccentricity of any vertex.
* The radius of a graph is the smallest eccentricity of any vertex.
* A center is a vertex whose eccentricity is the radius.
* The girth of a graph is the length of its shortest cycle.
* */

import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;
import java.util.Comparator;

public class GraphProperties {
    private Graph graph;

    // source specific
    private int source;
    private boolean[] marked;
    private int[] pathTo;
    private int[] degreeOfSeparation;
    private int[] shortestCycleContaining;

    public GraphProperties(Graph graph) {
        this.graph = graph;
        this.pathTo = new int[graph.getNumVertex()];
        this.degreeOfSeparation = new int[graph.getNumVertex()];
        this.marked = new boolean[graph.getNumVertex()];
        this.shortestCycleContaining = new int[graph.getNumVertex()];
    }

    private void bfs(int s) {
        this.source = s;
        this.degreeOfSeparation[s] = 0;
        Arrays.fill(this.marked, false);

        Queue<Integer> vertexQueue = new Queue<>();
        vertexQueue.enqueue(s);
        int v;
        int shortestCycle = this.graph.getNumVertex();
        while (!vertexQueue.isEmpty()) {
            v = vertexQueue.dequeue();
            for (int adj : this.graph.adjacentTo(v)) {
                if (!this.marked[adj]) {
                    this.marked[adj] = true;
                    this.pathTo[adj] = v;
                    vertexQueue.enqueue(adj);
                    this.degreeOfSeparation[adj] = this.degreeOfSeparation[v] + 1;
                } else if (adj != this.pathTo[v]) {
                    int currentCycleLength = this.degreeOfSeparation[adj] + this.degreeOfSeparation[v] + 1;
                    if (currentCycleLength < shortestCycle) {
                        shortestCycle = currentCycleLength;
                    }
                }
            }
        }
        this.shortestCycleContaining[s] = shortestCycle;
    }

    public int eccentricity(int v) {
        bfs(v);
        int maxDegreeOfSeparation = 0;
        for (int ver = 0; v < this.degreeOfSeparation.length; ver++) {
            if (this.marked[ver] && this.degreeOfSeparation[ver] > maxDegreeOfSeparation) {
                maxDegreeOfSeparation = this.degreeOfSeparation[ver];
            }
        }
        return maxDegreeOfSeparation;
    }

    public int diameter() {
        return findExtremeEccentricity(Integer::compare);
    }

    public int radius() {
        return findExtremeEccentricity((o1, o2) -> Integer.compare(o2, o1));
    }

    private int findExtremeEccentricity(Comparator<Integer> integerComparator) {
        int extremum = eccentricity(0);
        int currentEccentricity;
        for (int v = 1; v < this.graph.getNumVertex(); v++) {
            currentEccentricity = eccentricity(v);
            if (integerComparator.compare(currentEccentricity, extremum) > 0) {
                extremum = currentEccentricity;
            }
        }
        return extremum;
    }

    public int center() {
        int minEccentricityVertex = 0;
        int minEccentricity = eccentricity(0);
        int currentEccentricity;
        for (int v = 1; v < this.graph.getNumVertex(); v++) {
            currentEccentricity = eccentricity(v);
            if (currentEccentricity < minEccentricity) {
                minEccentricityVertex = v;
                minEccentricity = currentEccentricity;
            }
        }
        return minEccentricityVertex;
    }

    public int girth() {
        return Arrays.stream(this.shortestCycleContaining).min().orElse(-1);
    }
}
