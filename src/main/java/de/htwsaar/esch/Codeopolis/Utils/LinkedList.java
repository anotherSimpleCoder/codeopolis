package de.htwsaar.esch.Codeopolis.Utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<T> implements Iterable<T> {
	public class Node {
		private T data;
		private Node next;
		
		public Node(T data, Node next) {
			this.data = data;
			this.next = next;
		}
	}

	private class ListIterator implements Iterator<T> {
		private Node i = root;
		
		@Override
		public boolean hasNext() {
			return i.next != null;
		}

		@Override
		public T next() throws NoSuchElementException {
			if(!this.hasNext()) {
				throw new NoSuchElementException();
			}
			
			i = i.next;
			return i.data;
		}
		
	}
	
	private Node root;
	private int size;
	
	public LinkedList() {
		this.root = null;
		this.size = 0;
	}

	public void addLast(T data) {
		this.size++;
		if(this.root == null) {
			this.root = new Node(data, null);
			return;
		}
		
		Node tmp = this.root;
		while(tmp.next != null) {
			tmp = tmp.next;
		}

		tmp.next = new Node(data, null);
	}
	
	public T removeFirst() {
		if(this.root == null) {
			return null;
		}
		
		Node newRoot = this.root.next;
		Node result = this.root;
		this.root = newRoot;
		this.size--;
		
		return result.data;
	}
	
	public boolean isEmpty() {
		return this.size == 0;
	}
	
	public int size() {
		return this.size;
	}
	
	public T get(int index) throws IndexOutOfBoundsException {		
		Node tmp = this.root;
		for(int j = 0; j < this.size; j++) {
			if(j == index) {
				return tmp.data;
			}
			
			tmp = tmp.next;
		}
		
		throw new IndexOutOfBoundsException();
	}
	
	public T set(T newData, int index) throws IndexOutOfBoundsException {
		Node tmp = this.root;
		for(int j = 0; j < this.size; j++) {
			if(j == index) {
				T oldData = tmp.data;
				tmp.data = newData;
				return oldData;
			}
			
			tmp = tmp.next;
		}
		
		throw new IndexOutOfBoundsException();
	}

	public void clear() {
		this.root = null;
		this.size = 0;
	}

	public T remove(int index) throws IndexOutOfBoundsException {
		if(index == 0) {
			return this.removeFirst();
		}
		
		Node tmp = this.root;
		for(int j = 0; j < this.size; j++) {
			if(j == index-1) {
				Node toBeDeleted = tmp.next;
				tmp.next = toBeDeleted.next;
				size--;
				
				return toBeDeleted.data;
			}
		}
		
		throw new IndexOutOfBoundsException();
	}

	@Override
	public Iterator<T> iterator() {
		return this.new ListIterator();
	}
	
	public boolean equals(LinkedList<T> obj) {
		if(this.size != obj.size) {
			return false;
		}
		
		
		for(int i = 0; i < this.size; i++) {
			if(this.get(i) != obj.get(i)) {
				return false;
			}
		}
		
		return true;
	}
}
