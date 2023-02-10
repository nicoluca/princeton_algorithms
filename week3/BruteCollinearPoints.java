import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BruteCollinearPoints {
    private final LineSegment[] segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        Point[] pointsSorted = checkAndSortPoints(points);
        this.segments = findSegments(Arrays.copyOf(pointsSorted, points.length));
    }

    private static Point[] checkAndSortPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Points array is null");
        for (Point p : points) {
            if (p == null) throw new IllegalArgumentException("Point is null");
        }
        Point[] pointsSorted = points.clone();
        Arrays.sort(pointsSorted);
        for (int i = 0; i < pointsSorted.length - 1; i++) {
            if (pointsSorted[i].compareTo(pointsSorted[i + 1]) == 0)
                throw new IllegalArgumentException("Duplicate points");
        }
        return pointsSorted;
    }

    private static LineSegment[] findSegments(Point[] points) {
        List<LineSegment> segments = new ArrayList<>();
        if (points.length < 4) return new LineSegment[0];

        int numberPoints = points.length;
        int amountEvaluation = 4;

        // Look at 4 points p, q, r, s but only in distinct sets (brute force).
        for (int p = 0; p <= numberPoints - amountEvaluation; p++) {
            for (int q = p + 1; q <= numberPoints - amountEvaluation + 1; q++) {
                for (int r = q + 1; r <= numberPoints - amountEvaluation + 2; r++) {
                    for (int s = r + 1; s <= numberPoints - amountEvaluation + 3; s++) {

                        Comparator<Point> comparator = points[p].slopeOrder();

                        if (comparator.compare(points[q], points[r]) == 0 &&
                                comparator.compare(points[r], points[s]) == 0) {
                            Point[] currentPoints = {points[p], points[q], points[r], points[s]};
                            Arrays.sort(currentPoints);

                            LineSegment current = new LineSegment(currentPoints[0], currentPoints[3]);
                            segments.add(current);
                        }
                    }
                }
            }
        }

        if (segments.isEmpty()) return new LineSegment[0];
        else return segments.toArray(new LineSegment[0]);
    }


    // the number of line segments
    public int numberOfSegments() {
        return this.segments().length;
    }

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(this.segments, this.segments.length);
    }
}