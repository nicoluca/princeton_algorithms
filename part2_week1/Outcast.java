public class Outcast {
    private final WordNet wordnet;
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null)
            throw new IllegalArgumentException("WordNet cannot be null.");

        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        if (nouns == null)
            throw new IllegalArgumentException("Nouns cannot be null.");

        return calculateDistances(nouns);
    }

    private String calculateDistances(String[] nouns) {
        int maxDistance = Integer.MIN_VALUE;
        String outcast = null;

        for (String noun : nouns) {
            int distance = 0;
            for (String otherNoun : nouns) {
                distance += this.wordnet.distance(noun, otherNoun);
            }

            if (distance > maxDistance) {
                maxDistance = distance;
                outcast = noun;
            }
        }

        return outcast;
    }

    // see test client below
    public static void main(String[] args) {
    }
}