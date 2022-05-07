import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        String outcast = "";
        int maxDistance = 0;

        for (String noun : nouns) {
            int dist = 0;
            for (String nounT : nouns) {
                if (!noun.equals(nounT)) {
                    dist += this.wordNet.distance(noun, nounT);
//                    System.out.println("Common ancestor for " + noun + ", " + nounT + " : " + this.wordNet.sap(noun, nounT));
                }
            }
//            System.out.println("Distance for " + noun + " : " + dist);
            if (dist > maxDistance) {
                maxDistance = dist;
                outcast = noun;
            }
        }

        return outcast;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
        Outcast outcast = new Outcast(wordnet);
        In in = new In("outcast11.txt");
        String[] nouns = in.readAllStrings();
        StdOut.println("outcast11.txt" + ": " + outcast.outcast(nouns));
    }
}
