/*******************************************************************************
 * Copyright (c) 2013-2017 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.failure;

import java.util.Collection;
import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * Check and throw failures if needed.
 *
 * @author bhausen
 */
public abstract class FailureAssert {
	/**
	 * Assert that the given text does not contain the given substring.
	 *
	 * <pre class="code">
	 * Assert.doesNotContain(name, &quot;rod&quot;, &quot;Name must not contain 'rod'&quot;);
	 * </pre>
	 *
	 * @param textToSearch
	 *            the text to search
	 * @param substring
	 *            the substring to find within the text
	 * @param errorCode
	 *            the error code to use if the assertion fails
	 */
	public static void doesNotContain(final String textToSearch,
			final String substring, final String errorCode) {
		if (StringUtils.hasLength(textToSearch)
				&& StringUtils.hasLength(substring)
				&& textToSearch.indexOf(substring) != -1) {
			BaseFailureHandler.throwFailureException(errorCode, textToSearch);
		}
	}

	/**
	 * Assert that the given text does not contain the given substring.
	 *
	 * <pre class="code">
	 * Assert.doesNotContain(name, &quot;rod&quot;, &quot;Name must not contain 'rod'&quot;);
	 * </pre>
	 *
	 * @param textToSearch
	 *            the text to search
	 * @param substring
	 *            the substring to find within the text
	 * @param errorCode
	 *            the error code to use if the assertion fails
	 * @param data
	 *            additional information to the error code.
	 */
	public static void doesNotContain(final String textToSearch,
			final String substring, final String errorCode,
			final Object... data) {
		if (StringUtils.hasLength(textToSearch)
				&& StringUtils.hasLength(substring)
				&& textToSearch.indexOf(substring) != -1) {
			BaseFailureHandler.throwFailureException(errorCode, textToSearch,
					data);
		}
	}

	/**
	 * Assert that the given String is not empty; that is, it must not be
	 * <code>null</code> and not the empty String.
	 *
	 * <pre class="code">
	 * Assert.hasLength(name, &quot;Name must not be empty&quot;);
	 * </pre>
	 *
	 * @param text
	 *            the String to check
	 * @param errorCode
	 *            the error code to use if the assertion fails
	 * @see StringUtils#hasLength
	 */
	public static void hasLength(final String text, final String errorCode) {
		if (!StringUtils.hasLength(text)) {
			BaseFailureHandler.throwFailureException(errorCode, text);
		}
	}

	/**
	 * Assert that the given String is not empty; that is, it must not be
	 * <code>null</code> and not the empty String.
	 *
	 * <pre class="code">
	 * Assert.hasLength(name, &quot;Name must not be empty&quot;);
	 * </pre>
	 *
	 * @param text
	 *            the String to check
	 * @param errorCode
	 *            the error code to use if the assertion fails
	 * @param data
	 *            additional information to the error code.
	 * @see StringUtils#hasLength
	 */
	public static void hasLength(final String text, final String errorCode,
			final Object... data) {
		if (!StringUtils.hasLength(text)) {
			BaseFailureHandler.throwFailureException(errorCode, text, data);
		}
	}

	/**
	 * Assert that the given String has valid text content; that is, it must not
	 * be <code>null</code> and must contain at least one non-whitespace
	 * character.
	 *
	 * <pre class="code">
	 * Assert.hasText(name, &quot;'name' must not be empty&quot;);
	 * </pre>
	 *
	 * @param text
	 *            the String to check
	 * @param errorCode
	 *            the error code to use if the assertion fails
	 * @see StringUtils#hasText
	 */
	public static void hasText(final String text, final String errorCode) {
		if (!StringUtils.hasText(text)) {
			BaseFailureHandler.throwFailureException(errorCode, text);
		}
	}

	/**
	 * Assert that the given String has valid text content; that is, it must not
	 * be <code>null</code> and must contain at least one non-whitespace
	 * character.
	 *
	 * <pre class="code">
	 * Assert.hasText(name, &quot;'name' must not be empty&quot;);
	 * </pre>
	 *
	 * @param text
	 *            the String to check
	 * @param errorCode
	 *            the error code to use if the assertion fails
	 * @param data
	 *            additional information to the error code.
	 * @see StringUtils#hasText
	 */
	public static void hasText(final String text, final String errorCode,
			final Object... data) {
		if (!StringUtils.hasText(text)) {
			BaseFailureHandler.throwFailureException(errorCode, text, data);
		}
	}

	/**
	 * Assert a boolean expression, throwing
	 * <code>IllegalArgumentException</code> if the test result is
	 * <code>false</code>.
	 *
	 * <pre class="code">
	 * Assert.isTrue(i &gt; 0, &quot;The value must be greater than zero&quot;);
	 * </pre>
	 *
	 * @param expression
	 *            a boolean expression
	 * @param errorCode
	 *            the error code to use if the assertion fails
	 * @throws IllegalArgumentException
	 *             if expression is <code>false</code>
	 */
	public static void isTrue(final boolean expression,
			final String errorCode) {
		if (!expression) {
			BaseFailureHandler.throwFailureException(errorCode);
		}
	}

	/**
	 * Assert a boolean expression, throwing
	 * <code>IllegalArgumentException</code> if the test result is
	 * <code>false</code>.
	 *
	 * <pre class="code">
	 * Assert.isTrue(i &gt; 0, &quot;The value must be greater than zero&quot;);
	 * </pre>
	 *
	 * @param expression
	 *            a boolean expression
	 * @param errorCode
	 *            the error code to use if the assertion fails
	 * @param data
	 *            additional information to the error code.
	 * @throws IllegalArgumentException
	 *             if expression is <code>false</code>
	 */
	public static void isTrue(final boolean expression, final String errorCode,
			final Object... data) {
		if (!expression) {
			BaseFailureHandler.throwFailureException(errorCode, data);
		}
	}

	/**
	 * Assert that an array has no null elements. Note: Does not complain if the
	 * array is empty!
	 *
	 * <pre class="code">
	 * Assert.noNullElements(array, &quot;The array must have non-null elements&quot;);
	 * </pre>
	 *
	 * @param array
	 *            the array to check
	 * @param errorCode
	 *            the error code to use if the assertion fails
	 * @throws IllegalArgumentException
	 *             if the object array contains a <code>null</code> element
	 */
	public static void noNullElements(final Object[] array,
			final String errorCode) {
		if (array != null) {
			for (Object element : array) {
				if (element == null) {
					BaseFailureHandler.throwFailureException(errorCode);
				}
			}
		}
	}

	/**
	 * Assert that a collection has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 *
	 * <pre class="code">
	 * Assert.notEmpty(collection, &quot;Collection must have elements&quot;);
	 * </pre>
	 *
	 * @param collection
	 *            the collection to check
	 * @param errorCode
	 *            the error code to use if the assertion fails
	 * @throws IllegalArgumentException
	 *             if the collection is <code>null</code> or has no elements
	 */
	public static void notEmpty(final Collection<?> collection,
			final String errorCode) {
		if (CollectionUtils.isEmpty(collection)) {
			BaseFailureHandler.throwFailureException(errorCode);
		}
	}

	/**
	 * Assert that a collection has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 *
	 * <pre class="code">
	 * Assert.notEmpty(collection, &quot;Collection must have elements&quot;);
	 * </pre>
	 *
	 * @param collection
	 *            the collection to check
	 * @param errorCode
	 *            the error code to use if the assertion fails
	 * @param data
	 *            additional information to the error code.
	 * @throws IllegalArgumentException
	 *             if the collection is <code>null</code> or has no elements
	 */
	public static void notEmpty(final Collection<?> collection,
			final String errorCode, final Object... data) {
		if (CollectionUtils.isEmpty(collection)) {
			BaseFailureHandler.throwFailureException(errorCode, data);
		}
	}

	/**
	 * Assert that a Map has entries; that is, it must not be <code>null</code>
	 * and must have at least one entry.
	 *
	 * <pre class="code">
	 * Assert.notEmpty(map, &quot;Map must have entries&quot;);
	 * </pre>
	 *
	 * @param map
	 *            the map to check
	 * @param errorCode
	 *            the error code to use if the assertion fails
	 * @throws IllegalArgumentException
	 *             if the map is <code>null</code> or has no entries
	 */
	public static void notEmpty(final Map<?, ?> map, final String errorCode) {
		if (CollectionUtils.isEmpty(map)) {
			BaseFailureHandler.throwFailureException(errorCode);
		}
	}

	/**
	 * Assert that a Map has entries; that is, it must not be <code>null</code>
	 * and must have at least one entry.
	 *
	 * <pre class="code">
	 * Assert.notEmpty(map, &quot;Map must have entries&quot;);
	 * </pre>
	 *
	 * @param map
	 *            the map to check
	 * @param errorCode
	 *            the error code to use if the assertion fails
	 * @param data
	 *            additional information to the error code.
	 * @throws IllegalArgumentException
	 *             if the map is <code>null</code> or has no entries
	 */
	public static void notEmpty(final Map<?, ?> map, final String errorCode,
			final Object... data) {
		if (CollectionUtils.isEmpty(map)) {
			BaseFailureHandler.throwFailureException(errorCode, data);
		}
	}

	/**
	 * Assert that an array has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 *
	 * <pre class="code">
	 * Assert.notEmpty(array, &quot;The array must have elements&quot;);
	 * </pre>
	 *
	 * @param array
	 *            the array to check
	 * @param errorCode
	 *            the error code to use if the assertion fails
	 * @throws IllegalArgumentException
	 *             if the object array is <code>null</code> or has no elements
	 */
	public static void notEmpty(final Object[] array, final String errorCode) {
		if (ObjectUtils.isEmpty(array)) {
			BaseFailureHandler.throwFailureException(errorCode);
		}
	}

	/**
	 * Assert that an object is not <code>null</code> .
	 *
	 * <pre class="code">
	 * Assert.notNull(clazz, &quot;The class must not be null&quot;);
	 * </pre>
	 *
	 * @param object
	 *            the object to check
	 * @param errorCode
	 *            the error code to use if the assertion fails
	 * @throws IllegalArgumentException
	 *             if the object is <code>null</code>
	 */
	public static void notNull(final Object object, final String errorCode) {
		if (object == null) {
			BaseFailureHandler.throwFailureException(errorCode);
		}
	}

	/**
	 * Assert that an object is not <code>null</code> .
	 *
	 * <pre class="code">
	 * Assert.notNull(clazz, &quot;The class must not be null&quot;);
	 * </pre>
	 *
	 * @param object
	 *            the object to check
	 * @param errorCode
	 *            the error code to use if the assertion fails
	 * @param data
	 *            additional information to the error code.
	 * @throws IllegalArgumentException
	 *             if the object is <code>null</code>
	 */
	public static void notNull(final Object object, final String errorCode,
			final Object... data) {
		if (object == null) {
			BaseFailureHandler.throwFailureException(errorCode, data);
		}
	}

	/**
	 * Assert a boolean expression, throwing <code>IllegalStateException</code>
	 * if the test result is <code>false</code>. Call isTrue if you wish to
	 * throw IllegalArgumentException on an assertion failure.
	 *
	 * <pre class="code">
	 * Assert.state(id == null,
	 * 		&quot;The id property must not already be initialized&quot;);
	 * </pre>
	 *
	 * @param expression
	 *            a boolean expression
	 * @param errorCode
	 *            the error code to use if the assertion fails
	 * @throws IllegalStateException
	 *             if expression is <code>false</code>
	 */
	public static void state(final boolean expression, final String errorCode) {
		if (!expression) {
			BaseFailureHandler.throwFailureException(errorCode);
		}
	}

	/**
	 * Assert a boolean expression, throwing <code>IllegalStateException</code>
	 * if the test result is <code>false</code>. Call isTrue if you wish to
	 * throw IllegalArgumentException on an assertion failure.
	 *
	 * <pre class="code">
	 * Assert.state(id == null,
	 * 		&quot;The id property must not already be initialized&quot;);
	 * </pre>
	 *
	 * @param expression
	 *            a boolean expression
	 * @param errorCode
	 *            the error code to use if the assertion fails
	 * @param data
	 *            additional information to the error code.
	 * @throws IllegalStateException
	 *             if expression is <code>false</code>
	 */
	public static void state(final boolean expression, final String errorCode,
			final Object... data) {
		if (!expression) {
			BaseFailureHandler.throwFailureException(errorCode, data);
		}
	}
}
