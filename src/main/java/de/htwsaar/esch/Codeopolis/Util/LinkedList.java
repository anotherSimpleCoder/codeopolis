package de.htwsaar.esch.Codeopolis.Util;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

/**
 * A generic class for a singly linked list.
 * This class provides basic methods for adding, removing, and accessing elements.
 *
 * @param <T> The type of elements in the list.
 */
public class LinkedList<T extends Comparable<T>> {
    /**
     * Internal node class to hold data and link to the next node.
     */
    private class Node {
        T data;
        Node next;

        /**
         * Node constructor.
         *
         * @param data The data item to be held by this node.
         */
        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;  // Head of the list
    private int size;   // Number of elements in the list

    /**
     * Constructor for MyLinkedList.
     * Initializes an empty list.
     */
    public LinkedList() {
        head = null;
        size = 0;
    }

    /**
     * Adds an element to the end of the list.
     *
     * @param item The element to be added.
     */
    public void addLast(T item) {
        Node newNode = new Node(item);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    /**
     * Adds all elements from another LinkedList to this LinkedList.
     *
     * @param other The LinkedList containing elements to be added.
     */
    public void addAll(LinkedList<T> other) {
        if (other == null || other.isEmpty()) {
            return;
        }
        Iterator<T> iterator = other.iterator();
        while (iterator.hasNext()) {
            this.addLast(iterator.next());
        }
    }

    /**
     * Removes the first element of the list and returns it.
     * If the list is empty, returns null.
     *
     * @return The removed element or null if the list is empty.
     */
    public T removeFirst() {
        if (head == null) return null;
        T data = head.data;
        head = head.next;
        size--;
        return data;
    }

    /**
     * Checks if the list is empty.
     *
     * @return true if the list is empty, false otherwise.
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Returns the size of the list.
     *
     * @return The number of elements in the list.
     */
    public int size() {
        return size;
    }

    /**
     * Retrieves the element at a specified index.
     *
     * @param index The index of the element to retrieve.
     * @return The element at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of bounds (index < 0 || index >= size).
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + size);
        }
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    /**
     * Replaces the element at a specified index with a new element.
     * Returns the element previously at the index.
     *
     * @param index The index of the element to replace.
     * @param element The new element to be set at the index.
     * @return The element previously at the index.
     * @throws IndexOutOfBoundsException If the index is out of bounds (index < 0 || index >= size).
     */
    public T set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + size);
        }
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        T oldData = current.data;
        current.data = element;
        return oldData;
    }

    /**
     * Clears all elements from the list.
     * After this method is called, the list will be empty.
     */
    public void clear() {
        head = null;  // Remove the reference to the head node, allowing garbage collection
        size = 0;     // Reset the size of the list
    }

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left (subtracts one from their indices).
     *
     * @param index the index of the element to be removed
     * @return the element that was removed from the list
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
     */
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + size);
        }
        Node prev = null;
        Node current = head;

        if (index == 0) {
            head = head.next; // Move head
        } else {
            // Traverse to the element before the one to be removed
            for (int i = 0; i < index; i++) {
                prev = current;
                current = current.next;
            }
            // Remove the element
            prev.next = current.next;
        }
        size--;
        return current.data;
    }

    /**
     * Returns an iterator that provides sequential access to the elements in this list.
     *
     * @return an Iterator instance capable of iterating over the elements of the list in sequence.
     */
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T> {
        private Node current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (current == null) {
                throw new NoSuchElementException("No more elements in the list");
            }
            T data = current.data;
            current = current.next;
            return data;
        }
    }

    /**
     * Sorts the elements of the list using the Bubble Sort algorithm.
     */
    public void sort() {
        if (size <= 1) {
            return;
        }

        for (int n = size; n > 1; n--) {
            Node current = head;
            for (int i = 0; i < n - 1; i++) {
                Node next = current.next;
                if (current.data.compareTo(next.data) > 0) {
                    // Swap current and next data
                    T temp = current.data;
                    current.data = next.data;
                    next.data = temp;
                }
                current = current.next;
            }
        }
    }

    /**
     * Filters the elements of the list based on the provided predicate.
     *
     * @param predicate The predicate used to filter elements.
     * @return A new LinkedList containing elements that match the predicate.
     */
    public LinkedList<T> filter(Predicate<? super T> predicate) {
        LinkedList<T> filteredList = new LinkedList<>();
        Node current = head;
        while (current != null) {
            if (predicate.test(current.data)) {
                filteredList.addLast(current.data);
            }
            current = current.next;
        }
        return filteredList;
    }

    /**
     * Performs the given action for each element of the list.
     *
     * @param action The action to be performed for each element.
     */
    public void forEach(Consumer<? super T> action) {
        Node current = head;
        while (current != null) {
            action.accept(current.data);
            current = current.next;
        }
    }

    /**
     * Removes all elements of the list that satisfy the given predicate.
     *
     * @param filter The predicate used to remove elements.
     * @return true if any elements were removed, false otherwise.
     */
    public boolean removeIf(Predicate<? super T> filter) {
        boolean removed = false;
        Node current = head;
        Node prev = null;

        while (current != null) {
            if (filter.test(current.data)) {
                if (prev == null) {
                    head = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                removed = true;
            } else {
                prev = current;
            }
            current = current.next;
        }
        return removed;
    }

    /**
     * Adds an element to the list if it satisfies the given predicate.
     *
     * @param item The element to be added.
     * @param condition The predicate used to test the element.
     * @return true if the element was added, false otherwise.
     */
    public boolean addIf(T item, Predicate<? super T> condition) {
        if (condition.test(item)) {
            addLast(item);
            return true;
        }
        return false;
    }

    /**
     * Sorts the elements of the list based on the provided comparator.
     *
     * @param comparator The comparator used to determine the order of the list.
     */
    public void sort(Comparator<? super T> comparator) {
        if (size <= 1) {
            return;
        }

        for (int n = size; n > 1; n--) {
            Node current = head;
            for (int i = 0; i < n - 1; i++) {
                Node next = current.next;
                if (comparator.compare(current.data, next.data) > 0) {
                    // Swap current and next data
                    T temp = current.data;
                    current.data = next.data;
                    next.data = temp;
                }
                current = current.next;
            }
        }
    }

    /**
     * Sums the elements of the list based on the provided function.
     *
     * @param mapper The function to apply to each element to get the value to be summed.
     * @return The sum of the mapped values.
     */
    public double sum(ToDoubleFunction<? super T> mapper) {
        double sum = 0;
        Node current = head;
        while (current != null) {
            sum += mapper.applyAsDouble(current.data);
            current = current.next;
        }
        return sum;
    }

}
