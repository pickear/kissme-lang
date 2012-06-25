package com.kissme.lang;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

/**
 * 
 * @author loudyn
 * 
 */
class TransformingRandomAccessList<F, T> extends AbstractList<T> implements RandomAccess, Serializable {

	final List<F> fromList;
	final Function<? super F, ? extends T> function;

	TransformingRandomAccessList(List<F> fromList, Function<? super F, ? extends T> function) {
		this.fromList = fromList;
		this.function = function;
	}

	@Override
	public T get(int index) {
		return function.apply(fromList.get(index));
	}

	@Override
	public int size() {
		return fromList.size();
	}

	@Override
	public T remove(int index) {
		return function.apply(fromList.remove(index));
	}

	@Override
	public boolean isEmpty() {
		return fromList.isEmpty();
	}

	@Override
	public void clear() {
		fromList.clear();
	}

	private static final long serialVersionUID = 1L;
}
