/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html. Contributors: Bernhard Hausen -
 * Initial API and implementation
 ******************************************************************************/
package com.qpark.eip.core;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author bhausen
 */
public class DummyJdbcDriver implements Driver {

    /**
     * @see java.sql.Driver#connect(java.lang.String, java.util.Properties)
     */
    @Override
    public Connection connect(final String url, final Properties info) throws SQLException {
	return null;
    }

    /**
     * @see java.sql.Driver#acceptsURL(java.lang.String)
     */
    @Override
    public boolean acceptsURL(final String url) throws SQLException {
	return false;
    }

    /**
     * Since java 8.
     */
    public java.util.logging.Logger getParentLogger() {
	return null;
    }

    /**
     * @see java.sql.Driver#getPropertyInfo(java.lang.String,
     *      java.util.Properties)
     */
    @Override
    public DriverPropertyInfo[] getPropertyInfo(final String url, final Properties info) throws SQLException {
	return new DriverPropertyInfo[0];
    }

    /**
     * @see java.sql.Driver#getMajorVersion()
     */
    @Override
    public int getMajorVersion() {
	return 1;
    }

    /**
     * @see java.sql.Driver#getMinorVersion()
     */
    @Override
    public int getMinorVersion() {
	return 0;
    }

    /**
     * @see java.sql.Driver#jdbcCompliant()
     */
    @Override
    public boolean jdbcCompliant() {
	return true;
    }
}
