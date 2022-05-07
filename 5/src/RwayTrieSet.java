import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

import java.util.Iterator;
import java.util.Scanner;

public class RwayTrieSet implements Iterable<String> {
    private static final int R = 26;

    private final Node firstNode;

    public RwayTrieSet() {
        this.firstNode = new Node(false);
    }

    public void add(String s) {
        Node current = this.firstNode;
        for (int i = 0; i < s.length(); i++) {
            if (current.successors[alphabet(s.charAt(i))] == null) {
                current.successors[alphabet(s.charAt(i))] = new Node(false);
            }
            current = current.successors[alphabet(s.charAt(i))];
        }
        current.isContained = true;
    }

    public boolean contains(String s) {
        boolean isContained = false;
        Node target = getToNode(s);
        if (target != null) {
            isContained = target.isContained;
        }
        return isContained;
    }

    private Node getToNode(String s) {
        Node current = this.firstNode;
        for (int i = 0; i < s.length() && current != null; i++) {
            current = current.successors[alphabet(s.charAt(i))];
        }
        return current;
    }

    public boolean hasAnyWordsWithPrefix(String prefix) {
        boolean result = false;
        Node target = getToNode(prefix);
        if (target != null) {
            for (int i = 0; i < R; i++) {
                if (target.successors[i] != null) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public Iterator<String> iterator() {
        Stack<String> words = new Stack<>();
        for (int i = 0; i < R; i++) {
            collect(this.firstNode.successors[i], words, Character.toString('A' + i));
        }
        return words.iterator();
    }

    private void collect(Node node, Stack<String> words, String currentWord) {
        if (node == null) {
            return;
        }

        if (node.isContained) {
            words.push(currentWord);
        }

        for (int i = 0; i < R; i++) {
            collect(node.successors[i], words, currentWord + Character.toString('A' + i));
        }
    }

    private static class Node {
        public boolean isContained;
        public Node[] successors;

        public Node(boolean isContained) {
            this.isContained = isContained;
            this.successors = new Node[R];
        }
    }

    private int alphabet(char c) {
        return c - 'A';
    }

    public static void main(String[] args) {
        RwayTrieSet trie = new RwayTrieSet();
        In in = new In("dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        for (String word : dictionary) {
            trie.add(word);
        }

        int numWords = 0;
        for (String word : trie) {
            System.out.println(word);
            numWords++;
        }
        System.out.println("Number of words:" + numWords);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Search for a word: ");
            String search = scanner.nextLine();
            System.out.println("Search result for " + search + ": " + trie.contains(search));
            System.out.println("Prefix result for " + search + ": " + trie.hasAnyWordsWithPrefix(search));
        }
    }
}
