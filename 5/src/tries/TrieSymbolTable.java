package tries;

public class TrieSymbolTable<T> {
    private static final int RADIX = 256;
    private Node<T> root;

    public TrieSymbolTable() {
        this.root = new Node<>(null);
    }

    public T get(String keyString) {
        T targetVal;
        Node<T> current = this.root;
        int i;
        for (i = 0; i < keyString.length(); i++) {
            Node<T> childTrie = current.next[keyString.charAt(i)];
            if (childTrie != null) {
                current = childTrie;
            } else {
                break;
            }
        }

        if (i == keyString.length() && current.val != null) {
            targetVal = current.val;
        } else {
            targetVal = null;
        }

        return targetVal;
    }

    public void put(String keyString, T val) {
        Node<T> current = this.root;
        for (int i = 0; i < keyString.length(); i++) {
            if (current.next[keyString.charAt(i)] == null) {
                current.next[keyString.charAt(i)] = new Node<>(null);
            }
            current = current.next[keyString.charAt(i)];
        }
        current.val = val;
    }

    // has to be recursive to delete the nodes themselves.
    public void delete(String keyString) {
        Node<T> current = this.root;
        int i;
        for (i = 0; i < keyString.length(); i++) {
            Node<T> childTrie = current.next[keyString.charAt(i)];
            if (childTrie == null) {
                break;
            }
            current = childTrie;
        }
        if (i == keyString.length() && current.val != null) {
            current.val = null;
        }
    }

    public T floor(String keyString) {
        T floor = null;
        Node<T> floorNode = floorNode(this.root, keyString, 0);
        if (floorNode != null) {
            floor = floorNode.val;
        }

        return floor;
    }

    private Node<T> floorNode(Node<T> current, String key, int d) {
        if (current == null) {
            return null;
        }

        Node<T> floorNode = null;

        if (d > -1) {
            // still on equal search
            if (d == key.length() && current.val != null) {
                floorNode = current; // all key chars have been selected and val exists at the key
            } else if (d == key.length()) {
                return null;
            } else if (d < key.length()) {
                floorNode = floorNode(current.next[key.charAt(d)], key, d + 1); // char d is selected
            }
        }

        if (floorNode == null) {
            // equal not found, or any key below that is lesser.
            int c = (d > -1 ?
                    key.charAt(d) : RADIX) - 1; // if this search is on equal key so far, then a larger char appended to this
                                                // will lead to a larger key, not the floor.
            for (; c >= 0 && floorNode == null; c--) {
                floorNode = floorNode(current.next[c], key, -1);
            }
        }

        if (floorNode == null && current.val != null) {
            floorNode = current;
            // the keys below current are closer, so if no key below current is found,
            // then only current is the floor.
        }

        return floorNode;
    }

    // non generic classes defined inside a generic container class, are considered as if they were generic. So another generic parameter had to be introduced.
    private static class Node<V> {
        public V val;
        public Node<V>[] next;

        @SuppressWarnings("unchecked")
        public Node(V val) {
            this.val = val;
            this.next = (Node<V>[]) new Node[RADIX];
        }
    }

    public static void main(String[] args) {
        TrieSymbolTable<Integer> trieSymbolTable = new TrieSymbolTable<>();
        trieSymbolTable.put("Sal", 1);
        trieSymbolTable.put("Mike", 2);
        trieSymbolTable.put("Eleven", 11);
        trieSymbolTable.put("One", 1);
        trieSymbolTable.put("Two", 2);
        trieSymbolTable.put("Three", 3);
        trieSymbolTable.put("Four", 4);
        trieSymbolTable.put("a", 1);
        trieSymbolTable.put("aa", 11);
        trieSymbolTable.put("aaa", 111);


        System.out.println("Eleven: " + trieSymbolTable.get("Eleven"));
        System.out.println("Two: " + trieSymbolTable.get("Two"));
        System.out.println("Mike: " + trieSymbolTable.get("Mike"));
        System.out.println("Four: " + trieSymbolTable.get("Four"));
        System.out.println("FourRE: " + trieSymbolTable.get("FourRE"));

        trieSymbolTable.put("FourRE", 99);

        System.out.println("FourRE: " + trieSymbolTable.get("FourRE"));

        trieSymbolTable.delete("FourRE");

        System.out.println("FourRE: " + trieSymbolTable.get("FourRE"));

        System.out.println("Floor of Two: " + trieSymbolTable.floor("Two"));
        System.out.println("Floor of Twozz: " + trieSymbolTable.floor("Twozz"));
        System.out.println("Floor of Twe: " + trieSymbolTable.floor("Twe"));
        System.out.println("Floor of aaaa: " + trieSymbolTable.floor("aaaa"));
        System.out.println("Floor of aaa: " + trieSymbolTable.floor("aaa"));
    }
}
