package com.samples.platform.service.iss.tech.support;

import java.io.StringReader;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.samples.platform.service.iss.tech.support.msg.GetForwardedReferenceDataRequestType;
import com.samples.platform.service.iss.tech.support.msg.GetForwardedReferenceDataResponseType;
import com.samples.platform.service.iss.tech.support.msg.ObjectFactory;
import com.springsource.insight.annotation.InsightEndPoint;

/**
 * Operation get forwarded reference data on service
 * <code>iss.tech.support</code>.
 * 
 * @author bhausen
 */
@Component("operationProviderIssTechSupportGetForwardedReferenceData")
public class GetForwardedReferenceDataOperation {
    /** The {@link Logger}. */
    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GetForwardedReferenceDataOperation.class);

    /** The {@link ObjectFactory}. */
    private final ObjectFactory of = new ObjectFactory();

    /**
     * @param message
     *            the {@link JAXBElement} containing a
     *            {@link GetForwardedReferenceDataRequestType}.
     * @return the {@link JAXBElement} with a
     *         {@link GetForwardedReferenceDataResponseType}.
     */
    @InsightEndPoint
    @ServiceActivator
    public final JAXBElement<GetForwardedReferenceDataResponseType> getForwardedReferenceData(
	    final JAXBElement<GetForwardedReferenceDataRequestType> message) {
	this.logger.debug("+getForwardedReferenceData");
	GetForwardedReferenceDataRequestType request = message.getValue();
	GetForwardedReferenceDataResponseType response = this.of.createGetForwardedReferenceDataResponseType();
	long start = System.currentTimeMillis();
	try {
	    GetForwardedReferenceDataResponseType responseSample = this.getSampleResponseObject();
	    if (responseSample != null) {
		response = responseSample;
	    }
	    // response.getFailure().clear();
	    // The operation {0} of service {1} is not implement!!
	    // response.getFailure().add(
	    // FailureHandler.getFailureType("E_NOT_IMPLEMENTED_OPERATION",
	    // "get forwarded reference data", "iss.tech.support"));
	} catch (Throwable e) {
	    /* Add a not covered error to the response. */
	    this.logger.error(e.getMessage(), e);
	    // response.getFailure().add(
	    // FailureHandler.handleException(e, "E_ALL_NOT_KNOWN_ERROR",
	    // this.logger);
	} finally {
	    this.logger.debug(" getForwardedReferenceData duration {}", this.requestDuration(start));
	    this.logger.debug("-getForwardedReferenceData #{}, #f{}",
		    response/* .get() */ != null ? 1 : 0, response.getFailure().size());
	}
	return this.of.createGetForwardedReferenceDataResponse(response);
    }

    /**
     * @return a mock {@link GetForwardedReferenceDataResponseType}.
     */
    private GetForwardedReferenceDataResponseType getSampleResponseObject() {
	GetForwardedReferenceDataResponseType mock = null;
	String xml = this.getSampleXml();
	try {
	    JAXBContext jaxbContext = JAXBContext.newInstance("com.samples.platform.service.iss.tech.support.msg");
	    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
	    JAXBElement<GetForwardedReferenceDataResponseType> jaxb = (JAXBElement<GetForwardedReferenceDataResponseType>) unmarshaller
		    .unmarshal(new StringReader(xml));
	    if (jaxb != null) {
		mock = jaxb.getValue();
	    }
	} catch (Exception e) {
	    this.logger.debug("GetForwardedReferenceData generate sample message error: {}", e.getMessage());
	    mock = null;
	}
	return mock;
    }

    /**
     * @return a sample xml.
     */
    private String getSampleXml() {
	StringBuffer sb = new StringBuffer();
	sb.append(
		"<GetForwardedReferenceDataResponse xmlns=\"http://www.samples.com/TechnicalSupport/TechnicalSupportServiceMessages\"> ");
	sb.append("	<failure> ");
	sb.append("		<code>E_ALL_NOT_KNOWN_ERROR</code> ");
	sb.append("		<userMessage>string</userMessage> ");
	sb.append("		<errorDetails>string</errorDetails> ");
	sb.append("		<severity>WARNING</severity> ");
	sb.append("	</failure> ");
	sb.append("	<referenceData> ");
	sb.append("		<UUID>00000000-0000-0000-0000-000000000000</UUID> ");
	sb.append("		<displayValue>string</displayValue> ");
	sb.append("		<category>string</category> ");
	sb.append("		<description>string</description> ");
	sb.append("		<active>true</active> ");
	sb.append("		<value>string</value> ");
	sb.append("	</referenceData> ");
	sb.append("</GetForwardedReferenceDataResponse>");
	return sb.toString();
    }

    /**
     * @param start
     * @return the duration in 000:00:00.000 format.
     */
    private String requestDuration(final long start) {
	long millis = System.currentTimeMillis() - start;
	String hmss = String.format("%03d:%02d:%02d.%03d", TimeUnit.MILLISECONDS.toHours(millis),
		TimeUnit.MILLISECONDS.toMinutes(millis)
			- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
		TimeUnit.MILLISECONDS.toSeconds(millis)
			- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
		TimeUnit.MILLISECONDS.toMillis(millis)
			- TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis)));
	return hmss;
    }
}
