import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class BoggleTrie<T> {
    private static final int R = 26; // A-Z
    private Node root;

    private static class Node {
        private Object value;
        private Node[] next = new Node[R];
    }

    public void put(String key, T value) {
        if (key == null)
            throw new IllegalArgumentException("Key cannot be null.");

        root = put(root, key, value, 0);
    }

    private Node put(Node node, String key, T value, int depth) {
        if (node == null)
            node = new Node();

        if (depth == key.length()) {
            node.value = value;
            return node;
        }

        char c = key.charAt(depth);
        node.next[c - 'A'] = put(node.next[c - 'A'], key, value, depth + 1);
        return node;
    }

    public boolean contains(String key) {
        if (key == null)
            throw new IllegalArgumentException("Key cannot be null.");

        Node node = get(root, key, 0);
        return node != null && node.value != null;
    }

    public T get(String key) {
        if (key == null)
            throw new IllegalArgumentException("Key cannot be null.");

        Node node = get(root, key, 0);
        return node == null ? null : (T) node.value;
    }

    private Node get(Node node, String key, int depth) {
        if (node == null)
            return null;

        if (depth == key.length())
            return node;

        char c = key.charAt(depth);
        return get(node.next[c - 'A'], key, depth + 1);
    }

    public boolean isValidPrefix(String prefix) {
        if (prefix == null)
            throw new IllegalArgumentException("Prefix cannot be null.");

        Node node = get(root, prefix, 0);
        return node != null;
    }

    public static void main(String[] args) {
        // Unit Testing
        BoggleTrie<Integer> trie = new BoggleTrie<>();
        trie.put("A", 1);
        trie.put("AN", 2);
        trie.put("ANT", 3);
        trie.put("ANTCOLONY", 42);
        trie.put("B", 4);

        assert trie.contains("A") : "Expected trie to contain key 'A'.";
        assert trie.contains("AN") : "Expected trie to contain key 'AN'.";
        assert trie.contains("ANT") : "Expected trie to contain key 'ANT'.";
        assert trie.contains("B") : "Expected trie to contain key 'B'.";
        assert !trie.contains("C") : "Expected trie to not contain key 'C'.";

        assert trie.get("A") == 1 : "Expected value for key 'A' to be 1.";
        assert trie.get("AN") == 2 : "Expected value for key 'AN' to be 2.";
        assert trie.get("ANT") == 3 : "Expected value for key 'ANT' to be 3.";
        assert trie.get("ANTCOLONY") == 42 : "Expected value for key 'ANTCOLONY' to be 42.";
        assert trie.get("B") == 4 : "Expected value for key 'B' to be 4.";

        assert trie.isValidPrefix("A") : "Expected 'A' to be a valid prefix.";
        assert trie.isValidPrefix("AN") : "Expected 'AN' to be a valid prefix.";
        assert trie.isValidPrefix("ANT") : "Expected 'ANT' to be a valid prefix.";
        assert trie.isValidPrefix("ANTCOL") : "Expected 'ANTCOL' to be a valid prefix.";
        assert trie.isValidPrefix("ANTCOLONY") : "Expected 'ANTCOLONY' to be a valid prefix.";
        assert trie.isValidPrefix("B") : "Expected 'B' to be a valid prefix.";
        assert !trie.isValidPrefix("C") : "Expected 'C' to not be a valid prefix.";

        StdOut.println("All tests passed.");
    }
}
