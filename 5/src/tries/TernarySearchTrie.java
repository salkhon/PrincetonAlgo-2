package tries;

import edu.princeton.cs.algs4.Queue;

public class TernarySearchTrie<T> {
    private Node root;

    public TernarySearchTrie() {

    }

    public T get(String keyString) {
        int charOffset = 0;
        char keyChar;
        T targetVal = null;
        Node current = this.root;
        while (true) {
            keyChar = keyString.charAt(charOffset);
            if (keyChar < keyString.charAt(charOffset)) {
                if (current.left == null) {
                    break;
                }
                current = current.left;
            } else if (keyChar > keyString.charAt(charOffset)) {
                if (current.right == null) {
                    break;
                }
                current = current.right;
            } else if (charOffset < keyString.length() - 1) {
                if (current.mid == null) {
                    break;
                }
                current = current.mid;
                charOffset++;
            } else {
                targetVal = current.val;
                break;
            }
        }
        return targetVal;
    }

    public void put(String keyString, T val) {
        int charOffset = 0;
        if (this.root == null) {
            this.root = new Node(keyString.charAt(charOffset), null);
        }

        Node current = this.root;
        char keyChar;
        while (charOffset < keyString.length() - 1) {
            keyChar = keyString.charAt(charOffset);
            if (keyChar == current.c) {
                if (current.mid == null) {
                    current.mid = new Node(keyString.charAt(charOffset + 1), null);
                }
                current = current.mid;
                charOffset++;
            } else if (keyChar < current.c) {
                if (current.left == null) {
                    current.left = new Node(keyString.charAt(charOffset + 1), null);
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new Node(keyString.charAt(charOffset + 1), null);
                }
                current = current.right;
            }
        }

        current.val = val;
    }

    public Iterable<String> keys() {
        Queue<String> orderedStrings = new Queue<>();
        keys(this.root, "", orderedStrings);
        return orderedStrings;
    }

    private void keys(Node current, String prefix, Queue<String> orderedStrings) {
        if (current == null) {
            return;
        }

        keys(current.left, prefix, orderedStrings);

        if (current.val != null) {
            orderedStrings.enqueue(prefix);
        }

        keys(current.mid, prefix + current.c, orderedStrings);
        keys(current.right, prefix, orderedStrings);
    }

    public Iterable<String> keysWithPrefix(String prefix) {
        Node current = this.root;
        int charOffset = 0;
        while (charOffset < prefix.length()) {
            char keyChar = prefix.charAt(charOffset);
            if (current == null) {
                break;
            }

            if (keyChar < current.c) {
                current = current.left;
            } else if (keyChar > current.c) {
                current = current.right;
            } else if (charOffset < prefix.length() - 1) {
                current = current.mid;
                charOffset++;
            } else {
                charOffset++; // break;
            }
        }

        Queue<String> stringQueue = new Queue<>();
        if (current != null) {
            if (current.val != null) {
                stringQueue.enqueue(prefix);
            }

            keys(current.mid, prefix, stringQueue);
        }
        return stringQueue;
    }

    public String longestPrefixOf(String keyString) {
        int longestPrefixLen = 0;
        Node current = this.root;
        int charOffset = 0;
        while (current != null) {
            char keyChar = keyString.charAt(charOffset);
            if (keyChar < current.c) {
                current = current.left;
            } else if (keyChar > current.c) {
                current = current.right;
            } else {
                charOffset++;
                if (current.val != null) {
                    longestPrefixLen = charOffset;
                }
                current = current.mid;
            }
        }
        return keyString.substring(longestPrefixLen);
    }

    private class Node {
        public char c;
        public T val;
        public Node left, mid, right;

        public Node(char c, T val) {
            this.c = c;
            this.val = val;
        }
    }

    public static void main(String[] args) {

    }
}
