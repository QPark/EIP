/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting S.�r.l. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License
 * v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html. Contributors: Bernhard Hausen -
 * Initial API and implementation
 ******************************************************************************/
package com.qpark.eip.core.persistence;

import javax.persistence.EntityManager;

/**
 * DAO to access the authority database and to log the user calls.
 * 
 * @author bhausen
 */
public abstract class AbstractEipDao {

	public abstract void doAsyncDatabaseOperation(final String userName,
			Object obj);

	protected abstract String getContextName();

	protected abstract String getContextVersion();

	protected abstract EntityManager getEntityManager();
}
