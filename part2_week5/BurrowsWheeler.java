import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String input = BinaryStdIn.readString();

        CircularSuffixArray csa = new CircularSuffixArray(input);
        int index = -1;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < csa.length(); i++) {
            sb.append(input.charAt((csa.index(i) + input.length() - 1) % input.length()));
            if (csa.index(i) == 0)
                index = i;
        }

        if (index == -1)
            throw new IllegalArgumentException("Invalid input string: " + input);

        BinaryStdOut.write(index);
        BinaryStdOut.write(sb.toString());
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int index = BinaryStdIn.readInt();
        String lastRow = BinaryStdIn.readString();

        char[] sorted = lastRow.toCharArray();
        Arrays.sort(sorted);

        int[] next = constructNext(lastRow);

        for (int i = 0; i < lastRow.length(); i++) {
            BinaryStdOut.write(sorted[index]);
            index = next[index];
        }

        BinaryStdOut.close();
    }

    private static int[] constructNext(String input) {
        // key-indexed counting, as suggested in the assignment FAQ
        int[] next = new int[input.length()];
        // extended ASCII
        int r = 256;
        int[] count = new int[r + 1];

        for (int i = 0; i < input.length(); i++)
            count[input.charAt(i) + 1]++;

        for (int i = 0; i < r; i++)
            count[i + 1] += count[i];

        for (int i = 0; i < input.length(); i++)
            next[count[input.charAt(i)]++] = i;

        return next;
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (!(args[0].equals("-") || args[0].equals("+")))
            throw new IllegalArgumentException("Invalid program arguments: " + Arrays.toString(args));

        if (args[0].equals("-"))
            transform();
        else
            inverseTransform();
    }

}
