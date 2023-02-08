import java.util.Iterator;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Object[] o;
    private int N;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.o = new Object[1];
        this.N = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return N;
    }

    // add the item
    public void enqueue(Item item) {
        if (N == o.length) resize(2 * o.length);
        if (item == null) throw new IllegalArgumentException();
        o[N] = item;
        N++;
    }

    private void resize(int capacity) {
        Object[] copy = new Object[capacity];
        for (int i = 0; i < N; i++)
            copy[i] = o[i];
        o = copy;
    }

    // remove and return a random item
    public Item dequeue() {
        if (this.isEmpty()) throw new java.util.NoSuchElementException();
        if (N > 0 && N == o.length/4) resize(o.length/2);

        int random = StdRandom.uniformInt(N);
        Item returnItem = (Item) o[random];

        o[random] = o[N-1];
        o[N-1] = null;
        N--;

        return returnItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (this.isEmpty()) throw new java.util.NoSuchElementException();
        int random = StdRandom.uniformInt(N);
        return (Item) o[random];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() { return new ListIterator(); }

    private class ListIterator implements Iterator<Item> {
        private int i = N;
        private RandomizedQueue<Item> tmpQueue;

        public ListIterator() {
            this.tmpQueue = new RandomizedQueue<>();
            for (int i = 0; i < N; i++) {
                this.tmpQueue.enqueue((Item) o[i]);
            }
        }
        public boolean hasNext() { return i > 0; }
        public void remove() { throw new UnsupportedOperationException(); }
        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            i--;
            return (this.tmpQueue.dequeue());
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> deque = new RandomizedQueue<>();
        deque.enqueue("AAA");
        deque.enqueue("BBB");
        deque.enqueue("CCC");
        deque.enqueue("DDD");
        deque.enqueue("EEE");

        System.out.println("Sampling:");
        System.out.println(deque.sample());
        System.out.println(deque.sample());
        System.out.println(deque.sample());
        System.out.println();


        System.out.println("Iterating:");
        for (String s : deque) {
            System.out.println("Iterator result: " + s);
        }

        System.out.println("Sampling:");
        System.out.println(deque.sample());
        System.out.println(deque.sample());
        System.out.println(deque.sample());

        System.out.println("Size: " + deque.size()); // 5

        System.out.println("Dequeuing:");
        System.out.println(deque.dequeue());
        System.out.println(deque.dequeue());
        System.out.println(deque.dequeue());

        System.out.println("Iterating remaining items:");
        for (String s : deque) {
            System.out.println(s);
        }

    }

}