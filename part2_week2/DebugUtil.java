import java.util.Arrays;

public class DebugUtil {
    public static void printMatrix(double[][] matrix) {
        System.out.println("Printing matrix with " + matrix.length + " rows and " + matrix[0].length + " columns.");
        for (double[] row : matrix)
            System.out.println(Arrays.toString(row));
    }
}
