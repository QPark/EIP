/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Bernhard Hausen - Initial API and implementation
 *
 ******************************************************************************/
package com.qpark.eip.core.spring;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.integration.IntegrationMessageHeaderAccessor;
import org.springframework.integration.dispatcher.AggregateMessageDeliveryException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.xml.transform.TransformerHelper;

/**
 * @author bhausen
 */
public class PayloadLogger extends ChannelInterceptorAdapter implements EndpointInterceptor, ClientInterceptor {
    private static final SpelExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();
    private String contextPath = null;
    private final EvaluationContext evaluationContext;
    private volatile Expression expression;
    private JAXBContext jaxbContext;
    /** The {@link Logger}. */
    private Logger logger = null;
    private String loggerName = null;

    private final TransformerHelper transformerHelper = new TransformerHelper();

    public PayloadLogger() {
	StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
	evaluationContext.addPropertyAccessor(new MapAccessor());
	this.evaluationContext = evaluationContext;
	this.expression = EXPRESSION_PARSER.parseExpression("payload");
    }

    /**
     * @param messageContext
     * @param endpoint
     * @param ex
     */
    @Override
    public void afterCompletion(final MessageContext messageContext, final Object endpoint, final Exception ex) {
	// nothing to do.
    }

    private Transformer createNonIndentingTransformer() throws TransformerConfigurationException {
	Transformer transformer = this.transformerHelper.createTransformer();
	transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	transformer.setOutputProperty(OutputKeys.INDENT, "no");
	return transformer;
    }

    /**
     * @return the contextPath
     */
    public String getContextPath() {
	return this.contextPath;
    }

    private Logger getLogger() {
	if (this.logger == null) {
	    if (this.loggerName == null || this.loggerName.trim().length() == 0) {
		this.logger = LoggerFactory.getLogger(PayloadLogger.class);
	    } else {
		this.logger = LoggerFactory.getLogger(this.loggerName.trim());
	    }
	}
	return this.logger;
    }

    /**
     * @return the loggername
     */
    public String getLoggerName() {
	return this.loggerName;
    }

    /**
     * @return
     */
    private Marshaller getMarshaller() {
	Marshaller marshaller = null;
	try {
	    if (this.jaxbContext == null) {
		this.jaxbContext = JAXBContext.newInstance(this.contextPath);
	    }
	    marshaller = this.jaxbContext.createMarshaller();
	    marshaller.setProperty("jaxb.formatted.output", true);
	} catch (Exception e) {
	    // noting to do here.
	}
	return marshaller;
    }

    private String getMessage(final Message<?> message) {
	Object logMessage = this.expression.getValue(this.evaluationContext, message);
	if (JAXBElement.class.isInstance(logMessage)) {
	    JAXBElement<?> elem = (JAXBElement<?>) logMessage;
	    try {
		Marshaller marshaller = this.getMarshaller();
		if (marshaller != null) {
		    StringWriter sw = new StringWriter();
		    marshaller.marshal(logMessage, sw);
		    logMessage = sw.toString();
		}
	    } catch (Exception e) {
		logMessage = elem.getValue();
	    }
	} else if (logMessage instanceof Throwable) {
	    StringWriter stringWriter = new StringWriter();
	    if (logMessage instanceof AggregateMessageDeliveryException) {
		stringWriter.append(((Throwable) logMessage).getMessage());
		for (Exception exception : (List<? extends Exception>) ((AggregateMessageDeliveryException) logMessage)
			.getAggregatedExceptions()) {
		    exception.printStackTrace(new PrintWriter(stringWriter, true));
		}
	    } else {
		((Throwable) logMessage).printStackTrace(new PrintWriter(stringWriter, true));
	    }
	    logMessage = stringWriter.toString();
	}
	StringBuffer sb = new StringBuffer(1024);
	sb.append(MessageHeaders.ID).append("=").append(message.getHeaders().getId());
	Object correlationId = new IntegrationMessageHeaderAccessor(message).getCorrelationId();
	if (correlationId != null) {
	    sb.append(", ");
	    sb.append(IntegrationMessageHeaderAccessor.CORRELATION_ID).append("=").append(correlationId);
	}
	sb.append("\n");
	sb.append(String.valueOf(logMessage));
	return sb.toString();
    }

    protected Source getSource(final WebServiceMessage message) {
	return message.getPayloadSource();
    }

    @Override
    public boolean handleFault(final MessageContext messageContext) throws WebServiceClientException {
	return true;
    }

    @Override
    public boolean handleFault(final MessageContext messageContext, final Object endpoint) throws Exception {
	return this.handleFault(messageContext);
    }

    @Override
    public boolean handleRequest(final MessageContext messageContext) throws WebServiceClientException {
	if (this.getLogger().isDebugEnabled()) {
	    this.logMessageSource("Request: \n", this.getSource(messageContext.getRequest()));
	}
	return true;
    }

    @Override
    public boolean handleRequest(final MessageContext messageContext, final Object endpoint) throws Exception {
	return this.handleRequest(messageContext);
    }

    @Override
    public boolean handleResponse(final MessageContext messageContext) throws WebServiceClientException {
	if (this.getLogger().isDebugEnabled()) {
	    this.logMessageSource("Response: \n", this.getSource(messageContext.getResponse()));
	}
	return true;
    }

    @Override
    public boolean handleResponse(final MessageContext messageContext, final Object endpoint) throws Exception {
	return this.handleResponse(messageContext);
    }

    private void logMessage(final Message<?> message, final String channelName) {
	String logMessage = this.getMessage(message);
	this.logMessage(new StringBuffer(1024).append("Message (").append(channelName).append("): \n")
		.append(logMessage).toString());
    }

    /**
     * Logs the given string message and replaces the regex
     * <code>[Pp]assword=".*?"</code> by <code>password="***"</code>.
     * <p/>
     * By default, this method uses a "debug" level of logging. Subclasses can
     * override this method to change the level of logging used by the logger.
     *
     * @param message
     *            the message
     */
    protected void logMessage(final String message) {
	this.getLogger().debug(message.replaceAll("[Pp]assword=\\\".*?\\\"", "password=\"***\"")
		.replaceAll("[Pp]assword>.*?</", "password>***</"));
    }

    /**
     * Logs the given {@link Source source} to the {@link #logger}, using the
     * message as a prefix.
     * <p/>
     * By default, this message creates a string representation of the given
     * source, and delegates to {@link #logMessage(String)}.
     *
     * @param logMessage
     *            the log message
     * @param source
     *            the source to be logged
     * @throws TransformerException
     *             in case of errors
     */
    private void logMessageSource(final String logMessage, final Source source) {
	if (source != null) {
	    try {
		Transformer transformer = this.createNonIndentingTransformer();
		StringWriter writer = new StringWriter();
		transformer.transform(source, new StreamResult(writer));
		this.logMessage(new StringBuffer(1024).append(logMessage).append(String.valueOf(writer)).toString());
	    } catch (Exception e) {
		this.logMessage(new StringBuffer(logMessage).append(e.getMessage()).toString());
	    }
	}
    }

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
	if (this.getLogger().isDebugEnabled()) {
	    this.logMessage(message, String.valueOf(channel));
	}
	return super.preSend(message, channel);
    }

    /**
     * @param contextPath
     *            the contextPath to set
     */
    public void setContextPath(final String contextPath) {
	this.contextPath = contextPath;
    }

    /**
     * @param loggername
     *            the loggername to set
     */
    public void setLoggerName(final String loggername) {
	this.loggerName = loggername;
    }

    /**
     *
     * @see org.springframework.ws.client.support.interceptor.ClientInterceptor#afterCompletion(org.springframework.ws.context.MessageContext,
     *      java.lang.Exception)
     */
    @Override
    public void afterCompletion(final MessageContext messageContext, final Exception e)
	    throws WebServiceClientException {
	if (this.getLogger().isDebugEnabled()) {
	    this.logMessageSource("Completed: \n", this.getSource(messageContext.getRequest()));
	}
    }
}
