/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html. Contributors: Bernhard Hausen -
 * Initial API and implementation
 ******************************************************************************/
package com.samples.platform.serviceprovider.library.internal;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.qpark.eip.core.DateUtil;
import com.qpark.eip.service.common.msg.FailureType;
import com.samples.platform.core.failure.FailureHandler;
import com.samples.platform.model.library.BookType;
import com.samples.platform.service.library.msg.CreateBookRequestType;
import com.samples.platform.service.library.msg.CreateBookResponseType;
import com.samples.platform.service.library.msg.ObjectFactory;
import com.samples.platform.serviceprovider.library.internal.dao.PlatformDao;
import com.springsource.insight.annotation.InsightEndPoint;

/**
 * Operation create book on service <code>library</code>.
 *
 * @author bhausen
 */
@Component("operationProviderLibraryCreateBook")
public class CreateBookOperation {
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(CreateBookOperation.class);
	/** The {@link ObjectFactory}. */
	private ObjectFactory of = new ObjectFactory();
	/** The {@link PlatformDao}. */
	@Autowired
	private PlatformDao dao;

	/**
	 * @param message
	 *            the {@link JAXBElement} containing a
	 *            {@link CreateBookRequestType}.
	 * @return the {@link JAXBElement} with a {@link CreateBookResponseType}.
	 */
	@InsightEndPoint
	@ServiceActivator
	public final JAXBElement<CreateBookResponseType> createBook(
			final JAXBElement<CreateBookRequestType> message) {
		this.logger.debug("+createBook");
		CreateBookRequestType request = message.getValue();
		CreateBookResponseType response = this.of
				.createCreateBookResponseType();
		long start = System.currentTimeMillis();
		try {
			for (BookType book : request.getBook()) {
				response.getBook().add(this.dao.createBook(book));
			}
		} catch (Exception e) {
			FailureType f = FailureHandler
					.getFailureType("E_ALL_NOT_KNOWN_ERROR", e);
			response.getFailure().add(f);
		} finally {
			this.logger.debug(" createBook duration {}",
					DateUtil.getDuration(start, System.currentTimeMillis()));
			this.logger.debug("-createBook #{}, #f{}",
					response.getBook().size(), response.getFailure().size());
		}
		return this.of.createCreateBookResponse(response);
	}
}
