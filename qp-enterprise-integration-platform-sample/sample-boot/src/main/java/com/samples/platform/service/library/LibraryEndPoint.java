package com.samples.platform.service.library;

import java.time.Duration;
import java.time.Instant;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapHeader;

import com.samples.platform.model.library.BookType;
import com.samples.platform.service.library.msg.CreateBookRequestType;
import com.samples.platform.service.library.msg.CreateBookResponseType;
import com.samples.platform.service.library.msg.GetBookRequestType;
import com.samples.platform.service.library.msg.GetBookResponseType;
import com.samples.platform.service.library.msg.ObjectFactory;

/**
 * WebService {@link EndPoint} of service <code>library</code>.
 *
 * @author bhausen
 */
@Endpoint
public class LibraryEndPoint {
	/** The target name space. */
	private static final String NAMESPACE_URI = "http://www.sample.com/Library/LibraryServiceMessages";
	/** The {@link Logger}. */
	private final Logger logger = LoggerFactory
			.getLogger(LibraryEndPoint.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();

	/**
	 * @param request    the {@link CreateBookRequestType}.
	 * @param soapHeader the {@link SoapHeader}.
	 * @return the {@link JAXBElement} with value
	 *         {@link CreateBookResponseType}.
	 */
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreateBookRequest")
	@ResponsePayload
	public JAXBElement<CreateBookResponseType> createBook(
			@RequestPayload final CreateBookRequestType request,
			final SoapHeader header) {
		this.logger.debug("+createBook");
		Instant start = Instant.now();
		CreateBookResponseType value = this.of.createCreateBookResponseType();
		try {
			BookType b = new BookType();
			b.setTitle("My cretated book");
			value.getBook().add(b);
		} finally {
			this.logger.debug(" createBook duration {}",
					Duration.between(start, Instant.now()));
			this.logger.debug("-createBook");
		}
		return this.of.createCreateBookResponse(value);
	}

	/**
	 * @param request    the {@link GetBookRequestType}.
	 * @param soapHeader the {@link SoapHeader}.
	 * @return the {@link JAXBElement} with value {@link GetBookResponseType}.
	 */
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetBookRequest")
	@ResponsePayload
	public JAXBElement<GetBookResponseType> getBook(
			@RequestPayload final GetBookRequestType request,
			final SoapHeader header) {
		this.logger.debug("+getBook");
		Instant start = Instant.now();
		GetBookResponseType value = this.of.createGetBookResponseType();
		try {
			BookType b = new BookType();
			b.setTitle("My book");
			value.getBook().add(b);
		} finally {
			this.logger.debug(" getBook duration {}",
					Duration.between(start, Instant.now()));
			this.logger.debug("-getBook");
		}
		return this.of.createGetBookResponse(value);
	}
}