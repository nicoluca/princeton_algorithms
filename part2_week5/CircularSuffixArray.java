import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {
    private final String string;
    private final int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException("String cannot be null.");

        this.string = s; // String is immutable and string.length() is O(1)
        this.index = new int[s.length()];

        initializeIndex();
    }

    private void initializeIndex() {
        CircularSuffix[] suffixes = new CircularSuffix[string.length()];

        for (int i = 0; i < string.length(); i++)
            suffixes[i] = new CircularSuffix(string, i);

        Arrays.sort(suffixes); // O(N * log(N)), comparable is implemented in CircularSuffix

        for (int i = 0; i < string.length(); i++)
            index[i] = suffixes[i].offset;
    }

    // length of s
    public int length() {
        return string.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length())
            throw new IllegalArgumentException("Index out of bounds.");

        return index[i];
    }

    private class CircularSuffix implements Comparable<CircularSuffix> {
        private final int offset;

        public CircularSuffix(String s, int offset) {
            this.offset = offset;
        }

        // Not fastest but 'cleanest' implementation according to assignment FAQ
        @Override
        public int compareTo(CircularSuffix o) {
            if (this == o)
                return 0;

            int thisOffset = offset;
            int otherOffset = o.offset;
            int current = 0;

            while (current < string.length()) {
                if (string.charAt(thisOffset) < string.charAt(otherOffset))
                    return -1;
                else if (string.charAt(thisOffset) > string.charAt(otherOffset))
                    return 1;
                else {
                    thisOffset = (thisOffset + 1) % string.length();
                    otherOffset = (otherOffset + 1) % string.length();
                    current++;
                }
            }

            return 0;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        String input = "ABRACADABRA!";
        CircularSuffixArray csa = new CircularSuffixArray(input);

        assert csa.length() == 12 : "Length should be 12, was " + csa.length();
        // Test input from https://coursera.cs.princeton.edu/algs4/assignments/burrows/specification.php
        assert csa.index(0) == 11 : "Index 0 should be 11, was " + csa.index(0);
        assert csa.index(1) == 10 : "Index 1 should be 10, was " + csa.index(1);
        assert csa.index(2) == 7 : "Index 2 should be 7, was " + csa.index(2);
        assert csa.index(3) == 0 : "Index 3 should be 0, was " + csa.index(3);
        assert csa.index(4) == 3 : "Index 4 should be 3, was " + csa.index(4);
        assert csa.index(5) == 5 : "Index 5 should be 5, was " + csa.index(5);
        assert csa.index(6) == 8 : "Index 6 should be 8, was " + csa.index(6);
        assert csa.index(7) == 1 : "Index 7 should be 1, was " + csa.index(7);
        assert csa.index(8) == 4 : "Index 8 should be 4, was " + csa.index(8);
        assert csa.index(9) == 6 : "Index 9 should be 6, was " + csa.index(9);
        assert csa.index(10) == 9 : "Index 10 should be 9, was " + csa.index(10);
        assert csa.index(11) == 2 : "Index 11 should be 2, was " + csa.index(11);

        // Print ll suffixes
        StdOut.print("Sorted suffixes:\n");
        for (int i = 0; i < csa.length(); i++) {
            for (int j = 0; j < csa.length(); j++)
                StdOut.print(input.charAt((csa.index(i) + j) % csa.length()) + " ");
            StdOut.print("\n");
        }
        StdOut.println("All tests passed.");
    }
}