import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;

public class Graph {
    protected Bag<Integer>[] adjList;

    @SuppressWarnings("unchecked")
    public Graph(int numVertex) {
        this.adjList = (Bag<Integer>[]) new Bag[numVertex];
        for (int i = 0; i < numVertex; i++) {
            this.adjList[i] = new Bag<>();
        }
    }

    @SuppressWarnings("unchecked")
    public Graph(In in) {
        int numVertex = in.readInt();
        if (numVertex > 0) {
            this.adjList = (Bag<Integer>[]) new Bag[numVertex];
            while (in.hasNextLine()) {
                String[] vertices = in.readLine().split(" ");
                int v = Integer.parseInt(vertices[0]);
                int w = Integer.parseInt(vertices[1]);
                addEdge(v, w);
            }
        }
    }

    public void addEdge(int v, int w) {
        if (v < this.adjList.length && w < this.adjList.length) {
            this.adjList[v].add(w);
            this.adjList[w].add(v);
        }
    }

    public int getNumVertex() {
        return this.adjList.length;
    }

    public int getNumEdge() {
        int nEdge = 0;
        for (Bag<Integer> bag : this.adjList) {
            nEdge += bag.size();
        }
        return nEdge;
    }

    public Iterable<Integer> adjacentTo(int v) {
        Bag<Integer> integerBag = null;
        if (v < this.adjList.length) {
            integerBag = this.adjList[v];
        }
        return integerBag;
    }

    public int degreeOf(int v) {
        int degree = -1;
        if (v < this.adjList.length) {
            degree = this.adjList[v].size();
        }
        return degree;
    }

    public int maxDegree() {
        int maxDegree = 0;
        for (Bag<Integer> bag : this.adjList) {
            if (bag.size() > maxDegree) {
                maxDegree = bag.size();
            }
        }
        return maxDegree;
    }

    public double avgDegree() {
        return getNumEdge() / 2.0;
    }
}
