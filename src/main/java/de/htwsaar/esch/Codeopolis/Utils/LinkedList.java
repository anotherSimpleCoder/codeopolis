package de.htwsaar.esch.Codeopolis.Utils;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class LinkedList<T extends Comparable<T>> implements Iterable<T> {
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
			return i != null;
		}

		@Override
		public T next() throws NoSuchElementException {
			if(!this.hasNext()) {
				throw new NoSuchElementException();
			}
			
			T data = i.data;
			i = i.next;
			return data;
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
			
			tmp = tmp.next;
		}
		
		throw new IndexOutOfBoundsException();
	}
	
	public LinkedList<T> filter(Predicate<? super T> filterPredicate) {
		LinkedList<T> resultList = new LinkedList<T>();
		
		for(T element: this) {
			if(filterPredicate.test(element)) {
				resultList.addLast(element);
			}
		}
		
		return resultList;
	}

	public void forEach(Consumer<? super T> consumerForElements) {
		for(T element: this) {
			consumerForElements.accept(element);
		}
	}
	
	public void removeIf(Predicate<T> removePredicate) {
		for(int i = 0; i < this.size; i++) {
			if(removePredicate.test(this.get(i))) {
				this.remove(i);
			}
		}
	}
	
	public void addIf(T element, Predicate<T> addPredicate) {
		if(addPredicate.test(element)) {
			this.addLast(element);
		}
	}
	
	public double sum(Function<T, Double> function) {
		double result = 0;
		
		for(T element: this) {
			result += function.apply(element);
		}
		
		return result;
	}
	
	@Override
	public Iterator<T> iterator() {
		return this.new ListIterator();
	}
	
	public void sort(Comparator<T> comparator) {
        for (int i = this.size(); i > 0; i--) {
            for (int j = 0; j < i - 1; j++) {
            	if(comparator.compare(this.get(j), this.get(j+1)) == 1) {
                    T temp = this.get(j);
                    this.set(this.get(j+1), j);
                    this.set(temp, j+1);
            	}
            }
        }
	}
	
	public boolean equals(LinkedList<T> obj) {
		if(this.size != obj.size) {
			return false;
		}
		
		
		for(int i = 0; i < this.size; i++) {
			if(this.get(i).compareTo(obj.get(i)) == 0) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");		
		
		for(T data: this) {		
			sb.append(data.toString() + ",");
		}
		
		sb.append("]");
		
		return sb.toString();
	}
}
