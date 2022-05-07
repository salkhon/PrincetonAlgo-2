package radixsorts;

import edu.princeton.cs.algs4.Quick3string;

public class LongestRepeatingSubStr {

    public String LRS(String s) {
        String[] suffixStrings = new String[s.length()];
        for (int i = 0; i < suffixStrings.length; i++) {
            suffixStrings[i] = s.substring(i);
        }

        Quick3string.sort(suffixStrings);

        String longestRepeatingSubstr = "";
        for (int i = 1; i < suffixStrings.length; i++) {
            int currMatch = prefixMatch(suffixStrings[i], suffixStrings[i - 1]);
            if (currMatch > longestRepeatingSubstr.length()) {
                longestRepeatingSubstr = suffixStrings[i - 1].substring(0, currMatch);
            }
        }
        return longestRepeatingSubstr;
    }

    private int prefixMatch(String s1, String s2) {
        if (s2.length() < s1.length()) {
            String temp = s1;
            s1 = s2;
            s2 = temp;
        }

        int matchCount = 0;
        while (matchCount < s1.length() && s1.charAt(matchCount) == s2.charAt(matchCount)) {
            matchCount++;
        }

        return matchCount;
    }
}
