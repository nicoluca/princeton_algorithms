import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;
    private Node root;
    private int size = 0;

    private static class Node {
        private final Point2D point;
        private Node left;
        private Node right;
        private final boolean isVertical;
        private final RectHV rect;

        private Node(Point2D point, boolean isVertical, RectHV rect) {
            this.point = point;
            this.isVertical = isVertical;
            this.rect = rect;
        }
    }

    public KdTree() {
        this.root = null;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public int size() {
        return this.size;
    }

    public void insert(Point2D point) {
        if (point == null)
            throw new IllegalArgumentException("Point cannot be null.");

        if (this.root == null) {
            this.root = new Node(point, VERTICAL, new RectHV(0.0, 0.0, 1.0, 1.0));
            this.size++;
        }
        else
            insertIntoTree(this.root, point);
    }

    private void insertIntoTree(Node subTreeRoot, Point2D pointToInsert) {
        assert (subTreeRoot != null);

        // Traverse tree recursively until null node is found
        if (subTreeRoot.point.equals(pointToInsert))
            return; // Point already in tree
        else if (subTreeRoot.isVertical)
            insertHorizontally(subTreeRoot, pointToInsert);
        else
            insertVertically(subTreeRoot, pointToInsert);
    }

    private void insertHorizontally(Node subTreeRoot, Point2D pointToInsert) {
        // if pointToInsert smaller, go left, else right
        if (pointToInsert.x() < subTreeRoot.point.x()) {
            insertIntoLeftChild(subTreeRoot, pointToInsert);
        } else {
            insertIntoRightChild(subTreeRoot, pointToInsert);
        }
    }

    private void insertVertically(Node subTreeRoot, Point2D pointToInsert) {
        // if pointToInsert smaller, go left, else right
        if (pointToInsert.y() < subTreeRoot.point.y()) {
            insertIntoLeftChild(subTreeRoot, pointToInsert);
        } else {
            insertIntoRightChild(subTreeRoot, pointToInsert);
        }
    }

    private void insertIntoRightChild(Node parent, Point2D pointToInsert) {
        if (parent.right == null) {
            RectHV rect = generateRightRectangle(parent);
            parent.right = new Node(pointToInsert, parent.isVertical ? HORIZONTAL : VERTICAL, rect);
            this.size++;
        } else
            insertIntoTree(parent.right, pointToInsert);
    }

    private static RectHV generateRightRectangle(Node parent) {
        if (parent.isVertical)
            return new RectHV(parent.point.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
        else
            return new RectHV(parent.rect.xmin(), parent.point.y(), parent.rect.xmax(), parent.rect.ymax());
    }

    private void insertIntoLeftChild(Node subTreeRoot, Point2D pointToInsert) {
        if (subTreeRoot.left == null) {
            RectHV rect = generateLeftRectangle(subTreeRoot);
            subTreeRoot.left = new Node(pointToInsert, subTreeRoot.isVertical ? HORIZONTAL : VERTICAL, rect);
            this.size++;
        } else
            insertIntoTree(subTreeRoot.left, pointToInsert);
    }

    private static RectHV generateLeftRectangle(Node parent) {
        if (parent.isVertical)
            return new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.point.x(), parent.rect.ymax());
        else
            return new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.point.y());
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Point cannot be null.");
        if (this.isEmpty())
            return false;

        return inspectSubTree(this.root, p);
    }

    private boolean inspectSubTree(Node rootToInspect, Point2D p) {
        if (rootToInspect.point.equals(p))
            return true;
        else if (rootToInspect.isVertical)
            return inspectHorizontally(rootToInspect, p);
        else
            return inspectVertically(rootToInspect, p);
    }

    private boolean inspectHorizontally(Node rootToInspect, Point2D p) {
        if (rootToInspect.point.x() > p.x()) {
            return inspectRightChild(rootToInspect, p);
        } else {
            return inspectLeftChild(rootToInspect, p);
        }
    }

    private boolean inspectVertically(Node rootToInspect, Point2D p) {
        double diffY = rootToInspect.point.y() - p.y();
        if (diffY > 0.0) {
            return inspectRightChild(rootToInspect, p);
        } else {
            return inspectLeftChild(rootToInspect, p);
        }
    }

    private boolean inspectLeftChild(Node rootToInspect, Point2D p) {
        if (rootToInspect.left == null)
            return false;
        else
            return inspectSubTree(rootToInspect.left, p);
    }

    private boolean inspectRightChild(Node rootToInspect, Point2D p) {
        if (rootToInspect.right == null)
            return false;
        else
            return inspectSubTree(rootToInspect.right, p);
    }

    public void draw() {
        if (!this.isEmpty()) {
            drawSubTree(this.root);
        }
    }

    //  (all points have x- and y-coordinates between 0 and 1)
    private void drawSubTree(Node rootToDraw) {
        drawPointAndLine(rootToDraw);

        if (rootToDraw.left != null)
            drawSubTree(rootToDraw.left);
        if (rootToDraw.right != null)
            drawSubTree(rootToDraw.right);
    }

    private void drawPointAndLine(Node nodeToDraw) {
        if (nodeToDraw.isVertical) {
            drawVerticalLine(nodeToDraw);
        } else {
            drawHorizontalLine(nodeToDraw);
        }
    }

    private void drawHorizontalLine(Node nodeToDraw) {
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.line(nodeToDraw.rect.xmin(), nodeToDraw.point.y(), nodeToDraw.rect.xmax(), nodeToDraw.point.y());
    }

    private void drawVerticalLine(Node nodeToDraw) {
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.line(nodeToDraw.point.x(), nodeToDraw.rect.ymin(), nodeToDraw.point.x(), nodeToDraw.rect.ymax());
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("Rectangle cannot be null.");
        if (this.isEmpty())
            return new ArrayList<>();

        List<Point2D> pointsInRange = new ArrayList<>();
        inspectSubTreeForRange(this.root, rect, pointsInRange);
        return pointsInRange;
    }

    private void inspectSubTreeForRange(Node rootToInspect, RectHV rect, List<Point2D> pointsInRange) {
        if (rect.contains(rootToInspect.point))
            pointsInRange.add(rootToInspect.point);

        if (rootToInspect.left != null && rect.intersects(rootToInspect.left.rect))
            inspectSubTreeForRange(rootToInspect.left, rect, pointsInRange);
        if (rootToInspect.right != null && rect.intersects(rootToInspect.right.rect))
            inspectSubTreeForRange(rootToInspect.right, rect, pointsInRange);
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Point cannot be null.");
        if (this.isEmpty())
            return null;

        return inspectSubTreeForNearest(this.root, p, this.root.point);
    }

    private Point2D inspectSubTreeForNearest(Node rootToInspect, Point2D pointToInspect, Point2D currentNearest) {
        double currentNearestDistance = currentNearest.distanceSquaredTo(pointToInspect);
        if (rootToInspect.point.distanceSquaredTo(pointToInspect) < currentNearestDistance)
            currentNearest = rootToInspect.point;

        String sideToInspectFirst = sideToInspectFirst(rootToInspect, pointToInspect);
        switch (sideToInspectFirst) {
            case "left" -> {
                currentNearest = inspectLeftChildForNearest(rootToInspect, pointToInspect, currentNearest);
                currentNearest = inspectRightChildForNearest(rootToInspect, pointToInspect, currentNearest);
            }
            case "right" -> {
                currentNearest = inspectRightChildForNearest(rootToInspect, pointToInspect, currentNearest);
                currentNearest = inspectLeftChildForNearest(rootToInspect, pointToInspect, currentNearest);
            }
        }

        return currentNearest;
    }

    private Point2D inspectLeftChildForNearest(Node rootToInspect, Point2D pointToInspect, Point2D currentNearest) {
        if (rootToInspect.left != null && rootToInspect.left.rect.distanceSquaredTo(pointToInspect) < currentNearest.distanceSquaredTo(pointToInspect))
            currentNearest = inspectSubTreeForNearest(rootToInspect.left, pointToInspect, currentNearest);
        return currentNearest;
    }

    private Point2D inspectRightChildForNearest(Node rootToInspect, Point2D pointToInspect, Point2D currentNearest) {
        if (rootToInspect.right != null && rootToInspect.right.rect.distanceSquaredTo(pointToInspect) < currentNearest.distanceSquaredTo(pointToInspect))
            currentNearest = inspectSubTreeForNearest(rootToInspect.right, pointToInspect, currentNearest);
        return currentNearest;
    }

    private String sideToInspectFirst(Node rootToInspect, Point2D pointToInspect) {
        if (rootToInspect.isVertical) {
            if (rootToInspect.point.x() > pointToInspect.x())
                return "left";
            else
                return "right";
        } else {
            if (rootToInspect.point.y() > pointToInspect.y())
                return "left";
            else
                return "right";
        }
    }

    public static void main(String[] args) {
        KdTree kdTree = new KdTree();

        kdTree.insert(new Point2D(0.1, 0.1));
        kdTree.insert(new Point2D(0.5, 0.5));
        assert (kdTree.size() == 2) : "Size is " + kdTree.size() + ", expected 2";

        kdTree.insert(new Point2D(0.5, 0.5));
        assert (kdTree.size() == 2) : "Size is " + kdTree.size() + ", expected 2";

        kdTree.insert(new Point2D(0.25, 0.25));
        kdTree.insert(new Point2D(0.75, 0.75));
        assert (kdTree.size() == 4) : "Size is " + kdTree.size() + ", expected 4";

        assert (kdTree.contains(new Point2D(0.1, 0.1))) : "Point (0.1, 0.1) not found";
        assert (!kdTree.contains(new Point2D(0.6, 0.6))) : "Point (0.6, 30.6) found (should not be there)";

        kdTree.insert(new Point2D(0.6, 0.3));

        RectHV rect = new RectHV(0.1, 0.1, 0.5, 0.5);
        Iterable<Point2D> pointsInRange = kdTree.range(rect);
        assert (pointsInRange.iterator().hasNext()) : "No points in range, expected 2";
        assert (pointsInRange.iterator().next().equals(new Point2D(0.1, 0.1))) : "Point (0.1, 0.1) not found";

        Point2D nearestPoint = kdTree.nearest(new Point2D(0.6, 0.6));
        assert (nearestPoint.equals(new Point2D(0.5, 0.5))) : "Nearest point is " + nearestPoint + ", expected (0.5, 0.5)";
        kdTree.draw();
    }
}