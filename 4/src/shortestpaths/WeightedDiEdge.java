package shortestpaths;

public class WeightedDiEdge implements Comparable<WeightedDiEdge> {
    private final int from, to;
    private final double weight;

    public WeightedDiEdge(int from, int to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public int from() {
        return this.from;
    }

    public int to() {
        return this.to;
    }

    public double weight() {
        return this.weight;
    }

    @Override
    public int compareTo(WeightedDiEdge that) {
        return Double.compare(this.weight, that.weight);
    }

    @Override
    public String toString() {
        return this.from + " -> " + this.to;
    }
}
