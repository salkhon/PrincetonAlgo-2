import edu.princeton.cs.algs4.Picture;

import java.util.Arrays;

public class SeamCarver {
    private static final double BOUNDARY_ENERGY = 1000;

    private Picture picture;

    private final SeamFinder seamFinder;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        checkForNullArgument(picture);
        this.picture = clone(picture);
        this.seamFinder = new SeamFinder();
    }

    private Picture clone(Picture picture) {
        Picture clone = new Picture(picture.width(), picture.height());
        for (int x = 0; x < picture.width(); x++) {
            for (int y = 0; y < picture.height(); y++) {
                clone.setRGB(x, y, picture.getRGB(x, y));
            }
        }
        return clone;
    }

    // current picture
    public Picture picture() {
        return clone(this.picture);
    }

    // width of current picture
    public int width() {
        return this.picture.width();
    }

    // height of current picture
    public int height() {
        return this.picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        checkCoordinates(x, y);

        double energy = 0;

        if (x == 0 || y == 0 || x == this.picture.width() - 1 || y == this.picture.height() - 1) {
            energy = BOUNDARY_ENERGY;
        } else {
            int delX2 = getDel2(this.picture.getRGB(x - 1, y), this.picture.getRGB(x + 1, y));
            int delY2 = getDel2(this.picture.getRGB(x, y - 1), this.picture.getRGB(x, y + 1));
            energy = Math.sqrt(delX2 + delY2);
        }

        return energy;
    }

    private int getDel2(int rgbL, int rgbR) {
        int mask = 0x00_00_00_FF;
        int bL = rgbL & mask, bR = rgbR & mask, delB = bL - bR;
        int gL = (rgbL >> 8) & mask, gR = (rgbR >> 8) & mask, delG = gL - gR;
        int rL = (rgbL >> 16) & mask, rR = (rgbR >> 16) & mask, delR = rL - rR;

        return delR * delR + delG * delG + delB * delB;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (this.picture.height() > 0) {
            this.seamFinder.computeSeam(true);
            return this.seamFinder.seam;
        } else {
            return null;
        }
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (this.picture.width() > 0) {
            this.seamFinder.computeSeam(false);
            return this.seamFinder.seam;
        } else {
            return null;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        checkForNullArgument(seam);
        checkSeamValidity(seam, true);

        this.picture = carvedPicture(seam, true);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        checkForNullArgument(seam);
        checkSeamValidity(seam, false);

        this.picture = carvedPicture(seam, false);
    }

    private Picture carvedPicture(int[] seam, boolean isHorizontal) {
        Picture carved;
        if (isHorizontal) {
            carved = new Picture(this.picture.width(), this.picture.height() - 1);
        } else {
            carved = new Picture(this.picture.width() - 1, this.picture.height());
        }

        int lenDim = isHorizontal ? this.picture.width() : this.picture.height();
        int antiLenDim = isHorizontal ? this.picture.height() : this.picture.width();

        for (int i = 0; i < lenDim; i++) {
            for (int j = 0, carvedJ = 0; j < antiLenDim; j++) {
                if (j != seam[i]) {
                    if (isHorizontal) {
                        carved.setRGB(i, carvedJ, this.picture.getRGB(i, j));
                    } else {
                        carved.setRGB(carvedJ, i, this.picture.getRGB(j, i));
                    }
                    carvedJ++;
                }
            }
        }
        return carved;
    }

    private void checkForNullArgument(Object o) {
        if (o == null) {
            throw new IllegalArgumentException();
        }
    }

    private void checkCoordinates(int x, int y) {
        if (x < 0 || y < 0 || x >= this.picture.width() || y >= this.picture.height()) {
            throw new IllegalArgumentException();
        }
    }

    private void checkSeamValidity(int[] seam, boolean isHorizontal) {
        int pixelCount = isHorizontal ? this.picture.width() : this.picture.height();

        if (seam.length != pixelCount) {
            throw new IllegalArgumentException();
        }

        int availablePixels = isHorizontal ? this.picture.height() : this.picture.width();

        if (availablePixels <= 1) {
            throw new IllegalArgumentException();
        }

        int prev = seam[0];
        for (int i : seam) {
            if (i < 0 || i >= availablePixels) {
                throw new IllegalArgumentException();
            }

            if (i - prev < -1 || i - prev > 1) {
                throw new IllegalArgumentException();
            }

            prev = i;
        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        SeamCarver seamCarver = new SeamCarver(new Picture("6x5.png"));
        printPic(seamCarver.picture, seamCarver);

        System.out.println("Horizontal Seam:");
        for (int i : seamCarver.findHorizontalSeam()) {
            System.out.println(i + " ");
        }
        seamCarver.removeHorizontalSeam(seamCarver.findHorizontalSeam());
        printPic(seamCarver.picture, seamCarver);

        System.out.println("Vertical Seam: ");
        for (int i : seamCarver.findVerticalSeam()) {
            System.out.println(i + " ");
        }
        seamCarver.removeVerticalSeam(seamCarver.findVerticalSeam());
        printPic(seamCarver.picture, seamCarver);
    }

    private static void printPic(Picture picture, SeamCarver seamCarver) {
        for (int y = 0; y < picture.height(); y++) {
            for (int x = 0; x < picture.width(); x++) {
                System.out.printf("%10d (%#7.2f)", picture.getRGB(x, y), seamCarver.energy(x, y));
            }
            System.out.println();
        }
        System.out.println();
    }

    private class SeamFinder {
        private int[] seam;

        // cache
        private boolean isHorizontal;
        private double[][] distTo;
        private int[][][] edgeTo;
        private int len, antiLen;

        // maintain distTo and edgeTo for each vertex. Relax vertex as traversing from one side to the other.
        // the min distTo on the opposite side leads to the shortest path from one side to the other.
        public void computeSeam(boolean isHorizontal) {
            this.isHorizontal = isHorizontal;
            this.distTo = new double[picture.width()][picture.height()];
            for (double[] to : this.distTo) {
                Arrays.fill(to, Double.POSITIVE_INFINITY);
            }
            this.edgeTo = new int[picture.width()][picture.height()][2]; // x y
            if (isHorizontal) {
                this.len = picture.width();
                this.antiLen = picture.height();
            } else {
                this.len = picture.height();
                this.antiLen = picture.width();
            }

            for (int antiLenPos = 0; antiLenPos < this.antiLen; antiLenPos++) {
                if (isHorizontal) {
                    this.distTo[0][antiLenPos] = energy(0, antiLenPos);
                } else {
                    this.distTo[antiLenPos][0] = energy(antiLenPos, 0);
                }
            }

            for (int lenPos = 0; lenPos < this.len - 1; lenPos++) {
                for (int antiLenPos = 0; antiLenPos < this.antiLen; antiLenPos++) {
                    if (isHorizontal) {
                        relax(lenPos, antiLenPos, lenPos + 1, antiLenPos);
                        if (antiLenPos - 1 >= 0) {
                            relax(lenPos, antiLenPos, lenPos + 1, antiLenPos - 1);
                        }
                        if (antiLenPos + 1 < this.antiLen) {
                            relax(lenPos, antiLenPos, lenPos + 1, antiLenPos + 1);
                        }
                    } else {
                        relax(antiLenPos, lenPos, antiLenPos, lenPos + 1);
                        if (antiLenPos - 1 >= 0) {
                            relax(antiLenPos, lenPos, antiLenPos - 1, lenPos + 1);
                        }
                        if (antiLenPos + 1 < this.antiLen) {
                            relax(antiLenPos, lenPos, antiLenPos + 1, lenPos + 1);
                        }
                    }
                }
            }

            int[] closestToOtherEnd = findClosestToOtherEnd();
            this.seam = new int[this.len];
            for (int lenPos = 0; lenPos < this.len; lenPos++) {
                this.seam[this.len - 1 - lenPos] = isHorizontal ?
                        closestToOtherEnd[1] : closestToOtherEnd[0]; // y : x
                closestToOtherEnd = this.edgeTo[closestToOtherEnd[0]][closestToOtherEnd[1]];
            }
        }

        private int[] findClosestToOtherEnd() {
            int[] closest = new int[2];
            double min = Double.POSITIVE_INFINITY;

            for (int antiLenPos = 0; antiLenPos < this.antiLen; antiLenPos++) {
                if (this.isHorizontal) {
                    if (this.distTo[this.len - 1][antiLenPos] < min) {
                        min = this.distTo[this.len - 1][antiLenPos];
                        closest[0] = this.len - 1;
                        closest[1] = antiLenPos;
                    }
                } else {
                    if (this.distTo[antiLenPos][this.len - 1] < min) {
                        min = this.distTo[antiLenPos][this.len - 1];
                        closest[0] = antiLenPos;
                        closest[1] = this.len - 1;
                    }
                }
            }
            return closest;
        }

        private void relax(int fromX, int fromY, int toX, int toY) {
            double length = energy(toX, toY);
            if (this.distTo[toX][toY] > this.distTo[fromX][fromY] + length) {
                this.distTo[toX][toY] = this.distTo[fromX][fromY] + length;
                edgeTo[toX][toY] = new int[]{fromX, fromY};
            }
        }

        private void printDistToArray() {
            for (int y = 0; y < this.distTo[0].length; y++) {
                for (int x = 0; x < this.distTo.length; x++) {
                    System.out.printf("%#7.2f ", this.distTo[x][y]);
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}

//    private class SeamFinder {
//        private int[] seam;
//        private Picture picture;
//        private int len, antiLen;
//        private boolean isHorizontal;
//
//        public void computeSeam(boolean isHorizontal) {
//            this.picture = SeamCarver.this.picture;
//            this.len = isHorizontal ? SeamCarver.this.width() : SeamCarver.this.height();
//            this.antiLen = isHorizontal ? SeamCarver.this.height() : SeamCarver.this.width();
//            this.seam = new int[len];
//            this.isHorizontal = isHorizontal;
//
//            EdgeWeightedDigraph pictureGraph = preparePictureDigraph();
//
//            int i = 0;
//            for (int v : getShortestSeam(pictureGraph)) {
//                this.seam[i++] = v;
//            }
//        }
//
//        private EdgeWeightedDigraph preparePictureDigraph() {
//            EdgeWeightedDigraph edgeWeightedDigraph = new EdgeWeightedDigraph(picture.width() * picture.height());
//
//            if (this.isHorizontal) {
//                for (int x = 0; x < this.picture.width() - 1; x++) {
//                    for (int y = 0; y < this.picture.height(); y++) {
//                        if (y - 1 >= 0) {
//                            DirectedEdge directedEdge = new DirectedEdge(flatIndex(x, y), flatIndex(x + 1, y - 1), energy(x + 1, y - 1));
//                            edgeWeightedDigraph.addEdge(directedEdge);
//                        }
//                        if (y + 1 < this.picture.height()) {
//                            DirectedEdge directedEdge = new DirectedEdge(flatIndex(x, y), flatIndex(x + 1, y + 1), energy(x + 1, y + 1));
//                            edgeWeightedDigraph.addEdge(directedEdge);
//                        }
//                        DirectedEdge directedEdge = new DirectedEdge(flatIndex(x, y), flatIndex(x + 1, y), energy(x + 1, y));
//                        edgeWeightedDigraph.addEdge(directedEdge);
//                    }
//                }
//            } else {
//                for (int y = 0; y < this.picture.height() - 1; y++) {
//                    for (int x = 0; x < this.picture.width(); x++) {
//                        if (x - 1 >= 0) {
//                            DirectedEdge directedEdge = new DirectedEdge(flatIndex(x, y), flatIndex(x - 1, y + 1), energy(x - 1, y + 1));
//                            edgeWeightedDigraph.addEdge(directedEdge);
//                        }
//                        if (x + 1 < this.picture.width()) {
//                            DirectedEdge directedEdge = new DirectedEdge(flatIndex(x, y), flatIndex(x + 1, y + 1), energy(x + 1, y + 1));
//                            edgeWeightedDigraph.addEdge(directedEdge);
//                        }
//                        DirectedEdge directedEdge = new DirectedEdge(flatIndex(x, y), flatIndex(x, y + 1), energy(x, y + 1));
//                        edgeWeightedDigraph.addEdge(directedEdge);
//                    }
//                }
//            }
//            return edgeWeightedDigraph;
//        }
//
//        private int flatIndex(int x, int y) {
//            return y * this.picture.width() + x;
//        }
//
//        private Iterable<Integer> getShortestSeam(EdgeWeightedDigraph pictureGraph) {
//            AcyclicSP[] shortestPaths = new AcyclicSP[this.antiLen];
//            for (int i = 0; i < shortestPaths.length; i++) {
//                int sourceVertex = isHorizontal ? flatIndex(0, i) : flatIndex(i, 0);
//                shortestPaths[i] = new AcyclicSP(pictureGraph, sourceVertex);
//            }
//
//            double shortestSeamDistance = Double.POSITIVE_INFINITY;
//            Iterable<DirectedEdge> shortestPath = null;
//            for (AcyclicSP path : shortestPaths) {
//                for (int t = 0; t < this.antiLen; t++) {
//                    int destVertex = isHorizontal ? flatIndex(len - 1, t) : flatIndex(t, len - 1);
//                    double currentDist = path.distTo(destVertex);
//                    if (shortestSeamDistance > currentDist) {
//                        shortestSeamDistance = currentDist;
//                        shortestPath = path.pathTo(destVertex);
//                    }
//                }
//            }
//
//            Queue<Integer> seamQ = new Queue<>();
//            DirectedEdge lastEdge = null;
//            for (DirectedEdge directedEdge : shortestPath) {
//                seamQ.enqueue(antiLenPos(directedEdge.from()));
//                lastEdge = directedEdge;
//            }
//            if (lastEdge != null) {
//                seamQ.enqueue(antiLenPos(lastEdge.to()));
//            }
//            return seamQ;
//        }
//
//        private int antiLenPos(int vertex) {
//            int antiLenPos;
//            if (isHorizontal) {
//                antiLenPos = vertex / this.len;
//            } else {
//                antiLenPos = vertex % this.antiLen;
//            }
//            return antiLenPos;
//        }
//    }

// Greedy
/*
private class SeamFinder {
        private int[] seam;

        // cache
        private boolean isHorizontal;
        private int len;
        private int antiLen;

        // start off from one side. The first path to reach the other side is the shortest.
        // the picture has an implicit energy 2d array.

        // can consider in topological lorder, least energy path will be stored

        // greedy algorithm does not always work, because reaching the other side at any vertex will do. Therefore a larger edge might lead to a shorter path
        // greedily choosing least energy neighbor from one side would lead to the shortest path to the other side.
        // since there is no cycle, no chance of getting stuck.
        public void computeSeam(boolean isHorizontal) {
            this.isHorizontal = isHorizontal;

            this.len = isHorizontal ? picture.width() : picture.height();
            this.antiLen = isHorizontal ? picture.height() : picture.width();

            // will hold antiLenPos
            Queue<Integer> shortestPath = shortestPathToOtherSideFrom(0);
            double minDist = pathEnergy(shortestPath);

            for (int s = 1; s < this.antiLen; s++) {
                Queue<Integer> shortestPathToOtherSide = shortestPathToOtherSideFrom(s);

                double pathDist = pathEnergy(shortestPathToOtherSide);
                if (pathDist < minDist) {
                    minDist = pathDist;
                    shortestPath = shortestPathToOtherSide;
                }
            }

            this.seam = new int[this.len];
            for (int i = 0; i < this.len; i++) {
                this.seam[i] = shortestPath.dequeue();
            }
        }

        private double pathEnergy(Queue<Integer> path) {
            double energy = 0;
            int lenPos = 0;
            for (int antiLenPos : path) {
                energy += this.isHorizontal ?
                        energy(lenPos, antiLenPos) : energy(antiLenPos, lenPos);
                lenPos++;
            }
            return energy;
        }

        private Queue<Integer> shortestPathToOtherSideFrom(int source) {
            Queue<Integer> shortestPath = new Queue<>();
            shortestPath.enqueue(source);
            for (int lenPos = 0, antiLenPos = source; lenPos < this.len - 1; lenPos++) {
                int nearestNeighbor = antiLenPos; // antiLenPos of antiLenPos
                double leastEnergy = isHorizontal ?
                        energy(lenPos + 1, antiLenPos) : energy(antiLenPos, lenPos + 1);
                if (antiLenPos - 1 >= 0) {
                    double neighbor1Energy = this.isHorizontal ?
                            energy(lenPos + 1, antiLenPos - 1) : energy(antiLenPos - 1, lenPos + 1);
                    if (neighbor1Energy < leastEnergy) {
                        leastEnergy = neighbor1Energy;
                        nearestNeighbor = antiLenPos - 1;
                    }
                }
                if (antiLenPos + 1 < this.antiLen) {
                    double neighbor2Energy = this.isHorizontal ?
                            energy(lenPos + 1, antiLenPos + 1) : energy(antiLenPos + 1, lenPos + 1);
                    if (neighbor2Energy < leastEnergy) {
                        leastEnergy = neighbor2Energy;
                        nearestNeighbor = antiLenPos + 1;
                    }
                }

                shortestPath.enqueue(nearestNeighbor);
                antiLenPos = nearestNeighbor;
            }
            return shortestPath;
        }
    }

 */

