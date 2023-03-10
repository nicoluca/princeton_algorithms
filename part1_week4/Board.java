import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class Board {
    private final int[][] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null)
            throw new IllegalArgumentException("Tiles cannot be null.");
        if (tiles.length == 0)
            throw new IllegalArgumentException("Tiles cannot be empty.");
        if (tiles.length != tiles[0].length)
            throw new IllegalArgumentException("Tiles must be a square.");

        this.tiles = new int[tiles.length][tiles.length];
        for (int row = 0; row < tiles.length; row++)
            this.tiles[row] = tiles[row].clone();
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension() + "\n");
        Arrays.stream(tiles).forEach(row -> {
            Arrays.stream(row).forEach(col -> s.append(String.format("%2d ", col)));
            s.append("\n");
        });
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[row].length; col++) {
                if (tiles[row][col] != 0 &&
                        tiles[row][col] != row * tiles.length + col + 1)
                    hamming++;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        int length = tiles.length;
        for (int row = 0; row < length; row++)
            for (int col = 0; col < length; col++)
                manhattan += getManhattanDistance(row, col);

        return manhattan;
    }

    private int getManhattanDistance(int row, int col) {
        int value = tiles[row][col];
        if (tiles[row][col] == 0)
            return 0;

        int rowDestination = (value - 1) / tiles.length;
        int colDestination = (value - 1) % tiles.length;
        return Math.abs(row - rowDestination) + Math.abs(col - colDestination);
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null)
            return false;
        if (y == this)
            return true;
        if (y.getClass() != this.getClass())
            return false;

        Board that = (Board) y;
        if (this.dimension() != that.dimension())
            return false;

        return Arrays.deepEquals(this.tiles, that.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int[] zero = findZero();
        int row = zero[0];
        int col = zero[1];

        Stack<Board> neighbors = new Stack<>();
        if (row > 0)
            neighbors.push(swap(row, col, row - 1, col));
        if (row < tiles.length - 1)
            neighbors.push(swap(row, col, row + 1, col));
        if (col > 0)
            neighbors.push(swap(row, col, row, col - 1));
        if (col < tiles.length - 1)
            neighbors.push(swap(row, col, row, col + 1));

        return neighbors;
    }

    private Board swap(int row1, int col1, int row2, int col2) {
        int[][] newTiles = new int[tiles.length][tiles.length];
        for (int row = 0; row < tiles.length; row++)
            newTiles[row] = tiles[row].clone();

        int temp = newTiles[row1][col1];
        newTiles[row1][col1] = newTiles[row2][col2];
        newTiles[row2][col2] = temp;

        return new Board(newTiles);
    }

    private int[] findZero() {
        for (int row = 0; row < tiles.length; row++)
            for (int col = 0; col < tiles.length; col++)
                if (tiles[row][col] == 0)
                    return new int[] { row, col };
        throw new IllegalStateException("Board does not contain a zero.");
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[] origin = findNextNonZero(0, 0);
        int[] destination = findNextNonZero(origin[0], origin[1] + 1);
        return swap(origin[0], origin[1], destination[0], destination[1]);
    }

    private int[] findNextNonZero(int row, int col) {
        for (int r = row; r < tiles.length; r++) {
            for (int c = col; c < tiles.length; c++) {
                if (tiles[r][c] != 0)
                    return new int[] { r, c };
            }
        }
        throw new IllegalStateException("Board does not contain two non-zero tiles.");
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        System.out.println("Test boards:");
        Board testBoard = new Board(new int[][] { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } });
        System.out.println(testBoard);
        Board testBoard2 = new Board(new int[][] { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } });
        System.out.println(testBoard2);
        Board testBoard3 = new Board(new int[][] { { 7, 6, 5 }, { 4, 0, 2 }, { 8, 1, 3 } });
        System.out.println(testBoard3);

        assert testBoard.dimension() == 3 : "dimension() failed, expected 3, got " + testBoard.dimension();
        assert testBoard.hamming() == 5 : "hamming() failed, expected 5, got " + testBoard.hamming();
        assert testBoard.getManhattanDistance(0, 0) == 3 : "getManhattanDistance() failed, expected 0, got "
                + testBoard.getManhattanDistance(0, 0);
        assert testBoard.getManhattanDistance(2, 1) == 2 : "getManhattanDistance() failed, expected 2, got "
                + testBoard.getManhattanDistance(2, 1);
        assert testBoard.manhattan() == 10 : "manhattan() failed, expected 10, got " + testBoard.manhattan();

        assert testBoard.equals(testBoard2) : "equals() failed, expected true, got false";
        assert !testBoard.equals(testBoard3) : "equals() failed, expected false, got true";

        assert !testBoard.isGoal() : "isGoal() failed, expected false, got " + testBoard.isGoal();
        assert new Board(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 0}}).isGoal() : "isGoal() failed, expected true, got false";

        assert testBoard.findZero()[0] == 1 : "findZero() failed, expected 1, got " + testBoard.findZero()[0];
        assert testBoard.findZero()[1] == 1 : "findZero() failed, expected 1, got " + testBoard.findZero()[1];

        Stack<Board> neighbors = (Stack<Board>) testBoard.neighbors();
        System.out.println("Neighbor board:");
        System.out.println(neighbors.peek());
        // Asserting that the last pushed board is the one with column moved to the left
        assert neighbors.pop().equals(new Board(new int[][] { { 8, 1, 3 }, { 4, 2, 0 }, { 7, 6, 5 } })) : "neighbors() failed, expected true, got false";

        Board twinBoard = testBoard.twin();
        System.out.println("Twin board:");
        System.out.println(twinBoard);
        System.out.println("Original board:");
        System.out.println(testBoard);
        System.out.println(twinBoard.equals(testBoard));
        assert !twinBoard.equals(testBoard) : "twin() failed, expected false, got true";
        assert twinBoard.findZero()[0] == testBoard.findZero()[0] && twinBoard.findZero()[1] == testBoard.findZero()[1] : "twin() failed, expected zero at same position, got different position";

        System.out.println("All tests passed.");

    }

}