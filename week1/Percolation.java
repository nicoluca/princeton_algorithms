import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private WeightedQuickUnionUF tree;
    private int numberOfOpenSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        this.tree = new WeightedQuickUnionUF(n * n + 2);

        // Connect bottom layer to 'digital' node
        for (int j = 1; j <= n; j++) {
            this.tree.union(n * (n - 1) + j, n * n + 1);
        }

        this.grid = new boolean[n][n];
        this.numberOfOpenSites = 0;

    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row > this.grid.length || col > this.grid.length || row < 1 || col < 1) throw new IllegalArgumentException();
        if (!this.grid[row-1][col-1]) {
            this.grid[row-1][col-1] = true;
            this.numberOfOpenSites++;
            this.connectWithOpenNeighbors(row, col);
        }
    }

    private void connectWithOpenNeighbors (int row, int col)  {
        //Top row
        if (row != 1) {
            if (this.grid[row-2][col-1]) {
                this.tree.union(this.grid.length * (row - 1 - 1) + col, this.grid.length * (row - 1) + col);
            }
        }

        if (row == 1) {
            this.tree.union(0, this.grid.length * (row - 1) + col);
        }

        //Bottom row
        if (row != this.grid.length) {
            if (this.grid[row][col-1]) {
                this.tree.union(this.grid.length * (row - 1 + 1) + col, this.grid.length * (row - 1) + col);
            }
        }

        //Left
        if (col != 1) {
            if (this.grid[row-1][col-2]) {
                this.tree.union(this.grid.length * (row - 1) + col - 1, this.grid.length * (row - 1) + col);
            }
        }

        //Right
        if (col != this.grid.length) {
            if (this.grid[row-1][col]) {
                this.tree.union(this.grid.length * (row - 1) + col + 1, this.grid.length * (row - 1) + col);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row > this.grid.length || col > this.grid.length || row < 1 || col < 1) throw new IllegalArgumentException();
        return this.grid[row-1][col-1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row > this.grid.length || col > this.grid.length || row < 1 || col < 1) throw new IllegalArgumentException();
        return this.tree.find(this.grid.length * (row - 1) + col) == this.tree.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return this.tree.find(this.grid.length * this.grid.length + 1) == this.tree.find(0);
    }

    // test client (optional)
    public static void main(String[] args) {
        System.out.println("Enter size of grid n:");
        int n = StdIn.readInt();
        Percolation perc = new Percolation(n);
        perc.printGrid();
        int row, column;
        while (StdIn.hasNextChar()) {
            StdOut.println("Enter row to open");
            row = StdIn.readInt();
            StdOut.println("Enter column to open");
            column = StdIn.readInt();
            perc.open(row, column);
            perc.printGrid();
        }

    }

    private void printGrid() {
        for (int i = 1; i <= this.grid.length; i++) {
            for (int j = 1; j <= this.grid.length; j++) {
                if (this.isOpen(i, j)) {
                    if (this.isFull(i, j)) {
                        StdOut.print("W ");
                    } else {
                        StdOut.print("O ");
                    }
                } else {
                    StdOut.print("X ");
                }
            }
            StdOut.println();
        }
        StdOut.println("Number of open sites: " + this.numberOfOpenSites());
        StdOut.println("Percolates: " + this.percolates());
    }
}
