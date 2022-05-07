package radixsorts;

public class MSDRadixSort {
    private final int RADIX;

    public MSDRadixSort() {
        this.RADIX = 256;
    }

    public void sort(String[] strings) {
        sort(strings, 0, strings.length, 0);
    }

    private void sort(String[] strings, int lo, int hi, int msdIndex) {
        if (hi - lo <= 1) {
            return;
        }

        int[] freq = new int[this.RADIX + 1];

        for (int i = lo; i < hi; i++) {
            freq[charAt(msdIndex, strings[i]) + 1]++;
        }

        for (int r = 0; r < this.RADIX; r++) {
            freq[r + 1] += freq[r];
        }

        String[] sortedStrings = new String[hi - lo];

        int emptyCharPos = 0;
        for (int i = lo; i < hi; i++) {
            int charAtMSDIndex = charAt(msdIndex, strings[i]);
            if (charAtMSDIndex == -1) {
                sortedStrings[emptyCharPos++] = strings[i];
            } else {
                sortedStrings[freq[charAtMSDIndex]++] = strings[i];
            }
        }

        System.arraycopy(sortedStrings, 0, strings, lo, hi - lo);

        System.out.println();
        System.out.println("lo: " + lo + "  hi: " + hi + "  msdIdx: " + msdIndex);
        for (String s : strings) {
            System.out.println(s);
        }
        System.out.println();

        int start = emptyCharPos;
        for (int r = 0; r < this.RADIX; r++) {
            sort(strings, start + lo, freq[r] + lo, msdIndex + 1);
            start = freq[r];
        }
    }

    private static int charAt(int index, String s) {
        return index < s.length() ?
                s.charAt(index) : -1; // freq[0] will be incremented when empty char
    }

    public static void main(String[] args) {
        String[] strings = {"dab", "add", "cab", "fa", "fe", "bad", "dad", "be", "fed", "bed", "ebb", "ace"};
        MSDRadixSort msdRadixSort = new MSDRadixSort();
        msdRadixSort.sort(strings);
        for (String s : strings) {
            System.out.println(s);
        }
    }
}
