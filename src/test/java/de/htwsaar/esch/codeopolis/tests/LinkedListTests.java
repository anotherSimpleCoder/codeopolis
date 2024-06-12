package de.htwsaar.esch.codeopolis.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.Random;

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
	public void testFilter() throws Exception {
		for(int i = 0; i < 100; i++) {
			this.testList.addLast(i);
		}
		
		LinkedList<Integer> resultValues = this.testList.filter((number)->number % 2 == 0);
		
		for(int value: resultValues) {
			assertTrue(value % 2 == 0);
		}
	}
	
	@Test
	public void testRemoveIf() throws Exception {
		for(int i = 0; i < 100; i++) {
			this.testList.addLast(i);
		}
		
		this.testList.removeIf((number)-> number % 2 == 0);
	
		for(int number: this.testList) {
			assertTrue(number % 2 == 1);
		}
	}
	
	@Test
	public void testAddIf() throws Exception {
		for(int i = 0; i < 100; i++) {
			this.testList.addIf(i, (number)-> number % 2 == 0);
		}
		
		for(int number: this.testList) {
			assertTrue(number % 2 == 0);
		}
	}
	
	@Test
	public void testSum() {
		for(int i = 0; i <= 100; i++) {
			this.testList.addLast(i);
		}
		
		double sum = this.testList.sum((number)->number.doubleValue());
		assertEquals(5050.0f, sum);
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
	
	@Test
	public void testSort() {
		Random random = new Random();
		
		for(int i = 0; i < 10; i++) {
			int number = random.nextInt();
			this.testList.addLast(number);
		}
		
		this.testList.sort((number1, number2) -> number1.compareTo(number2));
		
		for(int i = 0; i < 9; i++) {
			int curr = this.testList.get(i);
			int next = this.testList.get(i+1);
			
			assertTrue(curr < next);
		}
	}
}
