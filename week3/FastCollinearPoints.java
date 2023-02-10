import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private final LineSegment[] segments;
    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsCopy);
        checkSortedPoints(pointsCopy);
        this.segments = findSegments(Arrays.copyOf(pointsCopy, pointsCopy.length));
    }

    private static void checkSortedPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("'points' is null");
        if (points[points.length - 1] == null) throw new IllegalArgumentException("At least one point is null");

        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null) throw new IllegalArgumentException("At least one point is null");
            if (points[i].compareTo(points[i + 1]) == 0) throw new IllegalArgumentException("Contains duplicate points");
        }
    }

    public int numberOfSegments() {
        return this.segments().length;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(this.segments, this.segments.length);
    }

    private static LineSegment[] findSegments(Point[] points) {
        List<LineSegment> segments = new ArrayList<>();
        if (points.length < 4) return new LineSegment[0];

        for (int p = 0; p < points.length; p++) {
            // Create tmp array for examination with respect to p, sort them by slope to p
            Point currentPoint = points[p];
            Point[] tmpPoints = new Point[points.length - 1];
            System.arraycopy(points, 0, tmpPoints, 0, p);
            System.arraycopy(points, p + 1, tmpPoints, p, points.length - p - 1);
            Arrays.sort(tmpPoints, currentPoint.slopeOrder());

            // Find all collinear points with respect to p
            int amountCollinear = 1;
            for (int i = 0; i < tmpPoints.length; i++) {
                // Check if the end is reached or the slope changes
                if (i == tmpPoints.length - 1
                        || currentPoint.slopeTo(tmpPoints[i]) != currentPoint.slopeTo(tmpPoints[i + 1])) {
                    // Check if there are at least 3 collinear points
                    if (amountCollinear >= 3) {
                        Point[] collinearPoints = new Point[amountCollinear + 1];
                        collinearPoints[0] = currentPoint;
                        System.arraycopy(tmpPoints, i - amountCollinear + 1, collinearPoints, 1, amountCollinear);
                        LineSegment current = new LineSegment(collinearPoints[0], collinearPoints[collinearPoints.length - 1]);

                        // Check if the segment is already in the list
                        // HashSet would be better, but it is not allowed
                        boolean alreadyInList = false;
                        for (LineSegment segment : segments) {
                            if (segment.toString().equals(current.toString())) {
                                alreadyInList = true;
                                break;
                            }
                        }
                        if (!alreadyInList) segments.add(current);
                    }
                    amountCollinear = 1;
                } else {
                    amountCollinear++;
                }
            }
        }
        if (segments.isEmpty()) return new LineSegment[0];
        return segments.toArray(new LineSegment[0]);
    }
}