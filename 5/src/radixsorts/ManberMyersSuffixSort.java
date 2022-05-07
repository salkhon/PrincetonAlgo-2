package radixsorts;

// 1. Two pass MSD radix for two most significant digits. (First assume maxLen >= 2)
// 2.

public class ManberMyersSuffixSort {
    private static final int RADIX = 256;
    private String s;
    private int[] orderedSuffixIndex; // order -> suffix
    private int[] suffixOrder; // suffix -> order

    // cache
    private int offset;

    public ManberMyersSuffixSort(String s) {
        this.s = s;

        this.orderedSuffixIndex = new int[this.s.length()];
        this.suffixOrder = new int[this.s.length()];

        for (int i = 0; i < this.s.length(); i++) {
            this.orderedSuffixIndex[i] = this.suffixOrder[i] = i;
        }

        MSDRadixSort(2);
        this.offset = 2;
    }

    private void MSDRadixSort(int offsetLim) {
        int[] aux = new int[orderedSuffixIndex.length];
        MSDRadixSort(0, this.s.length(), aux, 0, offsetLim);
    }

    private void MSDRadixSort(int lo, int hi, int[] aux, int offset, int offsetLim) {
        if (hi - lo <= 1) {
            System.out.println(lo + " " + hi + " " + offset + " rejected");
            return;
        }
        System.out.println(lo + " " + hi + " " + offset);

        this.offset = offset;

        // ordering the suffix indices in offset pos char
        int[] count = new int[RADIX + 2];
        for (int i = lo; i < hi; i++) {
            count[charAt(i) + 2]++;
        }

        for (int i = 0; i < RADIX + 1; i++) {
            count[i + 1] += count[i];
        }

        for (int i = lo; i < hi; i++) {
            aux[count[charAt(i) + 1]++] = this.orderedSuffixIndex[i];
        }

        System.arraycopy(aux, 0, this.orderedSuffixIndex, lo, hi - lo);

        for (int i = 0; i < RADIX && offset < offsetLim - 1; i++) {
            if (count[i + 1] != count[i]) {
                MSDRadixSort(count[i], count[i + 1], aux, offset + 1, offsetLim);
            }
        }
    }

    private int charAt(int i) {
        return this.orderedSuffixIndex[i] + this.offset < this.s.length() ?
                this.s.charAt(this.orderedSuffixIndex[i] + this.offset) : -1;
    }


    //test client
    public static void main(String[] args) {
        String test = "mississippi";
        ManberMyersSuffixSort myersSuffixSort = new ManberMyersSuffixSort(test);
        for (int i : myersSuffixSort.orderedSuffixIndex) {
            System.out.println(test.substring(i));
        }
    }
}
