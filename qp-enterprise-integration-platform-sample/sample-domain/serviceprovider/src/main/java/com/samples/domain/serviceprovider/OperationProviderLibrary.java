/*******************************************************************************
 * Copyright (c) 2013 - 2020 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.domain.serviceprovider;

import com.samples.platform.service.library.msg.CreateBookRequestType;
import com.samples.platform.service.library.msg.CreateBookResponseType;
import com.samples.platform.service.library.msg.GetBookRequestType;
import com.samples.platform.service.library.msg.GetBookResponseType;

public interface OperationProviderLibrary {

	/**
	 * @param request the {@link CreateBookRequestType}.
	 * @return the {@link CreateBookResponseType}.
	 */
	CreateBookResponseType createBook(CreateBookRequestType request);

	/**
	 * @param request the {@link GetBookRequestType}.
	 * @return the {@link GetBookResponseType}.
	 */
	GetBookResponseType getBook(GetBookRequestType request);

}