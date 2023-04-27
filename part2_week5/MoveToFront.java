import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {
    private static final int R = 256; // extended ASCII

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> alphabet = createAsciiList();

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();

            int index = alphabet.indexOf(c);

            if (index == -1)
                throw new IllegalArgumentException("Invalid input character: " + c);

            alphabet.remove(index);
            alphabet.addFirst(c);

            BinaryStdOut.write(index, 8); // 8 bits per character
        }

        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> alphabet = createAsciiList();

        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readChar();
            char c = alphabet.get(index);

            alphabet.remove(index);
            alphabet.addFirst(c);

            BinaryStdOut.write(c, 8); // 8 bits per character
        }

        BinaryStdOut.close();
    }

    private static LinkedList<Character> createAsciiList() {
        LinkedList<Character> ascii = new LinkedList<>();
        for (char c = 0; c < R; c++)
            ascii.add(c);

        return ascii;
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
            if (args.length != 1 || (!args[0].equals("-") && !args[0].equals("+")))
                throw new IllegalArgumentException("Expected + or - as argument");

            if (args[0].equals("-"))
                encode();
            else
                decode();
    }
}