package mincut_maxflow;

public class FloEdge {
    private final int v;
    private final int w;

    private final double capacity;
    private double flow;

    public FloEdge(int v, int w, double capacity) {
        this.v = v;
        this.w = w;
        this.capacity = capacity;
        this.flow = 0;
    }

    public int from() {
        return this.v;
    }

    public int to() {
        return this.w;
    }

    public int other(int vertex) {
        int other;
        if (vertex == w) {
            other = v;
        } else if (vertex == v) {
            other = w;
        } else {
            throw new RuntimeException("Illegal endpoint");
        }
        return other;
    }

    public double capacity() {
        return this.capacity;
    }

    public double flow() {
        return this.flow;
    }

    public double residualCapacityTo(int vertex) {
        double resCap = 0.0;
        if (vertex == this.w) {
            // forward edge
            resCap = this.capacity - this.flow;
        } else if (vertex == this.v) {
            // backward edge
            resCap = flow;
        } else {
            throw new IllegalArgumentException();
        }
        return resCap;
    }

    public void addResidualFlowTo(int vertex, double delta) {
        if (vertex == this.w) {
            this.flow += delta;
        } else if (vertex == this.v) {
            this.flow -= delta;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
