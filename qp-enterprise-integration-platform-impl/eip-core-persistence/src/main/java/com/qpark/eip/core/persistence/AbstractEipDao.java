/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.persistence;

import javax.persistence.EntityManager;

/**
 * DAO to access the authority database and to log the user calls.
 *
 * @author bhausen
 */
public abstract class AbstractEipDao {
	/**
	 * @param userName
	 *            the user name.
	 * @param obj
	 */
	public abstract void doAsyncDatabaseOperation(final String userName,
			Object obj);

	/**
	 * @return the context name.
	 */
	protected abstract String getContextName();

	/**
	 * @return the context version.
	 */
	protected abstract String getContextVersion();

	/**
	 * @return the {@link EntityManager}.
	 */
	protected abstract EntityManager getEntityManager();
}
