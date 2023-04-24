import java.util.HashMap;

public class BoggleTrie<T> {
    private static final int R = 26; // A-Z
    private Node root;
    private HashMap<String, T> map = new HashMap<>();

    private static class Node {
        private Object value;
        private Node[] next = new Node[R];
    }

}
