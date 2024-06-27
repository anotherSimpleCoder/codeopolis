package de.htwsaar.esch.Codeopolis.Util;

import java.util.NoSuchElementException;

/**
 * An iterator over a collection.
 *
 * <p>An {@code Iterator} is used to iterate over a collection of elements of type {@code T}.
 * It provides methods to check if there are more elements available and to retrieve those elements.
 *
 * @param <T> the type of elements returned by this iterator
 */
public interface Iterator<T> {
    /**
     * Determines if there are more elements in the list to iterate over.
     *
     * @return true if the next call to next() would return an element rather than throwing an exception,
     *         false if the list has no more elements.
     */
    boolean hasNext();

    /**
     * Returns the next element in the list.
     * This method should be called only if hasNext() returns true.
     *
     * @return the next element in the list.
     * @throws NoSuchElementException if there are no more elements to return.
     */
    T next();
}