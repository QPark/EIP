/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.sftp;

import java.util.Objects;

/**
 * Container of the connection details.
 *
 * @author bhausen
 */
public class ConnectionDetails {
	/** The sFTP server host name or ip address. */
	private String hostName;

	/** The sFTP user password. */
	private char[] password;

	/** The sFTP server port. */
	private int port = 22;
	/** The sFTP user name. */
	private String userName;

	/** Ctor. */
	public ConnectionDetails() {
	}

	/**
	 * Create with connection URL.
	 *
	 * @param connectionUrl
	 *            the connection URL.
	 */
	public ConnectionDetails(final String connectionUrl) {
		if (Objects.nonNull(connectionUrl)
				&& connectionUrl.toLowerCase().startsWith("sftp://")) {
			String[] strs = connectionUrl.substring(7).split("/");
			if (strs.length > 0) {
				if (strs[0].indexOf(':') > 0) {
					this.hostName = strs[0].substring(0, strs[0].indexOf(':'))
							.trim();
					try {
						this.port = Integer.parseInt(strs[0].substring(
								strs[0].indexOf(':') + 1, strs[0].length()));
					} catch (NumberFormatException e) {
						// Do nothing.
					}
				} else {
					this.hostName = strs[0].trim();
				}
			}
			if (this.port == 0) {
				this.port = 22;
			}
		} else {
			throw new IllegalArgumentException(String.format(
					"Connection URL does not start with 'sftp://' : %s",
					connectionUrl));
		}
	}

	/**
	 * Get the sFTP server host name or ip address.
	 *
	 * @return the sFTP server host name or ip address.
	 */
	public String getHostName() {
		return this.hostName;
	}

	/**
	 * Get the sFTP user password.
	 *
	 * @return the sFTP user password.
	 */
	public char[] getPassword() {
		return this.password;
	}

	/**
	 * Get the sFTP server port.
	 *
	 * @return the sFTP server port.
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * Get the sFTP user name.
	 *
	 * @return the sFTP user name.
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * Set the sFTP server host name or ip address.
	 *
	 * @param hostName
	 *            the sFTP server host name or ip address.
	 */
	public void setHostName(final String hostName) {
		this.hostName = hostName;
	}

	/**
	 * Set the sFTP user password.
	 *
	 * @param password
	 *            the sFTP user password.
	 */
	public void setPassword(final char[] password) {
		this.password = password;
	}

	/**
	 * Set the sFTP server port.
	 *
	 * @param port
	 *            the sFTP server port.
	 */
	public void setPort(final int port) {
		this.port = port;
	}

	/**
	 * Get the sFTP user name.
	 *
	 * @param userName
	 *            the sFTP user name.
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}
}
