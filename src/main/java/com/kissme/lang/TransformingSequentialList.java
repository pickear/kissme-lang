package com.kissme.lang;

import java.io.Serializable;
import java.util.AbstractSequentialList;
import java.util.List;
import java.util.ListIterator;

/**
 * 
 * @author loudyn
 * 
 */
class TransformingSequentialList<F, T> extends AbstractSequentialList<T> implements Serializable {


	final List<F> fromList;
	final Function<? super F, ? extends T> function;

	TransformingSequentialList(List<F> fromList, Function<? super F, ? extends T> function) {
		this.fromList = fromList;
		this.function = function;
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		final ListIterator<F> delegate = fromList.listIterator(index);
		return new ListIterator<T>() {

			@Override
			public boolean hasNext() {
				return delegate.hasNext();
			}

			@Override
			public T next() {
				return function.apply(delegate.next());
			}

			@Override
			public boolean hasPrevious() {
				return delegate.hasPrevious();
			}

			@Override
			public T previous() {
				return function.apply(delegate.previous());
			}

			@Override
			public int nextIndex() {
				return delegate.nextIndex();
			}

			@Override
			public int previousIndex() {
				return delegate.previousIndex();
			}

			@Override
			public void remove() {
				delegate.remove();
			}

			@Override
			public void set(T e) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void add(T e) {
				throw new UnsupportedOperationException();
			}

		};
	}

	@Override
	public int size() {
		return fromList.size();
	}

	@Override
	public void clear() {
		fromList.clear();
	}

	private static final long serialVersionUID = 1L;
}
