package radixsorts;

// modified to support different length strings
public class LSDRadixSort {
    // key index counting sort cache
    private final int radix;

    public LSDRadixSort() {
        this.radix = 256;
    }

    public void sort(String[] strings) {
        int maxStrLen = strings[0].length();
        for (String s : strings) {
            maxStrLen = Math.max(maxStrLen, s.length());
        }

        for (int i = maxStrLen - 1; i >= 0; i--) {
            keyIndexCountingSort(strings, i);
        }
    }

    private void keyIndexCountingSort(String[] strings, int index) {
        int[] countOf = new int[this.radix + 2]; // 1 for cumulates, 1 for non existing char (considering them as less)
        String[] sortedStrings = new String[strings.length];

        // counting frequencies
        for (String s : strings) {
            countOf[countArrayIndexOfCharAt(index, s) + 1]++; // frequencies translated by 1 for easier accumulation
        }

        // cumulates of frequencies
        for (int r = 0; r < this.radix; r++) {
            countOf[r + 1] += countOf[r];
        }

        // at the end of computing cumulates, countOf array becomes position array

        // placing keys on position
        for (String s : strings) {
            sortedStrings[countOf[countArrayIndexOfCharAt(index, s)]++] = s;
            // position of a char, is the ending position of prev char (cum freq of prev char, stored at current char's position)
        }

        // replacing string with sorted order
        System.arraycopy(sortedStrings, 0, strings, 0, strings.length);
    }

    private int countArrayIndexOfCharAt(int index, String str) {
        return index < str.length() ?
                str.charAt(index) + 1 : 0;
    }

    public static void main(String[] args) {
        String[] strings = {"dab", "add", "cab", "fa", "fe", "bad", "dad", "be", "fed", "bed", "ebb", "ace"};
        LSDRadixSort lsdRadixSort = new LSDRadixSort();
        lsdRadixSort.sort(strings);
        for (String s : strings) {
            System.out.println(s);
        }
    }
}
/*
* RAW
* for (int i = 0; i < strings.length(); i++) {
*   countOf[strings[i].chatAt(i) + 1]++;
* }
*
* for (int r = 0; r < countOf.length(); r++) {
*   countOf[r + 1] += countOf[r];
* }
*
* for (int i = 0; i < strings.length; i++) {
*   aux[countOf[strings[i].charAt(lsdIndex)]++] = strings[i];
* }
*
* for (int i = 0; i < strings.length; i++) {
*   aux[i] = strings[i];
* }
* */
