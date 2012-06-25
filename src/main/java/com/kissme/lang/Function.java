package com.kissme.lang;

/**
 * 
 * @author loudyn
 * 
 */
public interface Function<F, T> {
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	T apply(F input);
}
