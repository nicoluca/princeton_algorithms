import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;
public class PercolationStats {
    private double[] movesUntilPercolation;
    private final static double CONFIDENCE_95 = 1.96;
    private double mean, stddev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        this.movesUntilPercolation = new double[trials];
        int fieldSize = n * n;

        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                while (true) {
                    int row = StdRandom.uniformInt(n) + 1, col = StdRandom.uniformInt(n) + 1;
                    if (!perc.isOpen(row, col)) {
                        perc.open(row, col);
                        break;
                    }
                }
            }
            this.movesUntilPercolation[i] = perc.numberOfOpenSites() / (double) fieldSize;
        }

        mean = StdStats.mean(movesUntilPercolation);
        stddev = StdStats.stddev(movesUntilPercolation);
    }

    // sample mean of percolation threshold
    public double mean() {
        return this.mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return this.stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean() - (CONFIDENCE_95 * this.stddev() / Math.sqrt(this.movesUntilPercolation.length));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean() + (CONFIDENCE_95 * this.stddev() / Math.sqrt(this.movesUntilPercolation.length));
    }

    // test client (see below)

    /**
     * Also, include a main() method that takes two command-line arguments n and T,
     * performs T independent computational experiments (discussed above) on an n-by-n grid,
     * and prints the sample mean, sample standard deviation,
     * and the 95% confidence interval for the percolation threshold.
     * Use StdRandom to generate random numbers;
     * use StdStats to compute the sample mean and sample standard deviation.
     * @param args
     */
    public static void main(String[] args) {
        // Quick args override
        //args = new String[2];
        //args[0] = "2000";
        //args[1] = "1000";

        //Stopwatch stopwatch = new Stopwatch();
        PercolationStats percStats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        //StdOut.println("Time passed: " + stopwatch.elapsedTime());
        StdOut.println("mean                    = " + percStats.mean());
        StdOut.println("stddev                  = " + percStats.stddev());
        StdOut.println("95% confidence interval = [" + percStats.confidenceLo() + ", " + percStats.confidenceHi() + "]");

    }

}
