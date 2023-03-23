import java.util.Arrays;

public class PictureAcyclicDG {
    private final double[][] energyMatrix;
    private final int width;
    private final int height;
    private final double[][] distTo;
    private final int[][] edgeTo;
    private int[] seam;

    // Default will be vertical seam, picture is rotated for horizontal seam in SeamCarver as suggested in FAQ.
    public PictureAcyclicDG(double[][] energyMatrix) {
        this.energyMatrix = energyMatrix;
        this.width =  energyMatrix.length;
        this.height = energyMatrix[0].length;
        this.distTo = new double[width][height];
        this.edgeTo = new int[width][height];

        for (double[] row : distTo)
            Arrays.fill(row, Double.POSITIVE_INFINITY);

        for (int column = 0; column < width; column++)
            distTo[column][0] = 0;

        traverseGraph();
        findShortestPath();
    }

    public int[] getSeam() {
        return this.seam;
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

            if (distTo[neighbourColumn][row + 1] > distTo[column][row] + energyMatrix[column][row + 1]) {
                distTo[neighbourColumn][row + 1] = distTo[column][row] + energyMatrix[column][row + 1];
                edgeTo[neighbourColumn][row + 1] = column;
            }
        }
    }


    // We go through all bottom pixels and find the one with the shortest distance, then traverse to the top edge.
    private void findShortestPath() {
        double minDistance = Double.POSITIVE_INFINITY;
        int minDistanceBottomColumn = 0;
        for (int column = 0; column < width; column++) {
            if (distTo[column][height - 1] < minDistance) {
                minDistance = distTo[column][height - 1];
                minDistanceBottomColumn = column;
            }
        }

        this.seam = new int[height];
        int currentColumn = minDistanceBottomColumn;
        int currentRow = height - 1;
        while (currentRow >= 0) {
            this.seam[currentRow] = currentColumn;
            currentColumn = edgeTo[currentColumn][currentRow];
            currentRow--;
        }
    }

}
