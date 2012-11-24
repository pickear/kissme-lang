package com.kissme.lang;

import java.util.Iterator;

/**
 * 
 * @author loudyn
 * 
 */
public final class Strings {

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static String capitalize(String s) {
		if (null == s) {
			return null;
		}
		if (s.length() == 0) {
			return "";
		}

		char char0 = s.charAt(0);
		if (Character.isUpperCase(char0)) {
			return s.toString();
		}

		return new StringBuilder(s.length()).append(Character.toUpperCase(char0)).append(s.subSequence(1, s.length())).toString();
	}

	/**
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isEmpty(String text) {
		if (null == text) {
			return true;
		}

		return text.length() <= 0;
	}

	/**
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isBlank(String text) {
		if (isEmpty(text)) {
			return true;
		}

		int length = text.length();
		for (int i = 0; i < length; i++) {
			if (!Character.isWhitespace(text.charAt(i))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 
	 * @param text
	 * @param prefix
	 * @return
	 */
	public static boolean startsWith(String text, String prefix) {
		if (isBlank(text)) {
			return false;
		}

		return text.startsWith(prefix);
	}

	/**
	 * 
	 * @param text
	 * @param suffix
	 * @return
	 */
	public static boolean endsWith(String text, String suffix) {
		if (isBlank(text)) {
			return false;
		}

		return text.endsWith(suffix);
	}

	/**
	 * 
	 * @param text
	 * @param delimiter
	 * @return
	 */
	public static String[] split(String text, String delimiter) {
		if (isEmpty(text)) {
			return new String[] {};
		}

		return text.split(delimiter);
	}

	/**
	 * 
	 * @param it
	 * @param delimiter
	 * @return
	 */
	public static String join(Iterable<String> it, String delimiter) {
		Preconditions.isNull(it);
		Preconditions.isNull(delimiter);

		StringBuilder buf = new StringBuilder();
		Iterator<String> iterator = it.iterator();
		while (iterator.hasNext()) {
			buf.append(iterator.next()).append(delimiter);
		}

		return buf.substring(0, buf.length() - 1);
	}

	/**
	 * 
	 * @param text
	 * @return
	 */
	public static String nullToEmpty(String text) {
		return null == text ? "" : text;
	}

	/**
	 * 
	 * @param text
	 * @return
	 */
	public static String emptyToNull(String text) {
		return isEmpty(text) ? null : text;
	}

	/**
	 * 
	 * @param text
	 * @param minLength
	 * @param padChar
	 * @return
	 */
	public static String padLeft(String text, int minLength, char padChar) {
		Preconditions.notNull(text);
		if (text.length() >= minLength) {
			return text;
		}

		StringBuilder buf = new StringBuilder(minLength);
		for (int i = text.length(); i < minLength; i++) {
			buf.append(padChar);
		}

		return buf.append(text).toString();
	}

	/**
	 * 
	 * @param text
	 * @param minLength
	 * @param padChar
	 * @return
	 */
	public static String padRight(String text, int minLength, char padChar) {
		Preconditions.notNull(text);
		if (text.length() >= minLength) {
			return text;
		}

		StringBuilder buf = new StringBuilder(minLength).append(text);
		for (int i = text.length(); i < minLength; i++) {
			buf.append(padChar);
		}

		return buf.toString();
	}

	/**
	 * 
	 * @param text
	 * @param count
	 * @return
	 */
	public static String repeat(String text, int count) {
		Preconditions.notNull(text);
		Preconditions.isTrue(count > 0);

		int len = text.length();
		long longSize = (long) len * (long) count;
		int size = (int) longSize;
		Preconditions.isTrue(
								size == longSize,
								new ArrayIndexOutOfBoundsException("Required array size too large: " + String.valueOf(longSize))
				);

		final char[] array = new char[size];
		text.getChars(0, len, array, 0);
		int n;
		for (n = len; n < size - n; n <<= 1) {
			System.arraycopy(array, 0, array, n, n);
		}
		System.arraycopy(array, 0, array, n, size - n);
		return new String(array);
	}

	private Strings() {}
}
