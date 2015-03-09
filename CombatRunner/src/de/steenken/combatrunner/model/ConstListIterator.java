package de.steenken.combatrunner.model;

import java.util.ListIterator;

public class ConstListIterator<T> implements ListIterator<T> {

	private final ListIterator<T> listIt;
	
	public ConstListIterator(final ListIterator<T> listIt) {
		this.listIt = listIt;
	}
	
	private void unsupported() {
		throw new UnsupportedOperationException("This iterator does not allow modificatons");
	}
	
	@Override
	public void add(T arg0) {
		unsupported();
	}

	@Override
	public boolean hasNext() {
		return listIt.hasNext();
	}

	@Override
	public boolean hasPrevious() {
		return listIt.hasPrevious();
	}

	@Override
	public T next() {
		return listIt.next();
	}

	@Override
	public int nextIndex() {
		return listIt.nextIndex();
	}

	@Override
	public T previous() {
		return listIt.previous();
	}

	@Override
	public int previousIndex() {
		return listIt.previousIndex();
	}

	@Override
	public void remove() {
		unsupported();
	}

	@Override
	public void set(T arg0) {
		unsupported();
	}

}
