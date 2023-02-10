import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BruteCollinearPoints {
    private final LineSegment[] segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
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
        return segments.toArray(new LineSegment[segments.size()]);
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