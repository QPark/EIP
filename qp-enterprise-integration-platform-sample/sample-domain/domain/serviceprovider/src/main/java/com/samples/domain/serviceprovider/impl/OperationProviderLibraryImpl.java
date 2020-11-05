/*******************************************************************************
 * Copyright (c) 2013 - 2020 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.domain.serviceprovider.impl;

import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.samples.domain.serviceprovider.AppSecurityContextHandler;
import com.samples.domain.serviceprovider.OperationProviderLibrary;
import com.samples.platform.service.library.msg.CreateBookRequestType;
import com.samples.platform.service.library.msg.CreateBookResponseType;
import com.samples.platform.service.library.msg.GetBookRequestType;
import com.samples.platform.service.library.msg.GetBookResponseType;
import com.samples.platform.service.library.msg.ObjectFactory;
import com.samples.platform.service.library.msg.gateway.CreateBook;
import com.samples.platform.service.library.msg.gateway.GetBook;

/**
 * Operation provider of service <code>library</code>.
 *
 * @author bhausen
 */
@Component
public class OperationProviderLibraryImpl implements OperationProviderLibrary {
	/** The {@link Logger}. */
	private final Logger logger = LoggerFactory
			.getLogger(OperationProviderLibraryImpl.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();
	/** Authenticated? */
	@Autowired
	private AppSecurityContextHandler setSecurityContextAuth;
	/** Gateway to create book. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusLibraryCreateBookGateway")
	private CreateBook createBook;
	/** Gateway to get book. */
	@Autowired
	@Qualifier("eipCallerComSamplesBusLibraryGetBookGateway")
	private GetBook getBook;

	/**
	 * @param start
	 * @return the duration in 000:00:00.000 format.
	 */
	private String requestDuration(final long start) {
		long millis = System.currentTimeMillis() - start;
		String hmss = String.format("%03d:%02d:%02d.%03d",
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS
						.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES
						.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
				TimeUnit.MILLISECONDS.toMillis(millis) - TimeUnit.SECONDS
						.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis)));
		return hmss;
	}

	/** Initalize the request. */
	private void requestInit() {
		if (this.setSecurityContextAuth != null) {
			this.setSecurityContextAuth.setAppSecurityContextAuthentication();
		}
	}

	/** Finalize the request. */
	private void requestFinalization() {
	}
	/// ** Get the size of the response value. */
	// private String responseValueSize(Object value) {
	// String s = "null";
	// if (value != null) {
	// if (Collection.class.isInstance(value)) {
	// s = String.valueOf(((Collection<?>) value).size());
	// } else if (value.getClass().isArray()) {
	// s = String.valueOf(((Object[]) value).length);
	// } else {
	// s = "1";
	// }
	// }
	// return s;
	// }

	/**
	 * @param request
	 *                    the {@link CreateBookRequestType}.
	 * @return the {@link CreateBookResponseType}.
	 */
	@Override
	public CreateBookResponseType createBook(
			final CreateBookRequestType request) {
		this.logger.debug("+createBook");
		CreateBookResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<CreateBookResponseType> response = this.createBook
					.invoke(this.of.createCreateBookRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" createBook duration {}",
					this.requestDuration(start));
			this.logger.debug("-createBook");
		}
		return value;
	}

	/**
	 * @param request
	 *                    the {@link GetBookRequestType}.
	 * @return the {@link GetBookResponseType}.
	 */
	@Override
	public GetBookResponseType getBook(final GetBookRequestType request) {
		this.logger.debug("+getBook");
		GetBookResponseType value = null;
		long start = System.currentTimeMillis();
		try {
			this.requestInit();
			JAXBElement<GetBookResponseType> response = this.getBook
					.invoke(this.of.createGetBookRequest(request));
			if (response != null) {
				value = response.getValue();
			}
		} finally {
			this.requestFinalization();
			this.logger.debug(" getBook duration {}",
					this.requestDuration(start));
			this.logger.debug("-getBook");
		}
		return value;
	}
}