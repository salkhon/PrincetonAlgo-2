import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.ResizingArrayBag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;

import java.util.Iterator;

public class SAP {
    private Digraph digraph;

    // cache v-w
    private int ancestor;
    private int length;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }
        this.digraph = new Digraph(G.V());
        for (int v = 0; v < G.V(); v++) {
            for (int adj : G.adj(v)) {
                this.digraph.addEdge(v, adj);
            }
        }
    }

    // this method meets in the middle from both sources. But shortest ancestor might not be in
    // equal distance from both source. Might be further from one source and still closer that 2 * distance from one source.
//    private void computeSAP(Iterable<Integer> v, Iterable<Integer> w) {
//        Queue<Integer> bfsQueueForV = new Queue<>();
//        boolean[] markedFromV = new boolean[this.digraph.V()];
//        int[] degreeOfSeparationFromV = new int[this.digraph.V()];
//
//        Queue<Integer> bfsQueueForW = new Queue<>();
//        boolean[] markedFromW = new boolean[this.digraph.V()];
//        int[] degreeOfSeparationFromW = new int[this.digraph.V()];
//
//        this.ancestor = -1;
//        this.length = -1;
//
//        for (int vertex : v) {
//            bfsQueueForV.enqueue(vertex);
//            markedFromV[vertex] = true;
//        }
//
//        for (int vertex : w) {
//            bfsQueueForW.enqueue(vertex);
//            markedFromW[vertex] = true;
//            if (markedFromV[vertex]) {
//                this.ancestor = vertex;
//                this.length = 0;
//                return;
//            }
//        }
//
//        // shortest path cannot depend on the out degree of current vertex. So each iteration needs to
//        // dequeue all other vertices with same degree of separation
//        while (this.ancestor == -1 && (!bfsQueueForV.isEmpty() || !bfsQueueForW.isEmpty())) {
//            if (!bfsQueueForV.isEmpty()) {
//                continueBFSFromV(bfsQueueForV, markedFromV, markedFromW, degreeOfSeparationFromV, degreeOfSeparationFromW);
//            }
//
//            if (this.ancestor == -1 && !bfsQueueForW.isEmpty()) {
//                continueBFSFromV(bfsQueueForW, markedFromW, markedFromV, degreeOfSeparationFromW, degreeOfSeparationFromV);
//            }
//        }
//    }
//
//    private void continueBFSFromV(Queue<Integer> bfsQueueForV, boolean[] markedFromV, boolean[] markedFromW,
//                                  int[] degreeOfSeparationFromV, int[] degreeOfSeparationFromW) {
//        int degreeToComplete = degreeOfSeparationFromV[bfsQueueForV.peek()];
//        for (int vertex; this.ancestor == -1 &&
//                !bfsQueueForV.isEmpty() &&
//                degreeOfSeparationFromV[bfsQueueForV.peek()] == degreeToComplete;) {
//            vertex = bfsQueueForV.dequeue();
//            for (int adj : this.digraph.adj(vertex)) {
//                if (!markedFromV[adj]) {
//                    bfsQueueForV.enqueue(adj);
//                    markedFromV[adj] = true;
//                    degreeOfSeparationFromV[adj] = degreeOfSeparationFromV[vertex] + 1;
//                    if (markedFromW[adj]) {
//                        // shortest common ancestor
//                        this.ancestor = adj;
//                        this.length = degreeOfSeparationFromV[adj] + degreeOfSeparationFromW[adj];
//                        break;
//                    }
//                }
//            }
//        }
//    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        ResizingArrayBag<Integer> vs = new ResizingArrayBag<>();
        vs.add(v);
        ResizingArrayBag<Integer> ws = new ResizingArrayBag<>();
        ws.add(w);
        return length(vs, ws);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        ResizingArrayBag<Integer> vs = new ResizingArrayBag<>();
        vs.add(v);
        ResizingArrayBag<Integer> ws = new ResizingArrayBag<>();
        ws.add(w);
        return ancestor(vs, ws);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkIterableValidity(v, w);
        computeSAP(v, w);
        return this.ancestor != -1 ?
                this.length : -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkIterableValidity(v, w);
        computeSAP(v, w);
        return this.ancestor;
    }

    private void computeSAP(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths breadthFirstDirectedPathsFromV = new BreadthFirstDirectedPaths(this.digraph, v);
        BreadthFirstDirectedPaths breadthFirstDirectedPathsFromW = new BreadthFirstDirectedPaths(this.digraph, w);
        this.ancestor = -1;
        this.length = Integer.MAX_VALUE;
        for (int vertex = 0, dist; vertex < this.digraph.V(); vertex++) {
            if (breadthFirstDirectedPathsFromV.hasPathTo(vertex) && breadthFirstDirectedPathsFromW.hasPathTo(vertex)) {
                dist = breadthFirstDirectedPathsFromV.distTo(vertex) + breadthFirstDirectedPathsFromW.distTo(vertex);
                if (dist < this.length) {
                    this.length = dist;
                    this.ancestor = vertex;
                }
            }
        }
    }

    private void checkIterableValidity(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null || !v.iterator().hasNext() || !w.iterator().hasNext()) {
            throw new IllegalArgumentException();
        }
        for (Iterator<Integer> it = v.iterator(); it.hasNext(); ) {
            if (it.next() == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("digraph5.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
