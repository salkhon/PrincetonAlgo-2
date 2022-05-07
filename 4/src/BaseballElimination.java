import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    private final String[] teamWithId;
    private final RedBlackBST<String, Integer> idOfTeam;

    private final int[] w, l, r;
    private final int[][] g;

    private final Bag<String>[] teamEliminationSubset;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        int numTeams = in.readInt();
        in.readLine();

        this.teamWithId = new String[numTeams];
        this.idOfTeam = new RedBlackBST<>();

        this.w = new int[numTeams];
        this.l = new int[numTeams];
        this.r = new int[numTeams];
        this.g = new int[numTeams][numTeams];

        populateFields(in);

        this.teamEliminationSubset = (Bag<String>[]) new Bag[numTeams];
        for (int i = 0; i < this.w.length; i++) {
            this.teamEliminationSubset[i] = new Bag<>();
        }

        for (int i = 0; i < this.w.length; i++) {
            checkElimination(i);
        }
    }

    private void populateFields(In in) {
        int id = 0;
        while (in.hasNextLine()) {
            this.teamWithId[id] = in.readString();
            this.idOfTeam.put(this.teamWithId[id], id);
            this.w[id] = in.readInt();
            this.l[id] = in.readInt();
            this.r[id] = in.readInt();

            for (int j = 0; j < this.w.length; j++) {
                this.g[id][j] = in.readInt();
            }

            id++;

            in.readLine();
        }
    }

    private void checkElimination(int id) {
        if (isTriviallyEliminated(id)) {
            return;
        }

        FlowNetwork flowNetwork = networkFor(id);

        final int numTeamVertex = this.w.length;
        final int numGameVertex = numTeamVertex * (numTeamVertex - 1) / 2;
        final int source = numGameVertex + numTeamVertex;
        final int target = source + 1;

        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, source, target);

        int sourceCapacity = 0;
        for (FlowEdge edge : flowNetwork.adj(source)) {
            sourceCapacity += edge.capacity();
        }

        if (fordFulkerson.value() != sourceCapacity) {
            // team id can't win
            for (int i = 0; i < numTeamVertex; i++) {
                if (i != id && fordFulkerson.inCut(numGameVertex + i)) {
                    this.teamEliminationSubset[id].add(this.teamWithId[i]);
                }
            }
        }
    }

    private boolean isTriviallyEliminated(int id) {
        boolean isEliminated = false;
        for (int v = 0; v < this.w.length; v++) {
            if (this.w[id] + this.r[id] < this.w[v]) {
                isEliminated = true;
                this.teamEliminationSubset[id].add(this.teamWithId[v]);
            }
        }

        return isEliminated;
    }

    private FlowNetwork networkFor(int id) {
        final int numTeamVertex = this.w.length;
        final int numGameVertex = numTeamVertex * (numTeamVertex - 1) / 2;

        final int source = numGameVertex + numTeamVertex;
        final int target = source + 1;
        // game vertices are in the sequence encountered in the upper triangular matrix
        // s and t are at the end

        FlowNetwork flowNetwork = new FlowNetwork(numGameVertex + numTeamVertex + 2);

        int gameId = 0;
        for (int i = 0; i < numTeamVertex; i++) {
            for (int j = i + 1; j < numTeamVertex; j++, gameId++) {
                if (i == id || j == id) {
                    continue;
                }

                flowNetwork.addEdge(new FlowEdge(source, gameId, this.g[i][j]));

                flowNetwork.addEdge(new FlowEdge(gameId, i + numGameVertex, Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(gameId, j + numGameVertex, Double.POSITIVE_INFINITY));
            }
        }

        for (int v = 0; v < numTeamVertex; v++) {
            if (v != id) {
                flowNetwork.addEdge(new FlowEdge(v + numGameVertex, target,
                        this.w[id] + this.r[id] - this.w[v]));
            }
        }

        return flowNetwork;
    }

    public int numberOfTeams() {
        return this.w.length;
    }

    public Iterable<String> teams() {
        Queue<String> teams = new Queue<>();
        for (String team : this.teamWithId) {
            teams.enqueue(team);
        }
        return teams;
    }

    public int wins(String team) {
        return this.w[idOfTeam.get(team)];
    }

    public int losses(String team) {
        return this.l[idOfTeam.get(team)];
    }

    public int remaining(String team) {
        return this.r[idOfTeam.get(team)];
    }

    public int against(String team1, String team2) {
        return this.g[idOfTeam.get(team1)][idOfTeam.get(team2)];
    }

    public boolean isEliminated(String team) {
        return !this.teamEliminationSubset[this.idOfTeam.get(team)].isEmpty();
    }

    public Iterable<String> certificateOfElimination(String team) {
        return this.teamEliminationSubset[this.idOfTeam.get(team)];
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination("teams12.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
