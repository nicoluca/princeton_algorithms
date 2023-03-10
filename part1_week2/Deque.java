import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    private Node first = null;
    private Node last = null;
    private int numberOfItems = 0;

    private class Node
    {
        Item item;
        Node next;
        Node previous;
    }

    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return this.first == null;
    }

    // return the number of items on the deque
    public int size() {
        return this.numberOfItems;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldFirst = first;
        this.first = new Node();
        this.first.item = item;
        this.first.next = oldFirst;
        if (this.first.next != null) this.first.next.previous = this.first;
        if (this.numberOfItems == 0) this.last = this.first;
        this.numberOfItems++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldLast = last;
        this.last = new Node();
        this.last.item = item;
        this.last.previous = oldLast;
        if (this.last.previous != null) this.last.previous.next = this.last;
        if (this.numberOfItems == 0) this.first = this.last;
        this.numberOfItems++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (this.isEmpty()) throw new java.util.NoSuchElementException();
        Item item = this.first.item;

        this.first = this.first.next;
        this.numberOfItems--;

        if (this.first != null) this.first.previous = null;
        else this.last = null;

        if (this.numberOfItems == 0) this.last = null;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (this.isEmpty()) throw new java.util.NoSuchElementException();
        Item item = this.last.item;

        this.last = this.last.previous;
        this.numberOfItems--;

        if (this.last != null) this.last.next = null;
        else this.first = null;

        if (this.numberOfItems == 0) this.first = null;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext() {  return this.current != null;  }
        public void remove() { throw new UnsupportedOperationException(); }
        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            Item item = this.current.item;
            this.current = this.current.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> integers = new Deque<>();
        integers.addFirst(1);
        integers.addFirst(2);
        integers.addLast(3);

        integers.removeLast(); // 3
        integers.removeLast(); // 2
        integers.removeLast(); // 1
        System.out.println(integers.isEmpty()); // True
        System.out.println(integers.size()); // 0

        Deque<String> strings = new Deque<>();
        strings.addFirst("AAA");
        strings.addFirst("BBB");
        strings.addLast("CCC");

        for (String s : strings) {
            System.out.println(s);
        }

        System.out.println();

        strings.addLast("DDD");
        System.out.println(strings.removeLast()); // DDD
        System.out.println(strings.removeFirst()); // BBB
        System.out.println(strings.removeFirst()); //AAA
        System.out.println(strings.isEmpty()); //False
        System.out.println(strings.size()); // 1

        System.out.println();
        for (String s : strings) {
            System.out.println(s); // CCC
        }

    }

}