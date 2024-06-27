package de.htwsaar.esch.codeopolis.tests;

import de.htwsaar.esch.Codeopolis.Util.LinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListTest {

    private LinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new LinkedList<>();
    }

    @Test
    void testAddLast() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    void testRemoveFirst() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertEquals(1, list.removeFirst());
        assertEquals(2, list.size());
        assertEquals(2, list.get(0));
    }

    @Test
    void testIsEmpty() {
        assertTrue(list.isEmpty());
        list.addLast(1);
        assertFalse(list.isEmpty());
    }

    @Test
    void testSize() {
        assertEquals(0, list.size());
        list.addLast(1);
        assertEquals(1, list.size());
        list.addLast(2);
        assertEquals(2, list.size());
    }

    @Test
    void testGet() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(3));
    }

    @Test
    void testSet() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertEquals(2, list.set(1, 4));
        assertEquals(4, list.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(3, 5));
    }

    @Test
    void testClear() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.clear();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    void testRemove() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertEquals(2, list.remove(1));
        assertEquals(2, list.size());
        assertEquals(3, list.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(2));
    }

    @Test
    void testSort() {
        list.addLast(3);
        list.addLast(1);
        list.addLast(2);
        list.sort();
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    void testFilter() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        LinkedList<Integer> filteredList = list.filter(n -> n > 1);
        assertEquals(2, filteredList.size());
        assertEquals(2, filteredList.get(0));
        assertEquals(3, filteredList.get(1));
    }

    @Test
    void testForEach() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        StringBuilder sb = new StringBuilder();
        list.forEach(sb::append);
        assertEquals("123", sb.toString());
    }

    @Test
    void testRemoveIf() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        boolean removed = list.removeIf(n -> n > 1);
        assertTrue(removed);
        assertEquals(1, list.size());
        assertEquals(1, list.get(0));
    }

    @Test
    void testAddIf() {
        boolean added = list.addIf(1, n -> n > 0);
        assertTrue(added);
        assertEquals(1, list.size());
        assertEquals(1, list.get(0));
    }

    @Test
    void testSortWithComparator() {
        list.addLast(3);
        list.addLast(1);
        list.addLast(2);
        list.sort(Comparator.reverseOrder());
        assertEquals(3, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(1, list.get(2));
    }

    @Test
    void testSum() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        double sum = list.sum(Integer::doubleValue);
        assertEquals(6.0, sum);
    }

    @Test
    void testAddAll() {
        LinkedList<Integer> other = new LinkedList<>();
        other.addLast(4);
        other.addLast(5);
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.addAll(other);
        assertEquals(5, list.size());
        assertEquals(4, list.get(3));
        assertEquals(5, list.get(4));
    }
}
