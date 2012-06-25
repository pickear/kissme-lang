package com.kissme.lang;

/**
 * 
 * @author loudyn
 * 
 */
public interface Each<WHICH> {

	/**
	 * 
	 * @param which
	 */
	public void invoke(int index, WHICH which);
}
