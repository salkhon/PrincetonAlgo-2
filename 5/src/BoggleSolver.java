import edu.princeton.cs.algs4.In;

public class BoggleSolver {
    private final RwayTrieSet dictionaryTrie;

    public BoggleSolver(String[] dictionary) {
        this.dictionaryTrie = new RwayTrieSet();
        for (String word : dictionary) {
            this.dictionaryTrie.add(word);
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        RwayTrieSet validWords = new RwayTrieSet();
        boolean[][] isMarked = new boolean[board.rows()][board.cols()];

        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                isMarked[i][j] = true;
                searchBoggle(i, j, board, getBoardLetter(board, i, j), validWords, isMarked);
                isMarked[i][j] = false;
                // can save the words starting with a character under that character. Then if a latter
                // character DFS reaches the saved characters, we can just add the words formed
                // to the currentWord, without searching further.
            }
        }
        return validWords;
    }

    private void searchBoggle(int i, int j, BoggleBoard board, String currentWord, RwayTrieSet validWords, boolean[][] isMarked) {
        // the boggle board might be considered as a graph, with positions as vertices and adjacents as edges.
        // For each board we do a DFS for all vertices. (all words possible starting with a character)

        if (currentWord.length() >= 3 && this.dictionaryTrie.contains(currentWord)) {
            validWords.add(currentWord);
        }

        if (currentWord.length() >= 3 && !this.dictionaryTrie.hasAnyWordsWithPrefix(currentWord)) {
            return;
        }

        // further search
        for (int i1 = i - 1; i1 <= i + 1; i1++) {
            for (int j1 = j - 1; j1 <= j + 1; j1++) {
                if (i1 >= 0 && i1 < board.rows() && j1 >= 0 && j1 < board.cols() && !isMarked[i1][j1]) {
                    isMarked[i1][j1] = true;
                    searchBoggle(i1, j1, board, currentWord + getBoardLetter(board, i1, j1), validWords, isMarked);
                    isMarked[i1][j1] = false;
                }
            }
        }
    }

    private String getBoardLetter(BoggleBoard board, int i, int j) {
        String nextLetter = Character.toString(board.getLetter(i, j));
        if (nextLetter.equals("Q")) {
            nextLetter += "U";
        }
        return nextLetter;
    }

    public int scoreOf(String word) {
        int score = 0;
        if (this.dictionaryTrie.contains(word)) {
            switch (word.length()) {
                case 0:
                case 1:
                case 2:
                    break;
                case 3:
                case 4:
                    score = 1;
                    break;
                case 5:
                    score = 2;
                    break;
                case 6:
                    score = 3;
                    break;
                case 7:
                    score = 5;
                    break;
                default:
                    score = 11;
            }
        }
        return score;
    }

    public static void main(String[] args) {
        String dictionaryFilename = "dictionary-algs4.txt";
        String boardFilename = "board-q.txt";
        In in = new In(dictionaryFilename);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(boardFilename);

        int score = 0;
        int entries = 0;
        for (String word : solver.getAllValidWords(board)) {
            System.out.println(word);
            score += solver.scoreOf(word);
            entries++;
        }
        System.out.println("Score = " + score);
        System.out.println("Entries = " + entries);
    }
}
