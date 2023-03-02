import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.Set;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        this.points = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return this.points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Point cannot be null.");
        this.points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return this.points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        this.points.forEach(Point2D::draw);
    }

    // all points that are inside the rectangle (or on the boundary)
    // RQ: O(n) time complexity
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("Rectangle cannot be null.");

        Set<Point2D> pointsInRange = new TreeSet<>();
        this.points.forEach(point -> {
            if (rect.contains(point))
                pointsInRange.add(point);
        });
        return pointsInRange;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    // RQ: O(n) time complexity
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Point cannot be null.");

        Point2D nearestPoint = null;
        double nearestDistance = Double.MAX_VALUE;

        for (Point2D point : this.points) {
            double distance = point.distanceTo(p);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestPoint = point;
            }
        }
        return nearestPoint;

    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET pointSet = new PointSET();
        assert pointSet.isEmpty() : "Expected empty point set.";

        pointSet.insert(new Point2D(1, 1));
        pointSet.insert(new Point2D(1, 1));
        pointSet.insert(new Point2D(0, 0));

        assert pointSet.size() == 2 : "Point set has size " + pointSet.size() + ", expected 2.";
        assert !pointSet.isEmpty() : "Expected non-empty point set.";
        assert pointSet.contains(new Point2D(1, 1)) : "Expected point set to contain (1, 1).";
        assert !pointSet.contains(new Point2D(2, 2)) : "Expected point set to not contain (2, 2).";

        RectHV rect = new RectHV(0, 0, 1, 1);
        Iterable<Point2D> pointsInRect = pointSet.range(rect);
        assert pointsInRect.iterator().next().equals(new Point2D(0, 0)) : "Expected rectangle to contain Point(0, 0).";

        Point2D nearestPoint = pointSet.nearest(new Point2D(0, 0.5));
        assert nearestPoint.equals(new Point2D(0, 0)) : "Expected nearest point to be Point(0, 0).";
    }
}