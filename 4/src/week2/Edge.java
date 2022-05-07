package week2;

public class Edge implements Comparable<Edge> {
    private final int v;
    private final int w;

    private final double weight;

    public Edge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public int either() {
        return this.v;
    }

    public int other(int vertex) {
        return vertex == this.v ?
                this.w : this.v;
    }

    @Override
    public int compareTo(Edge that) {
        return Double.compare(this.weight, that.weight);
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return this.v + "-" + this.w;
    }
}
