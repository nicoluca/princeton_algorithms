/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
        if (that == null) throw new NullPointerException();

        if (this.x == that.x && this.y == that.y) return Double.NEGATIVE_INFINITY;
        else if (this.x == that.x) return Double.POSITIVE_INFINITY;
        else if (this.y == that.y) return 0.0; // +0.0
        else return (double) (that.y - this.y) / (that.x - this.x);
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        /* YOUR CODE HERE */
        if (this.y > that.y) return 1;
        if (this.y < that.y) return -1;
        if (this.x > that.x) return 1;
        if (this.x < that.x) return -1;
        return 0;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        /* YOUR CODE HERE */
        return new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                if (o1 == null || o2 == null) throw new NullPointerException();

                if (slopeTo(o1) < slopeTo(o2)) return -1;
                if (slopeTo(o1) == slopeTo(o2)) return 0;
                return 1;
            }
        };
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        Point point1 = new Point(1, 1);
        Point point2 = new Point(500, 500);
        Point point3 = new Point(1, 2);

        // compareTo
        assert point1.compareTo(point2) < 0;
        assert point1.compareTo(point3) < 0;
        assert point2.compareTo(point3) > 0;
        assert point3.compareTo(point1) > 0;

        // slopeTo
        assert point1.slopeTo(point2) == 1;
        assert point1.slopeTo(point3) == Double.POSITIVE_INFINITY;
        assert point3.slopeTo(point3) == Double.NEGATIVE_INFINITY;

        Point pointA = new Point(391, 481);
        Point pointB = new Point(473, 434);
        assert pointA.slopeTo(pointB) == -0.573170731707317 :
                "SlopeTo: Expected -0.573170731707317, but was: " + pointA.slopeTo(pointB);

        pointA = new Point(1, 6);
        pointB = new Point(8, 1);
        assert pointA.slopeTo(pointB) == -0.7142857142857143 :
                "SlopeTo: Expected -0.573170731707317, but was: " + pointA.slopeTo(pointB);

        Point pointP = new Point(442, 186);
        Point pointQ = new Point(215, 236);
        Point pointR = new Point(199, 275);
        Comparator<Point> comp = pointP.slopeOrder();
        assert comp.compare(pointQ, pointR) > 0 :
                "Compare: Expected 1, but was: " + comp.compare(pointQ, pointR);

        // slopeOrder
        assert point1.slopeOrder().compare(point2, point3) < 0;
        assert point1.slopeOrder().compare(point3, point2) > 0;
        assert point1.slopeOrder().compare(point3, point3) == 0;

        // draw
        // point1.drawTo(point3);
    }
}