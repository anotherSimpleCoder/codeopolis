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
		
		Node oldRoot = this.root;
		while(oldRoot.next != null) {
			oldRoot = oldRoot.next;
		}

		oldRoot.next = new Node(data, null);
	}
	
	public T removeFirst() {
		if(this.root == null) {
			return null;
		}
		
		Node newRoot = this.root.next;
		Node deletedRootNode = this.root;
		
		this.root = newRoot;
		this.size--;
		
		return deletedRootNode.data;
	}
	
	public boolean isEmpty() {
		return this.size == 0;
	}
	
	public int size() {
		return this.size;
	}
	
	public T get(int index) throws IndexOutOfBoundsException {		
		Node iterationNode = this.root;
		for(int iterationIndex = 0; iterationIndex < this.size; iterationIndex++) {
			if(iterationIndex == index) {
				return iterationNode.data;
			}
			
			iterationNode = iterationNode.next;
		}
		
		throw new IndexOutOfBoundsException();
	}
	
	public T set(T newData, int index) throws IndexOutOfBoundsException {
		Node iterationNode = this.root;
		for(int iterationIndex = 0; iterationIndex < this.size; iterationIndex++) {
			if(iterationIndex == index) {
				T oldData = iterationNode.data;
				iterationNode.data = newData;
				return oldData;
			}
			
			iterationNode = iterationNode.next;
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
		
		Node iterationNode = this.root;
		for(int iterationIndex = 0; iterationIndex < this.size; iterationIndex++) {
			if(iterationIndex == index-1) {
				Node toBeDeleted = iterationNode.next;
				iterationNode.next = toBeDeleted.next;
				size--;
				
				return toBeDeleted.data;
			}
			
			iterationNode = iterationNode.next;
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
		for(int iterationIndex = 0; iterationIndex < this.size; iterationIndex++) {
			if(removePredicate.test(this.get(iterationIndex))) {
				this.remove(iterationIndex);
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
        for (int unsortedIndex = this.size(); unsortedIndex > 0; unsortedIndex--) {
            for (int iterationIndex = 0; iterationIndex < unsortedIndex - 1; iterationIndex++) {
            	if(comparator.compare(this.get(iterationIndex), this.get(iterationIndex+1)) == 1) {
                    T swapNode = this.get(iterationIndex);
                    this.set(this.get(iterationIndex+1), iterationIndex);
                    this.set(swapNode, iterationIndex+1);
            	}
            }
        }
	}
	
	public boolean equals(LinkedList<T> obj) {
		if(this.size != obj.size) {
			return false;
		}
		
		
		for(int iterationIndex = 0; iterationIndex < this.size; iterationIndex++) {
			if(this.get(iterationIndex).compareTo(obj.get(iterationIndex)) == 0) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("[");		
		
		for(T data: this) {		
			stringBuilder.append(data.toString() + ",");
		}
		
		stringBuilder.append("]");
		
		return stringBuilder.toString();
	}
}
