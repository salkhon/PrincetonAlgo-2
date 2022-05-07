package radixsorts.book;

import java.util.Arrays;

public class Alphabet {
    // EXTENDED ASCII 256
    private static final int NUM_CHAR = 256;

    private int[] idxInStr;
    private String s;

    public Alphabet(String s) {
        this.s = s;
        this.idxInStr = new int[NUM_CHAR];
        Arrays.fill(this.idxInStr, -1);
        for (int i = 0; i < s.length(); i++) {
            this.idxInStr[s.charAt(i)] = i; // uniqueness assumed
        }
    }

    public char toChar(int index) {
        return this.s.charAt(index);
    }

    public int toIndex(char c) {
        return this.idxInStr[c];
    }

    public boolean contains(char c) {
        return this.idxInStr[c] != -1;
    }

    public int R() {
        return this.s.length();
    }

    public int lgR() {
        int numBits = 0;
        int len = this.s.length();
        while (len > 0) {
            len >>= 1;
            numBits++;
        }
        return numBits;
    }

    public int[] toIndices(String s) {
        int[] indices = new int[this.s.length()];
        for (int i = 0; i < s.length(); i++) {
            indices[i] = this.idxInStr[s.charAt(i)];
        }
        return indices;
    }

    public String toChars(int[] indices) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int index : indices) {
            stringBuilder.append(this.s.charAt(index));
        }
        return stringBuilder.toString();
    }
}
