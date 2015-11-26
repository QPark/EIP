package com.samples.platform.service.iss.tech.support;

import java.io.StringReader;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.samples.platform.service.iss.tech.support.msg.GetFlowReportRequestType;
import com.samples.platform.service.iss.tech.support.msg.GetFlowReportResponseType;
import com.samples.platform.service.iss.tech.support.msg.ObjectFactory;
import com.springsource.insight.annotation.InsightEndPoint;

/**
 * Operation get flow report on service <code>iss.tech.support</code>.
 * 
 * @author bhausen
 */
@Component("operationProviderIssTechSupportGetFlowReport")
public class GetFlowReportOperation {
    /** The {@link Logger}. */
    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GetFlowReportOperation.class);

    /** The {@link ObjectFactory}. */
    private final ObjectFactory of = new ObjectFactory();

    /**
     * @param message
     *            the {@link JAXBElement} containing a
     *            {@link GetFlowReportRequestType}.
     * @return the {@link JAXBElement} with a {@link GetFlowReportResponseType}.
     */
    @InsightEndPoint
    @ServiceActivator
    public final JAXBElement<GetFlowReportResponseType> getFlowReport(
	    final JAXBElement<GetFlowReportRequestType> message) {
	this.logger.debug("+getFlowReport");
	GetFlowReportRequestType request = message.getValue();
	GetFlowReportResponseType response = this.of.createGetFlowReportResponseType();
	long start = System.currentTimeMillis();
	try {
	    GetFlowReportResponseType responseSample = this.getSampleResponseObject();
	    if (responseSample != null) {
		response = responseSample;
	    }
	    // response.getFailure().clear();
	    // The operation {0} of service {1} is not implement!!
	    // response.getFailure().add(
	    // FailureHandler.getFailureType("E_NOT_IMPLEMENTED_OPERATION",
	    // "get flow report", "iss.tech.support"));
	} catch (Throwable e) {
	    /* Add a not covered error to the response. */
	    this.logger.error(e.getMessage(), e);
	    // response.getFailure().add(
	    // FailureHandler.handleException(e, "E_ALL_NOT_KNOWN_ERROR",
	    // this.logger);
	} finally {
	    this.logger.debug(" getFlowReport duration {}", this.requestDuration(start));
	    this.logger.debug("-getFlowReport #{}, #f{}", response/* .get() */ != null ? 1 : 0,
		    response.getFailure().size());
	}
	return this.of.createGetFlowReportResponse(response);
    }

    /**
     * @return a mock {@link GetFlowReportResponseType}.
     */
    private GetFlowReportResponseType getSampleResponseObject() {
	GetFlowReportResponseType mock = null;
	String xml = this.getSampleXml();
	try {
	    JAXBContext jaxbContext = JAXBContext.newInstance("com.samples.platform.service.iss.tech.support.msg");
	    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
	    JAXBElement<GetFlowReportResponseType> jaxb = (JAXBElement<GetFlowReportResponseType>) unmarshaller
		    .unmarshal(new StringReader(xml));
	    if (jaxb != null) {
		mock = jaxb.getValue();
	    }
	} catch (Exception e) {
	    this.logger.debug("GetFlowReport generate sample message error: {}", e.getMessage());
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
		"<GetFlowReportResponse xmlns=\"http://www.samples.com/TechnicalSupport/TechnicalSupportServiceMessages\"> ");
	sb.append("	<failure> ");
	sb.append("		<code>E_ALL_NOT_KNOWN_ERROR</code> ");
	sb.append("		<userMessage>string</userMessage> ");
	sb.append("		<errorDetails>string</errorDetails> ");
	sb.append("		<severity>WARNING</severity> ");
	sb.append("	</failure> ");
	sb.append("	<report> ");
	sb.append("		<flowSessionId>00000000-0000-0000-0000-000000000000</flowSessionId> ");
	sb.append("		<logTime>2013-11-23T15:44:07</logTime> ");
	sb.append("		<severity>string</severity> ");
	sb.append("		<flowName>string</flowName> ");
	sb.append("		<flowStep>string</flowStep> ");
	sb.append("		<messageClassification>string</messageClassification> ");
	sb.append("		<additionalInformation>string</additionalInformation> ");
	sb.append("		<messageType>string</messageType> ");
	sb.append("		<data>string</data> ");
	sb.append("	</report> ");
	sb.append("</GetFlowReportResponse>");
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
