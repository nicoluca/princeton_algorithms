import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Stack;

import java.util.HashMap;
import java.util.LinkedList;

public class SAP {
    private final Digraph digraph;
    // Store previous results to avoid recomputing, reult index 0 is length, index 1 is ancestor
    private final HashMap<int[], int[]> previousResults;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException("Graph cannot be null.");

        this.digraph = new Digraph(G);
        this.previousResults = new HashMap<>();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);

        int[] pair = {v, w};
        sapVerticesPair(pair);
        return previousResults.get(pair)[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);

        int[] pair = {v, w};
        sapVerticesPair(pair);
        return previousResults.get(pair)[1];
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateIterable(v);
        validateIterable(w);

        int[] result = sapIterablesPair(v, w);
        return result[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateIterable(v);
        validateIterable(w);

        int[] result = sapIterablesPair(v, w);
        return result[1];
    }

    private void sapVerticesPair(int[] pair) {
        if (previousResults.containsKey(pair))
            return;

        if (checkForEquality(pair))
            return;

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, pair[0]);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, pair[1]);

        int shortestDistance = -1;
        int ancestor = -1;

        for (int ancestorCandidate = 0; ancestorCandidate < digraph.V(); ancestorCandidate++) {
            if (bfsV.hasPathTo(ancestorCandidate) && bfsW.hasPathTo(ancestorCandidate)) {
                int candidateDistance = bfsV.distTo(ancestorCandidate) + bfsW.distTo(ancestorCandidate);

                if (candidateDistance < shortestDistance || shortestDistance == -1) {
                    shortestDistance = candidateDistance;
                    ancestor = ancestorCandidate;
                }
            }
        }

        int[] result = {shortestDistance, ancestor};
        previousResults.put(pair, result);
    }

    private int[] sapIterablesPair(Iterable<Integer> v, Iterable<Integer> w) {
        Stack<int[]> pairs = new Stack<>();
        for (Integer vertex : v) {
            for (Integer vertex2 : w) {
                int[] pair = {vertex, vertex2};
                pairs.push(pair);
                sapVerticesPair(pair);
            }
        }

        int shortestDistance = -1;
        int ancestor = -1;

        for (int[] pair : pairs) {
            int[] result = previousResults.get(pair);
            if (result[0] < shortestDistance || shortestDistance == -1) {
                shortestDistance = result[0];
                ancestor = result[1];
            }
        }

        return new int[]{shortestDistance, ancestor};
    }

    private boolean checkForEquality(int[] pair) {
        if (pair[0] == pair[1]) {
            int[] result = {0, pair[0]};
            previousResults.put(pair, result);
            return true;
        }

        return false;
    }

    private void validateVertex(int vertex) {
        if (vertex < 0 || vertex >= digraph.V())
            throw new IllegalArgumentException("Vertex cannot be negative or out of range.");
    }

    private void validateIterable(Iterable<Integer> v) {
        if (v == null)
            throw new IllegalArgumentException("Iterable cannot be null.");

        for (Integer vertex : v) {
            if (vertex == null)
                throw new IllegalArgumentException("Iterable cannot contain null values.");
            validateVertex(vertex);
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        Digraph G = new Digraph(13);
        // Example from https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php
        G.addEdge(7, 3);
        G.addEdge(8, 3);
        G.addEdge(3, 1);
        G.addEdge(4, 1);
        G.addEdge(5, 1);
        G.addEdge(9, 5);
        G.addEdge(10, 5);
        G.addEdge(11, 10);
        G.addEdge(12, 10);
        G.addEdge(1, 0);
        G.addEdge(2, 0);

        SAP sap = new SAP(G);

        assert sap.length(3, 11) == 4 : "SAP length between 3 and 11 should be 4, was " + sap.length(3, 11);
        assert sap.ancestor(3, 11) == 1 : "SAP ancestor between 3 and 11 should be 1, was " + sap.ancestor(3, 11);
        assert sap.length(9, 12) == 3 : "SAP length between 9 and 12 should be 3, was " + sap.length(9, 12);
        assert sap.ancestor(9, 12) == 5 : "SAP ancestor between 9 and 12 should be 5, was " + sap.ancestor(9, 12);
        assert sap.length(7, 2) == 4 : "SAP length between 7 and 2 should be 4, was " + sap.length(7, 2);
        assert sap.ancestor(7, 2) == 0 : "SAP ancestor between 7 and 2 should be 0, was " + sap.ancestor(7, 2);
        assert sap.length(1, 6) == -1 : "SAP length between 1 and 6 should be -1, was " + sap.length(1, 6);
        assert sap.ancestor(1, 6) == -1 : "SAP ancestor between 1 and 6 should be -1, was " + sap.ancestor(1, 6);

        Iterable<Integer> v = new LinkedList<>();
        ((LinkedList<Integer>) v).add(3);
        ((LinkedList<Integer>) v).add(11);
        Iterable<Integer> w = new LinkedList<>();
        ((LinkedList<Integer>) w).add(9);
        ((LinkedList<Integer>) w).add(12);

        assert sap.length(v, w) == 2 : "SAP length between 3, 11 and 9, 12 should be 2, was " + sap.length(v, w);
        assert sap.ancestor(v, w) == 10 : "SAP ancestor between 3, 11 and 9, 12 should be 5, was " + sap.ancestor(v, w);

        System.out.println("All tests passed.");
    }
}
