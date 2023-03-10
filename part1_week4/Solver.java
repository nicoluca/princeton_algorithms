import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private final static boolean IS_MANHATTAN = true;

    private final Board initial;
    private final Board twin;
    private int moves = -1;
    private boolean solvable = false;
    private Stack<Board> solution = null;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("Initial board cannot be null.");
        
        this.initial = initial;
        this.twin = initial.twin();
        this.initialise();
    }

    private void initialise() {
        MinPQ<BoardSearchNode> boardMinPriorityQueue = new MinPQ<>(getBoardSearchNodeComparator());
        boardMinPriorityQueue.insert(new BoardSearchNode(initial, 0, null));
        MinPQ<BoardSearchNode> twinBoardMinPriorityQueue = new MinPQ<>(getBoardSearchNodeComparator());
        twinBoardMinPriorityQueue.insert(new BoardSearchNode(twin, 0, null));
        this.tryToSolve(boardMinPriorityQueue, twinBoardMinPriorityQueue);
    }

    private void tryToSolve(MinPQ<BoardSearchNode> boardMinPriorityQueue, MinPQ<BoardSearchNode> twinBoardMinPriorityQueue) {
        BoardSearchNode current = boardMinPriorityQueue.delMin();
        BoardSearchNode twinCurrent = twinBoardMinPriorityQueue.delMin();

        do {
            if (current.getBoard().isGoal()) {
                this.buildSolution(current);
                return;
            }

            if (twinCurrent.getBoard().isGoal())
                return;

            current = this.makeOneMove(current, boardMinPriorityQueue);
            twinCurrent = this.makeOneMove(twinCurrent, twinBoardMinPriorityQueue);

        } while (!boardMinPriorityQueue.isEmpty() && !twinBoardMinPriorityQueue.isEmpty());
    }

    private BoardSearchNode makeOneMove(BoardSearchNode current, MinPQ<BoardSearchNode> minPQ) {
        for (Board neighbour : current.getBoard().neighbors())
            if (current.getPrevious() == null || // First move
                    !neighbour.equals(current.getPrevious().getBoard())) // Avoid going back
                minPQ.insert(new BoardSearchNode(neighbour, current.getMoves() + 1, current));
        return minPQ.delMin();
    }

    private void buildSolution(BoardSearchNode current) {
        this.moves = current.getMoves();
        this.solvable = true;
        this.solution = new Stack<>();
        while (current != null) {
            solution.push(current.getBoard());
            current = current.getPrevious();
        }
    }

    private static class BoardSearchNode {
        private final Board board;
        private final int moves;
        private final BoardSearchNode previous;

        // Cache these values to avoid recalculation
        private final int distance;

        public BoardSearchNode(Board board, int moves, BoardSearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
            if (IS_MANHATTAN)
                this.distance = board.manhattan();
            else
                this.distance = board.hamming();
        }

        public Board getBoard() {
            return board;
        }

        public int getMoves() {
            return moves;
        }

        public BoardSearchNode getPrevious() {
            return previous;
        }

        public int getDistance() {
            return distance;
        }
    }

    private Comparator<BoardSearchNode> getBoardSearchNodeComparator() {
        return Comparator.comparingInt(o -> o.getDistance() + o.getMoves());
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return this.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return this.solution;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}