import java.util.Arrays;
import java.util.stream.IntStream;

public class PictureAcyclicDG {
    private final double[][] energyMatrix;
    private final int width;
    private final int height;
    private final double[][] distTo;
    private final int[][] edgeTo;
    private int[] seam;

    // Default will be vertical seam, picture is rotated for horizontal seam in SeamCarver as suggested in FAQ.
    public PictureAcyclicDG(double[][] energyMatrix) {
        this.width = energyMatrix[0].length;
        this.height = energyMatrix.length;

        this.energyMatrix = new double[height][width];
        for (int row = 0; row < height; row++)
            this.energyMatrix[row] = Arrays.copyOf(energyMatrix[row], energyMatrix[row].length);

        this.distTo = new double[height][width];
        this.edgeTo = new int[height][width];

        // Fill first edgeTo row with the column index, rest with -1.
        IntStream.range(0, width).forEach(column -> edgeTo[0][column] = column);
        for (int row = 1; row < height; row++)
                Arrays.fill(edgeTo[row], -1);

        // Fill first row of distTo with 1000, rest with infinity.
        for (int row = 0; row < height; row++) {
            if (row == 0)
                Arrays.fill(distTo[row], 1000);
            else
                Arrays.fill(distTo[row], Double.POSITIVE_INFINITY);
        }

        traverseGraph();
        findShortestPath();
    }

    public int[] getSeam() {
        return Arrays.copyOf(seam, seam.length);
    }

    // We traverse the graph from top to bottom, and relax each node with its neighbours.
    private void traverseGraph() {
        for (int row = 0; row < this.height - 1; row++)
            for (int column = 0; column < this.width; column++)
                relax(column, row);
    }

    // Relax if the distance to the neighbour is less than the current distance -> update the distance and edge.
    private void relax(int column, int row) {
        for (int neighbourColumn = column - 1; neighbourColumn <= column + 1; neighbourColumn++) {
            if (neighbourColumn < 0 || neighbourColumn >= width)
                continue;

            if (distTo[row+1][neighbourColumn] > distTo[row][column] + energyMatrix[row+1][neighbourColumn]) {
                distTo[row+1][neighbourColumn] = distTo[row][column] + energyMatrix[row+1][neighbourColumn];
                edgeTo[row+1][neighbourColumn] = column;
            }
        }
    }


    // We go through all bottom pixels and find the one with the shortest distance, then traverse to the top edge.
    private void findShortestPath() {
        double minDistance = Double.POSITIVE_INFINITY;
        int minDistanceBottomColumn = 0;

        for (int column = 0; column < width; column++) {
            if (distTo[height-1][column] < minDistance) {
                minDistance = distTo[height-1][column];
                minDistanceBottomColumn = column;
            }
        }

        this.seam = new int[height];
        int currentColumn = minDistanceBottomColumn;
        int currentRow = height - 1;
        while (currentRow >= 0) {
            this.seam[currentRow] = currentColumn;
            currentColumn = edgeTo[currentRow][currentColumn];
            currentRow--;
        }
    }

}
