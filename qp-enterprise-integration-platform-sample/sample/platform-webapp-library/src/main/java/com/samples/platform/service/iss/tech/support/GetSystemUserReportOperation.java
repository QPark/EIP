package com.samples.platform.service.iss.tech.support;

import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.samples.platform.inf.iss.tech.support.flow.SystemUserReportFlow;
import com.samples.platform.inf.iss.tech.support.flow.SystemUserReportFlowRequestType;
import com.samples.platform.inf.iss.tech.support.flow.SystemUserReportFlowResponseType;
import com.samples.platform.service.iss.tech.support.msg.GetSystemUserReportRequestType;
import com.samples.platform.service.iss.tech.support.msg.GetSystemUserReportResponseType;
import com.samples.platform.service.iss.tech.support.msg.ObjectFactory;
import com.springsource.insight.annotation.InsightEndPoint;

/**
 * Operation get system user report on service <code>iss.tech.support</code>.
 *
 * @author bhausen
 */
@Component("operationProviderIssTechSupportGetSystemUserReport")
public class GetSystemUserReportOperation {
	/** The {@link Logger}. */
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(GetSystemUserReportOperation.class);
	/** The {@link ObjectFactory}. */
	private final ObjectFactory of = new ObjectFactory();
	/** The {@link SystemUserReportFlow}. */
	@Autowired
	private SystemUserReportFlow flow;

	/**
	 * @param message
	 *            the {@link JAXBElement} containing a
	 *            {@link GetSystemUserReportRequestType}.
	 * @return the {@link JAXBElement} with a
	 *         {@link GetSystemUserReportResponseType}.
	 */
	@InsightEndPoint
	@ServiceActivator
	public final JAXBElement<GetSystemUserReportResponseType> getSystemUserReport(
			final JAXBElement<GetSystemUserReportRequestType> message) {
		this.logger.debug("+getSystemUserReport");
		// GetSystemUserReportRequestType request = message.getValue();
		GetSystemUserReportResponseType response = this.of
				.createGetSystemUserReportResponseType();
		long start = System.currentTimeMillis();
		try {
			SystemUserReportFlowRequestType in = new SystemUserReportFlowRequestType();
			in.setIn(message.getValue());
			SystemUserReportFlowResponseType out = this.flow.invokeFlow(in);
			if (out != null) {
				response = out.getOut();
			}
		} catch (Throwable e) {
			/* Add a not covered error to the response. */
			this.logger.error(e.getMessage(), e);
			// response.getFailure().add(
			// FailureHandler.handleException(e, "E_ALL_NOT_KNOWN_ERROR",
			// this.logger);
		} finally {
			this.logger.debug(" getSystemUserReport duration {}",
					this.requestDuration(start));
			this.logger.debug("-getSystemUserReport #{}, #f{}",
					response/* .get() */ != null ? 1 : 0,
					response.getFailure().size());
		}
		return this.of.createGetSystemUserReportResponse(response);
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
