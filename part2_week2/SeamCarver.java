import edu.princeton.cs.algs4.Picture;

import java.util.Arrays;

public class SeamCarver {
    private Picture picture;
    private double[][] energyMatrix;
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException("Picture cannot be null.");

        this.picture = new Picture(picture);
        this.energyMatrix = calculateEnergyMatrix();
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return this.picture.width();
    }

    // height of current picture
    public int height() {
        return this.picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        verifyColumnPixel(x);
        verifyRowPixel(y);

        if (isBorderPixel(x, y))
            return 1000;

        return Math.sqrt(deltaXSquare(x, y) + deltaYSquare(x, y));
    }

    private double deltaXSquare(int column, int row) {
        int red = picture.get(column - 1, row).getRed() - picture.get(column + 1, row).getRed();
        int green = picture.get(column - 1, row).getGreen() - picture.get(column + 1, row).getGreen();
        int blue = picture.get(column - 1, row).getBlue() - picture.get(column + 1, row).getBlue();
        return Math.pow(red, 2) + Math.pow(green, 2) + Math.pow(blue, 2);
    }

    private double deltaYSquare(int column, int row) {
        int red = picture.get(column, row - 1).getRed() - picture.get(column, row + 1).getRed();
        int green = picture.get(column, row - 1).getGreen() - picture.get(column, row + 1).getGreen();
        int blue = picture.get(column, row - 1).getBlue() - picture.get(column, row + 1).getBlue();
        return Math.pow(red, 2) + Math.pow(green, 2) + Math.pow(blue, 2);
    }

    private boolean isBorderPixel(int column, int row) {
        return column == 0 || column == width() - 1 || row == 0 || row == height() - 1;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // As suggested in the assignment, we can rotate the picture and use the vertical seam finder.
        double[][] cachedEnergyMatrix = energyMatrix;
        this.energyMatrix = rotateEnergyMatrix(energyMatrix);
        int[] horizontalSeam = findVerticalSeam();
        this.energyMatrix = cachedEnergyMatrix;
        return horizontalSeam;
    }

    private double[][] rotateEnergyMatrix(double[][] energyMatrixToRotate) {
        double[][] rotatedEnergyMatrix = new double[height()][width()];
        for (int column = 0; column < width(); column++)
            for (int row = 0; row < height(); row++)
                rotatedEnergyMatrix[row][column] = energyMatrixToRotate[column][row];

        return rotatedEnergyMatrix;
    }


    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        PictureAcyclicDG pictureAcyclicDG = new PictureAcyclicDG(energyMatrix);
        return pictureAcyclicDG.getSeam();
    }

    private double[][] calculateEnergyMatrix() {
        double[][] energyMatrixCalculated = new double[width()][height()];
        for (int column = 0; column < width(); column++)
            for (int row = 0; row < height(); row++)
                energyMatrixCalculated[column][row] = energy(column, row);

        return energyMatrixCalculated;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        verifyHorizontalSeam(seam);

        this.picture = rotatePicture(this.picture);
        removeVerticalSeam(seam);
        this.picture = rotatePicture(this.picture);
        this.energyMatrix = calculateEnergyMatrix();
    }

    private Picture rotatePicture(Picture pictureToRotate) {
        Picture rotatedPicture = new Picture(pictureToRotate.height(), pictureToRotate.width());
        for (int column = 0; column < pictureToRotate.width(); column++)
            for (int row = 0; row < pictureToRotate.height(); row++)
                rotatedPicture.set(row, column, pictureToRotate.get(column, row));

        return rotatedPicture;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        verifyVerticalSeam(seam);

        Picture newPicture = new Picture(width() - 1, height());
        for (int row = 0; row < height(); row++) {
            int column = 0;

            // Skip the column that is part of the seam
            for (int newColumn = 0; newColumn < width() - 1; newColumn++) {
                if (newColumn == seam[row])
                    column++;

                newPicture.set(newColumn, row, this.picture.get(column, row));
                column++;
            }
        }

        this.picture = newPicture;
        this.energyMatrix = calculateEnergyMatrix();
    }

    private void verifyHorizontalSeam(int[] horizontalSeam) {
        if (horizontalSeam == null)
            throw new IllegalArgumentException("Seam cannot be null.");
        if (horizontalSeam.length != width())
            throw new IllegalArgumentException("Seam length must be equal to width. Was: " + horizontalSeam.length);
        if (height() <= 1)
            throw new IllegalArgumentException("Height must be greater than 1. Was: " + height());
        for (int i = 0; i < horizontalSeam.length; i++) {
            verifyRowPixel(horizontalSeam[i]);
            if (i > 0 && Math.abs(horizontalSeam[i] - horizontalSeam[i - 1]) > 1)
                throw new IllegalArgumentException("Seam must be a valid path. Was: " + horizontalSeam[i]);
        }
    }

    private void verifyVerticalSeam(int[] verticalSeam) {
        if (verticalSeam == null)
            throw new IllegalArgumentException("Seam cannot be null.");
        if (verticalSeam.length != height())
            throw new IllegalArgumentException("Seam length must be equal to height. Was: " + verticalSeam.length);
        if (width() <= 1)
            throw new IllegalArgumentException("Width must be greater than 1. Was: " + width());
        for (int i = 0; i < verticalSeam.length; i++) {
            verifyColumnPixel(verticalSeam[i]);
            if (i > 0 && Math.abs(verticalSeam[i] - verticalSeam[i - 1]) > 1)
                throw new IllegalArgumentException("Seam must be a valid path. Was: " + verticalSeam[i]);
        }
    }

    private void verifyColumnPixel(int column) {
        if (column < 0 || column >= width())
            throw new IllegalArgumentException("Column pixel must be between 0 and width - 1. Was: " + column);
    }

    private void verifyRowPixel(int row) {
        if (row < 0 || row >= height())
            throw new IllegalArgumentException("Row pixel must be between 0 and height - 1. Was: " + row);
    }


    //  unit testing (optional)
    public static void main(String[] args) {
        String pathToPicture = "test-ressources/3x4.png";
        double doubleCompareTolerance = 0.0001d;

        Picture picture = new Picture(pathToPicture);
        SeamCarver seamCarver = new SeamCarver(picture);

        assert seamCarver.width() == 3 : "Width should be 3. Was: " + seamCarver.width();
        assert seamCarver.height() == 4 : "Height should be 4. Was: " + seamCarver.height();

        assert seamCarver.energy(0, 0) == 1000 : "Energy should be 1000. Was: " + seamCarver.energy(0, 0);
        assert seamCarver.energy(1, 0) == 1000 : "Energy should be 1000. Was: " + seamCarver.energy(1, 0);
        assert seamCarver.energy(2, 3) == 1000 : "Energy should be 1000. Was: " + seamCarver.energy(2, 0);
        double energySquared12 = Math.pow(seamCarver.energy(1, 2), 2);
        assert Math.abs(energySquared12 - 52024) < doubleCompareTolerance : "Squared energy should be close to 52024. Was: " + energySquared12;
        double energySquared11 = Math.pow(seamCarver.energy(1, 1), 2);
        assert Math.abs(energySquared11 - 52225) < doubleCompareTolerance : "Squared energy should be close to 52225. Was: " + energySquared11;

        int[] verticalSeam = seamCarver.findVerticalSeam();
        assert verticalSeam.length == 4 : "Vertical seam length should be 4. Was: " + verticalSeam.length;
        int[] horizontalSeam = seamCarver.findHorizontalSeam();
        assert horizontalSeam.length == 3 : "Horizontal seam length should be 3. Was: " + horizontalSeam.length;

        assert verticalSeam[0] == 1 : "Vertical seam should be [1, 1, 0, 0]. Was: " + Arrays.toString(verticalSeam);
        assert horizontalSeam[0] == 2 : "Horizontal seam should be [2, 1, 0]. Was: " + Arrays.toString(horizontalSeam);

        seamCarver.removeVerticalSeam(verticalSeam);
        assert seamCarver.width() == 2 : "Width should be 2. Was: " + seamCarver.width();
        assert seamCarver.height() == 4 : "Height should be 4. Was: " + seamCarver.height();

        horizontalSeam = seamCarver.findHorizontalSeam();
        seamCarver.removeHorizontalSeam(horizontalSeam);
        assert seamCarver.width() == 2 : "Width should be 2. Was: " + seamCarver.width();
        assert seamCarver.height() == 3 : "Height should be 3. Was: " + seamCarver.height();

        // Additional testing using Princeton's provided test clients.
    }


}