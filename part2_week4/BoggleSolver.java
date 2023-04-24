import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TrieSET;
import edu.princeton.cs.algs4.TrieST;

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {

    private final TrieST<Integer> dictionaryTrie;
    private final TrieSET prefixTrie;
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null)
            throw new IllegalArgumentException("Dictionary cannot be null.");

        this.dictionaryTrie = new TrieST<>();
        this.prefixTrie = new TrieSET();
        parseDictionary(dictionary);
    }

    private void parseDictionary(String[] dict) {
        for (String word : dict) {
            if (word.length() >= 3) {
                this.dictionaryTrie.put(word, calculateScore(word));
                addPrefixes(word);
            }
        }
    }

    private void addPrefixes(String word) {
        for (int i = 0; i < word.length(); i++)
            this.prefixTrie.add(word.substring(0, i + 1));
    }

    private int calculateScore(String word) {
        int length = word.length();

        if (length <= 2)
            return 0;
        if (length <= 4)
            return 1;
        if (length == 5)
            return 2;
        if (length == 6)
            return 3;
        if (length == 7)
            return 5;
        return 11;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null)
            throw new IllegalArgumentException("Board cannot be null.");

        return searchBoard(board);
    }

    private Iterable<String> searchBoard(BoggleBoard board) {
        TrieST<Integer> results = new TrieST<>();
        for (int row = 0; row < board.rows(); row++)
            for (int column = 0; column < board.cols(); column++) {
                boolean[][] visited = new boolean[board.rows()][board.cols()];
                dfs(row, column, "", visited, board, results);
            }

        return results.keys();
    }

    private void dfs(int row, int column, String currentWord, boolean[][] visited, BoggleBoard board, TrieST<Integer> results) {
        char letter = board.getLetter(row, column);
        visited[row][column] = true;

        currentWord += letter == 'Q' ? "QU" : letter;

        if (this.dictionaryTrie.contains(currentWord))
            results.put(currentWord, this.dictionaryTrie.get(currentWord));

        // TODO This is inefficient
        if (!this.prefixTrie.contains(currentWord)) return;

        for (int[] neighbor : getNeighbors(row, column, board)) {
            int nextRow = neighbor[0];
            int nextCol = neighbor[1];

            if (visited[nextRow][nextCol]) continue;

            dfs(nextRow, nextCol, currentWord, visited, board, results);
            visited[nextRow][nextCol] = false;
        }
    }

    private Iterable<int[]> getNeighbors(int row, int column, BoggleBoard board) {
        Set<int[]> neighbors = new HashSet<>();

        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++)
                if (i != 0 || j != 0)
                    if (row + i >= 0 && row + i < board.rows())
                        if (column + j >= 0 && column + j < board.cols())
                            neighbors.add(new int[]{row + i, column + j});

        return neighbors;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        Integer value =  this.dictionaryTrie.get(word);
        return value == null ? 0 : value;
    }

    public static void main(String[] args) {
        // Unit testing
        In testIn = new In("test-resources/part3_week4/dictionary-algs4.txt");
        String[] testDict = testIn.readAllStrings();
        BoggleSolver testSolver = new BoggleSolver(testDict);

        assert testSolver.scoreOf("END") == 1 : "Score of END should be 1, was " + testSolver.scoreOf("END");
        assert testSolver.scoreOf("ENDS") == 1 : "Score of ENDS should be 1, was " + testSolver.scoreOf("ENDS");
        assert testSolver.scoreOf("QUESTIONS") == 11 : "Score of QUESTIONS should be 11, was " + testSolver.scoreOf("QUESTIONS");

        // board 4x4 (without q)
        BoggleBoard testBoard1 = new BoggleBoard("test-resources/part3_week4/board4x4.txt");
        int testScore1 = 0;
        for (String word : testSolver.getAllValidWords(testBoard1))
            testScore1 += testSolver.scoreOf(word);

        assert testScore1 == 33 : "Test score should be 33, was " + testScore1;

        // board-q
        BoggleBoard testBoard2 = new BoggleBoard("test-resources/part3_week4/board-q.txt");
        int testScore2 = 0;
        for (String word : testSolver.getAllValidWords(testBoard2))
            testScore2 += testSolver.scoreOf(word);

        assert testScore2 == 84 : "Test score should be 84, was " + testScore2;

        // dictionairy-yawl.txt
        testIn = new In("test-resources/part3_week4/dictionary-yawl.txt");
        testDict = testIn.readAllStrings();
        testSolver = new BoggleSolver(testDict);
        int wordCount3 = 0;

        for (String word : testSolver.getAllValidWords(testBoard1)) {
            wordCount3++;
            assert testSolver.scoreOf(word) > 0 : "Expected score > 0 for word: " + word;
        }

        assert testSolver.scoreOf("EDIT") == 1 : "Expected dict to contain 'EDIT' with score 1, score was: " + testSolver.scoreOf("EDIT");
        assert wordCount3 == 204 : "Expected 204 entries, was: " + wordCount3;

        StdOut.println("All tests passed.");

        // Main
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}
