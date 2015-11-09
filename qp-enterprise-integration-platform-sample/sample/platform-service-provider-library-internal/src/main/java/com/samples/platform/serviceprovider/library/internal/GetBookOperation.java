package com.samples.platform.serviceprovider.library.internal;

import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.samples.platform.service.library.msg.GetBookRequestType;
import com.samples.platform.service.library.msg.GetBookResponseType;
import com.samples.platform.service.library.msg.ObjectFactory;
import com.samples.platform.serviceprovider.library.internal.dao.PlatformDao;
import com.springsource.insight.annotation.InsightEndPoint;

/**
 * Operation get book on service <code>library</code>.
 *
 * @author bhausen
 */
@Component("operationProviderLibraryGetBook")
public class GetBookOperation {
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetBookOperation.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();
	/** The {@link PlatformDao}. */
	@Autowired
	private PlatformDao dao;

	/**
	 * @param message
	 *            the {@link JAXBElement} containing a
	 *            {@link GetBookRequestType}.
	 * @return the {@link JAXBElement} with a {@link GetBookResponseType}.
	 */
	@InsightEndPoint
	@ServiceActivator
	public final JAXBElement<GetBookResponseType> getBook(
			final JAXBElement<GetBookRequestType> message) {
		this.logger.debug("+getBook");
		GetBookRequestType request = message.getValue();
		GetBookResponseType response = this.of.createGetBookResponseType();
		long start = System.currentTimeMillis();
		try {
			if (request.getCriteria().isSetISBN()) {
				response.getBook().add(this.dao
						.getBookByISBN(request.getCriteria().getISBN()));
			} else if (request.getCriteria().getId().size() > 0) {
				for (String uuid : request.getCriteria().getId()) {
					response.getBook().add(this.dao.getBookById(uuid));
				}
			}
		} catch (Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
			// response.getFailure().add(
			// FailureHandler.handleException(e, "E_ALL_NOT_KNOWN_ERROR",
			// this.logger);
		} finally {
			this.logger.debug(" getBook duration {}",
					this.requestDuration(start));
			this.logger.debug("-getBook #{}, #f{}",
					response/* .get() */ != null ? 1 : 0,
					response.getFailure().size());
		}
		return this.of.createGetBookResponse(response);
	}

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
}