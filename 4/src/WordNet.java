import edu.princeton.cs.algs4.ResizingArrayBag;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.Digraph;

public class WordNet {
    private Digraph digraph;
    private ST<String, ResizingArrayBag<Integer>> wordIdMap;
    private String[][] idWordMap;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        checkForNull(synsets, hypernyms);
        buildSymbolDigraph(synsets, hypernyms);
        this.sap = new SAP(this.digraph);
    }

    private void buildSymbolDigraph(String synsets, String hypernyms) {
        In synsetIn = new In(synsets);
        String[] synsetLines = synsetIn.readAllLines();

        this.digraph = new Digraph(synsetLines.length);
        this.wordIdMap = new ST<>();
        this.idWordMap = new String[this.digraph.V()][];

        for (String synsetLine : synsetLines) {
            String[] lineSplits = synsetLine.split(",");
            String[] words = lineSplits[1].split(" ");
            int synId = Integer.parseInt(lineSplits[0]);
            this.idWordMap[synId] = words;
            for (String word : words) {
                if (!this.wordIdMap.contains(word)) {
                    this.wordIdMap.put(word, new ResizingArrayBag<>());
                }
                this.wordIdMap.get(word).add(synId);
            }
        }

        In hypernymsIn = new In(hypernyms);
        String[] hypernymLines = hypernymsIn.readAllLines();

        for (String line : hypernymLines) {
            String[] lineSplits = line.split(",");
            for (int i = 1; i < lineSplits.length; i++) {
                this.digraph.addEdge(Integer.parseInt(lineSplits[0]), Integer.parseInt(lineSplits[i]));
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return this.wordIdMap.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        checkForNull(word);
        return this.wordIdMap.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        checkForNull(nounA, nounB);
        if (this.wordIdMap.contains(nounA) && this.wordIdMap.contains(nounB)) {
            // same word can have different meaning and different synId.
            // returns iterable, thus finds the closest ancestor between two iterables
            return this.sap.length(this.wordIdMap.get(nounA), this.wordIdMap.get(nounB));
        } else {
            throw new IllegalArgumentException();
        }
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        checkForNull(nounA, nounB);
        StringBuilder ancestorSynset = new StringBuilder();
        int ancestorId = this.sap.ancestor(this.wordIdMap.get(nounA), this.wordIdMap.get(nounB));
        for (int i = 0; i < this.idWordMap[ancestorId].length; i++) {
            ancestorSynset.append(this.idWordMap[ancestorId][i]).append(" ");
//            if (i < this.idWordMap[ancestorId].length - 1) {
//                ancestorSynset.append(" ");
//            }
        }
        return ancestorSynset.toString().trim();
    }

    private void checkForNull(String... strings) {
        for (String string : strings) {
            if (string == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("synsets.txt", "hypernyms.txt");
        while (!StdIn.isEmpty()) {
            String wordA = StdIn.readString();
            String wordB = StdIn.readString();
            System.out.println("Distance: " + wordNet.distance(wordA, wordB));
        }
    }
}