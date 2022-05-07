import edu.princeton.cs.algs4.Stack;

import java.util.Iterator;

// THM: if one component (with other components it leads to) has a directed edge from another component but has no path back to that other component -
// both components have at least one vertex with unequal in and out degree

// there will be only one strong component. And no maximal or minimal edges.
public class Euler {
    private Digraph digraph;

    private Stack<Integer> eulerCycle;

    // cache
    private Degree degree;
    private int[] numOutPathsTaken;
    private Iterator<Integer>[] adjIterators;

    @SuppressWarnings("unchecked")
    public Euler(Digraph digraph) {
        this.digraph = digraph;
        this.degree = new Degree(digraph);
        if (containsEqualInOutDegree()) {
            this.eulerCycle = new Stack<>();
            this.numOutPathsTaken = new int[digraph.getNumVertex()];
            this.adjIterators = (Iterator<Integer>[]) new Iterator[digraph.getNumVertex()];
            for (int i = 0; i < digraph.getNumVertex(); i++) {
                this.adjIterators[i] = digraph.adjacentTo(i).iterator();
            }
            findEulerCycle();
        }
    }

    private boolean containsEqualInOutDegree() {
        boolean contains = true;
        for (int i = 0; i < this.digraph.getNumVertex(); i++) {
            if (this.degree.inDegree(i) != this.degree.outDegree(i)) {
                contains = false;
                break;
            }
        }
        return contains;
    }

    // not going to put vertices on stack before the starting vertex of the tour is encountered.
    // once the start is encountered will put vertex on cycleStack as in call stack. Will use DFS
    // to visit unvisited paths (kept track using this.numOutPathsTaken) and will put them on cycleStack.
    // every vertex has equal in and out degrees. So if a branch of path is taken, it is a guarantee that
    // traversal will lead back to that branch-out vertex.
    // if there is an in left, there is an out left. No in will be taken twice because in of one vertex is out of another
    // which are tracked
    // each vertex entered will be left. But the source was left. So that will be the exhausting one and the last one
    // on the stack, the first one to run out of adjacent vertices
    private void findEulerCycle() {
        dfs(0);
    }

    private void dfs(int v) {
        while (this.adjIterators[v].hasNext()) {
            dfs(this.adjIterators[v].next());
        }
        this.eulerCycle.push(v);
    }

    // test client
    public static void main(String[] args) {

    }
}
