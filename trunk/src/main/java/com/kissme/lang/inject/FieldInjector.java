package com.kissme.lang.inject;

import java.lang.reflect.Field;

import com.kissme.lang.Lang;

/**
 * 
 * @author loudyn
 * 
 */
public class FieldInjector implements Injector {
	private final Object me;
	private final Field field;

	/**
	 * 
	 * @param me
	 * @param field
	 */
	public FieldInjector(Object me, Field field) {
		this.me = me;
		this.field = field;
		this.field.setAccessible(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.lang.inject.Injecting#inject(java.lang.Object)
	 */
	public void inject(Object value) {
		try {

			field.set(me, value);
		} catch (Exception e) {
			throw Lang.uncheck(e);
		}
	}

}
