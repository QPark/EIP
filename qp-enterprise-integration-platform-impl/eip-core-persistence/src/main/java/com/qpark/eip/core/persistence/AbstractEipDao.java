package com.qpark.eip.core.persistence;

import javax.persistence.EntityManager;

/**
 * DAO to access the authority database and to log the user calls.
 * @author bhausen
 */
public abstract class AbstractEipDao {

	public abstract void doAsyncDatabaseOperation(final String userName,
			Object obj);

	protected abstract String getContextName();

	protected abstract String getContextVersion();

	protected abstract EntityManager getEntityManager();
}
