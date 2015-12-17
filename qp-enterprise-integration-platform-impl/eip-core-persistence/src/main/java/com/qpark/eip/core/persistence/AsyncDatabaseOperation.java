/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.persistence;

import java.util.concurrent.Callable;

/**
 * Call the database operation of the channel invocation asynchronous.
 * 
 * @author bhausen
 */
public class AsyncDatabaseOperation implements Callable<Void> {
	private final AbstractEipDao dao;
	private final Object obj;
	private final String userName;

	/**
	 * Ctor.
	 * 
	 * @param dao
	 *            the {@link AbstractEipDao}.
	 * @param userName
	 *            the user name.
	 * @param obj
	 *            the object to persist.
	 */
	public AsyncDatabaseOperation(final AbstractEipDao dao,
			final String userName, final Object obj) {
		this.dao = dao;
		this.userName = userName;
		this.obj = obj;
	}

	/**
	 * Call the database operation of the channel invocation asynchronous.
	 * 
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Void call() throws Exception {
		this.dao.doAsyncDatabaseOperation(this.userName, this.obj);
		return null;
	}
}
