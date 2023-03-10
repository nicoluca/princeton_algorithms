import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;

public class WordNet {
    private final Digraph digraph;
    private final HashMap<Integer, String> synsets;
    private final HashMap<String, Bag<Integer>> nouns;
    private final SAP sap;


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("Arguments cannot be null.");

        this.nouns = new HashMap<>();
        this.synsets = new HashMap<>();

        int synsetCount = readSynsets(synsets);
        this.digraph = new Digraph(synsetCount);

        readHypernyms(hypernyms);

        this.sap = new SAP(this.digraph);
    }


    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return this.nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException("Word cannot be null.");

        return this.nouns.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        checkNoun(nounA);
        checkNoun(nounB);

        Bag<Integer> v = this.nouns.get(nounA);
        Bag<Integer> w = this.nouns.get(nounB);

        return this.sap.length(v, w);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        checkNoun(nounA);
        checkNoun(nounB);

        Bag<Integer> v = this.nouns.get(nounA);
        Bag<Integer> w = this.nouns.get(nounB);
        int ancestor = this.sap.ancestor(v, w);

        return this.synsets.get(ancestor);
    }

    private void checkNoun(String noun) {
        if (noun == null)
            throw new IllegalArgumentException("Noun cannot be null.");

        if (!this.nouns.containsKey(noun))
            throw new IllegalArgumentException(String.format("Noun %s not found.", noun));
    }

    private int readSynsets(String synsets) {
        In in = new In(synsets);
        int count = 0;

        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] fields = line.split(",");
            int id = Integer.parseInt(fields[0]);
            String synset = fields[1];
            this.synsets.put(id, synset);

            String[] nouns = synset.split(" ");

            for (String noun : nouns) {
                if (!this.nouns.containsKey(noun))
                    this.nouns.put(noun, new Bag<>());

                this.nouns.get(noun).add(id);
            }

            count++;
        }
        return count;
    }

    private void readHypernyms(String hypernyms) {
        In in = new In(hypernyms);

        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] fields = line.split(",");
            int id = Integer.parseInt(fields[0]);

            for (int i = 1; i < fields.length; i++) {
                int hypernymId = Integer.parseInt(fields[i]);
                this.digraph.addEdge(id, hypernymId);
            }
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String synsets = System.getProperty("user.dir") + "/ressources/synsets.txt";
        String hypernyms = System.getProperty("user.dir") + "/ressources/hypernyms.txt";
        WordNet wordNet = new WordNet(synsets, hypernyms);

        assert wordNet.distance("b", "c") == 2 : "Expected 2 for distance between a and b, got " + wordNet.distance("b", "c");
        assert wordNet.sap("b", "c").equals("a") : "Expected a for ancestor between a and b, got " + wordNet.sap("b", "c");
        assert !wordNet.isNoun("d") : "Did not expect d to not be a noun, got " + wordNet.isNoun("d");
        assert wordNet.isNoun("a") : "Expected a to be a noun, got " + wordNet.isNoun("a");
        System.out.println("All tests passed.");
    }
}