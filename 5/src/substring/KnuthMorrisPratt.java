package substring;

public class KnuthMorrisPratt {
    private static final int R = 256;

    private String pattern;

    private int[][] dfa;

    public KnuthMorrisPratt(String pattern) {
        this.pattern = pattern;
        this.dfa = new int[R][pattern.length()];
        prepareDFA();
    }

    private void prepareDFA() {
        // match transitions
        for (int j = 0; j < this.pattern.length(); j++) {
            this.dfa[this.pattern.charAt(j)][j] = j + 1;
        }

        // mismatch transitions
        int stateX = 0;
        for (int j = 1; j < this.pattern.length(); j++) {
            for (char c = 0; c < R; c++) {
                if (c != this.pattern.charAt(j)) {
                    this.dfa[c][j] = this.dfa[c][stateX];
                }
            }
            stateX = this.dfa[this.pattern.charAt(j)][stateX];
        }
    }

    public int findPattern(String text) {
        int patterStart = -1;
        for (int i = 0, j = 0; i < text.length(); i++) {
            j = this.dfa[text.charAt(i)][j];
            if (j == this.pattern.length()) {
                patterStart = i - this.pattern.length() + 1;
                break;
            }
        }
        return patterStart;
    }

    public static void main(String[] args) {

    }
}
