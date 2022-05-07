package radixsorts;

public class ThreeWayStringQuickSort {

    // 3 way partition cache
    private int lessPtr, greaterPtr;

    public void sort(String[] strings) {
        sort(strings, 0, strings.length, 0);
    }

    private void sort(String[] strings, int lo, int hi, int charIndex) {
        if (hi - lo <= 1) {
            return;
        }

        // three way partition
        int lessPtr = lo + 1, greaterPtr = hi - 1;
        for (int i = lo + 1; i < hi && i <= greaterPtr; i++) {
            if (compareTo(strings[lo], strings[i], charIndex) < 0) {
                exchange(i, greaterPtr--, strings);
                i--; // redo this pos
            } else if (compareTo(strings[lo], strings[i], charIndex) > 0) {
                exchange(i, lessPtr++, strings);
            }
        }
        exchange(lo, lessPtr, strings);

        sort(strings, lo, lessPtr, charIndex);
        if (charIndex < strings[lo].length()) {
            sort(strings, lessPtr, greaterPtr + 1, charIndex + 1);
        }
        sort(strings, greaterPtr + 1, hi, charIndex);
    }

    private int compareTo(String s1, String s2, int charIndex) {
        int comparison;
        if (charIndex < s1.length() && charIndex < s2.length()) {
            comparison = Character.compare(s1.charAt(charIndex), s2.charAt(charIndex));
        } else if (charIndex >= s1.length() && charIndex >= s2.length()) {
            comparison = 0;
        } else if (charIndex >= s1.length()) {
            comparison = -1;
        } else {
            comparison = 1;
        }
        return comparison;
    }

    private void exchange(int index1, int index2, String[] strings) {
        String temp = strings[index1];
        strings[index1] = strings[index2];
        strings[index2] = temp;
    }

    private int charAt(int index, String string) {
        return index < string.length() ?
                string.charAt(index) : -1;
    }
}
