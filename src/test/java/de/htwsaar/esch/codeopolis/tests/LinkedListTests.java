package de.htwsaar.esch.codeopolis.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.htwsaar.esch.Codeopolis.Utils.LinkedList;

public class LinkedListTests {
	private LinkedList<Integer> testList;
	
	@BeforeEach
	public void setup() {
		this.testList = new LinkedList<Integer>();
	}
	
	@Test
	public void testAddLast() throws Exception {
		this.testList.addLast(42);
		this.testList.addLast(32);
		assertEquals(42, this.testList.get(0));
	}
	
	
	@Test
	public void testRemoveFirst() throws Exception {
		this.testList.addLast(42);
		assertEquals(42, this.testList.removeFirst());
	}
	
	@Test
	public void testIsEmpty() {
		assertTrue(this.testList.isEmpty());
		
		this.testList.addLast(32);
		assertFalse(this.testList.isEmpty());
	}
	
	@Test
	public void testSize() {
		assertEquals(0, this.testList.size());
		
		for(int i = 0; i < 10; i++) {
			this.testList.addLast(42);
		}
		
		assertEquals(10, this.testList.size());
	}
	
	@Test
	public void testSet() throws Exception {
		this.testList.addLast(42);
		this.testList.addLast(69);
		this.testList.addLast(420);
		
		this.testList.set(3, 0);
		assertEquals(3, this.testList.get(0));
	}
	
	@Test
	public void testClear() {
		for(int i = 0; i < 10; i++) {
			this.testList.addLast(42);
		}
		
		this.testList.clear();
		assertTrue(this.testList.isEmpty());
	}
	
	@Test
	public void testRemove() throws Exception {
		this.testList.addLast(42);
		this.testList.addLast(69);
		this.testList.addLast(420);
		
		assertEquals(69, this.testList.remove(1));
		assertEquals(420, this.testList.remove(1));
	}
	
	@Test
	public void testIterator() {
		for(int i = 0; i < 10; i++) {
			this.testList.addLast(42);
		}
		
		for(int i: this.testList) {
			assertEquals(42, i);
		}
	}
}
