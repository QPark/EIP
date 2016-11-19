/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.sftp;

import java.util.Properties;

import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;

/**
 * Provides the creation of the {@link DefaultSftpSessionFactory}.
 *
 * @author bhausen
 */
public class SftpSessionFactoryProvider {
	/**
	 * Get the {@link DefaultSftpSessionFactory} according to the
	 * {@link ConnectionDetails}.
	 *
	 * @param connectionDetails
	 *            the {@link ConnectionDetails}.
	 * @return the {@link DefaultSftpSessionFactory}.
	 */
	public static DefaultSftpSessionFactory getSessionFactory(
			final ConnectionDetails connectionDetails) {
		Properties sessionConfig = new Properties();
		sessionConfig.setProperty("compression.s2c", "zlib@openssh.com,none");
		sessionConfig.setProperty("compression.c2s", "zlib@openssh.com,none");
		sessionConfig.setProperty("compression_level", "9");
		DefaultSftpSessionFactory sessionFactory = new DefaultSftpSessionFactory();
		sessionFactory.setHost(connectionDetails.getHostName());
		sessionFactory.setPort(connectionDetails.getPort());
		sessionFactory.setUser(connectionDetails.getUserName());
		sessionFactory.setPassword(new String(connectionDetails.getPassword()));
		sessionFactory.setSessionConfig(sessionConfig);
		return sessionFactory;
	}

}
