import edu.princeton.cs.algs4.Stack;

public class DirectedCycle {
    private Digraph digraph;
    private boolean[] marked;
    private int[] edgeTo;
    private boolean[] onStack;

    private boolean hasCycle;

    // cycle cache
    private Stack<Integer> cycle;

    public DirectedCycle(Digraph digraph) {
        this.digraph = digraph;
        this.marked = new boolean[digraph.getNumVertex()];
        this.edgeTo = new int[digraph.getNumVertex()];
        this.onStack = new boolean[digraph.getNumVertex()];

        for (int v = 0; v < digraph.getNumVertex() && !this.hasCycle; v++) {
            if (!this.marked[v]) {
                dfs(v);
            }
        }
    }

    private void dfs(int v) {
        this.marked[v] = true;
        this.onStack[v] = true;
        for (int adj : this.digraph.adjacentTo(v)) {
            if (this.hasCycle) {
                break;
            } else if (!this.marked[adj]) {
                this.edgeTo[adj] = v;
                dfs(adj);
            } else if (this.onStack[adj]) {
                this.hasCycle = true;
                this.cycle = new Stack<>();
                this.cycle.push(adj);
                for (int i = v; i != adj; i = this.edgeTo[i]) {
                    this.cycle.push(i);
                }
                this.cycle.push(adj);
            }
        }
        this.onStack[v] = false;
    }

    public boolean hasCycle() {
        return hasCycle;
    }

    public Iterable<Integer> getCycle() {
        return this.cycle;
    }
}

//public class DirectedCycle {
//    private Digraph digraph;
//    private boolean[] marked;
//    private Stack<Integer> minCycleStack;
//
//    public DirectedCycle(Digraph digraph) {
//        this.digraph = digraph;
//        this.marked = new boolean[digraph.getNumVertex()];
//
//        dfs(0);
//    }
//
//    private void dfs(int source) {
//        Stack<Integer> traversalStack = new Stack<>();
//        traversalStack.push(source);
//        while (!traversalStack.isEmpty()) {
//            int v = traversalStack.peek();
//            if (!this.marked[v]) {
//                this.marked[v] = true;
//                Stack<Integer> cycle;
//                for (int adj : this.digraph.adjacentTo(v)) {
//                    // to be in a cycle vertex needs to be marked (path taken) and needs to be in the stack (on current path)
//                    if (!this.marked[adj]) {
//                        traversalStack.push(adj);
//                    } else if ((cycle = getCycleFromStackWith(adj, traversalStack)) != null) {
//                        storeCycleIfMin(cycle);
//                    }
//                }
//            } else {
//                traversalStack.pop();
//            }
//        }
//    }
//
//    private Stack<Integer> getCycleFromStackWith(int v, Stack<Integer> traversalStack) {
//        Stack<Integer> cycle = new Stack<>();
//
//        for (int w = -1; w != v && !traversalStack.isEmpty();) {
//            w = traversalStack.pop();
//            cycle.push(w);
//        }
//
//        // iterating does not pop
//        for (int w : cycle) {
//            traversalStack.push(w);
//        }
//
//        if (cycle.peek() != v) {
//            // cycle not found
//            cycle = null;
//        }
//
//        return cycle;
//    }
//
//    private void storeCycleIfMin(Stack<Integer> cycle) {
//        if (this.minCycleStack == null) {
//            this.minCycleStack = cycle;
//        } else if (cycle.size() < this.minCycleStack.size()) {
//            this.minCycleStack = cycle;
//        }
//    }
//
//    public Iterable<Integer> getMinCycle() {
//        return minCycleStack;
//    }
//}
