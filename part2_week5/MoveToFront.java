import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256; // extended ASCII
    private static final char[] ascii = createAscii();

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int index = -1;

            // Find the index of the character in the array
            for (int i = 0; i < R; i++)
                if (ascii[i] == c) {
                    index = i;
                    break;
                }

            bringToFront(c, index);

            BinaryStdOut.write(index, 8); // 8 bits per character
        }

        BinaryStdOut.close();
    }


    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {

        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readChar();
            char c = ascii[index];

            bringToFront(c, index);

            BinaryStdOut.write(c, 8); // 8 bits per character
        }

        BinaryStdOut.close();
    }

    private static void bringToFront(char c, int index) {
        // Move the character to the front, shifting the rest of the array
        System.arraycopy(ascii, 0, ascii, 1, index);
        ascii[0] = c;
    }

    private static char[] createAscii() {
        char[] ascii = new char[R];
        for (int i = 0; i < R; i++)
            ascii[i] = (char) i;
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