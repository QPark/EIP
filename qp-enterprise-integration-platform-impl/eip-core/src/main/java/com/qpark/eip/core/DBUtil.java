/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Bernhard Hausen - Initial API and implementation
 *
 ******************************************************************************/
package com.qpark.eip.core;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import javax.persistence.TypedQuery;

/**
 * @author bhausen
 */
public abstract class DBUtil {
	/**
	 * Set the maximum result set of the {@link javax.persistence.TypedQuery}.
	 *
	 * @param typedQuery
	 *            The {@link TypedQuery}
	 * @param maxElements
	 *            The maximum number of elements if given.
	 */
	public static void setMaxElements(final TypedQuery<?> typedQuery, final BigInteger maxElements,
			final Map<String, String> properties) {
		if (typedQuery != null) {
			int max = EipSettings.EIP_UTIL_MAX_DATABASE_LINES_NUMBER;
			if (maxElements != null && maxElements.intValue() > 0) {
				max = maxElements.intValue();
			} else if (properties.get(EipSettings.EIP_UTIL_MAX_DATABASE_LINES) != null) {
				try {
					max = Integer.parseInt(properties.get(EipSettings.EIP_UTIL_MAX_DATABASE_LINES));
				} catch (NumberFormatException e) {
					// nothing to do.
				}
			}
			typedQuery.setMaxResults(max);
		}
	}

	/**
	 * @param url
	 * @param username
	 * @param password
	 * @return the connection or null.
	 */
	@SuppressWarnings("unused")
	private static Connection getOracleConnection(final String url, final String username, final String password) {
		Connection connection = null;
		try {
			String driverName = "oracle.jdbc.driver.OracleDriver";
			Class.forName(driverName);
			connection = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * If the criteria does not start or end with <code>*</code> it will
	 * appended. Then the method replaces all occurrences of <code>?</code> by
	 * <code>_</code> and <code>*</code> by <code>%</code>.
	 *
	 * @param criteria
	 *            the criteria.
	 * @return the changed criteria.
	 */
	public static String prepareContainsSearch(final String criteria) {
		String s = criteria;
		if (s != null) {
			s = s.trim();
			if (s.length() == 0) {
				s = null;
			} else {
				if (!s.startsWith("*")) {
					s = new StringBuffer(s.length() + 1).append('*').append(s).toString();
				}
				if (!s.endsWith("*")) {
					s = new StringBuffer(s).append('*').toString();
				}
				s = s.replaceAll("\\?", "_");
				s = s.replaceAll("\\*", "%");
			}
		}
		return s;
	}
}
