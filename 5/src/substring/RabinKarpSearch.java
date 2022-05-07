package substring;

public class RabinKarpSearch {
    private static final int Q = 997;
    private static final int R = 256;

    private String pattern;
    private int patternHash;
    private int RM;

    public RabinKarpSearch(String pattern) {
        this.pattern = pattern;
        this.patternHash = hornerHash(pattern);

        this.RM = 1;
        for (int i = 1; i < this.pattern.length(); i++) {
            this.RM *= R;
            this.RM %= Q;
        }
    }

    private int hornerHash(String s) {
        int hash = 0;
        for (int i = 0; i < s.length(); i++) {
            hash = (hash * R + s.charAt(i)) % Q;
        }
        return hash;
    }

    public int search(String text) {
        int start = -1;
        for (int i = 0, hash = hornerHash(text.substring(0, this.pattern.length()));
             i < text.length() - this.pattern.length();
             i++, hash = translateHash(text.charAt(i), text.charAt(i + this.pattern.length() - 1), hash)) {
            if (hash == this.patternHash && text.startsWith(this.pattern, i)) {
                start = i;
                break;
            }
        }
        return start;
    }

    private int translateHash(char excludeChar, char includeChar, int prevHash) {
        int hash = prevHash;
        hash = (hash + Q - (excludeChar * this.RM) % Q) % Q;
        hash = (hash * R + includeChar) % Q;
        return hash;
    }

    public static void main(String[] args) {

    }
}
