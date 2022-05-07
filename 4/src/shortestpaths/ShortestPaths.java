package shortestpaths;

public interface ShortestPaths {
    double weightOfPathTo(int v);
    Iterable<WeightedDiEdge> pathTo(int v);
    boolean hasPathTo(int v);
}
