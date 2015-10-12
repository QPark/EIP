/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting  S.�r.l.
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
package com.qpark.eip.core.failure;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.SSLHandshakeException;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.MessagingException;
import org.springframework.oxm.UnmarshallingFailureException;
import org.springframework.remoting.soap.SoapFaultException;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.WebServiceTransportException;
import org.springframework.ws.soap.client.SoapFaultClientException;

import com.qpark.eip.core.failure.domain.FailureMessageListType;
import com.qpark.eip.core.failure.domain.FailureMessagePhraseType;
import com.qpark.eip.core.failure.domain.FailureMessagePhraseableType;
import com.qpark.eip.core.failure.domain.FailureMessageSeverity;
import com.qpark.eip.core.failure.domain.FailureMessageType;
import com.qpark.eip.core.xml.AbstractUnmarschaller;
import com.springsource.insight.annotation.InsightOperation;

/**
 * Handle failures.
 * 
 * @author bhausen
 */
public abstract class BaseFailureHandler {
    /** The default error code if non presented. */
    private static final String DEFAULT = "E_ALL_NOT_KNOWN_ERROR";
    /** The default database error code if non presented. */
    private static final String DEFAULT_DATABASE = "E_DATABASE_ERROR";

    private static final String FAILURE_MESSAGES_XML_BASE = "failure-messages-base.xml";
    public static final String FAILURE_MESSAGES_XML = "failure-messages.xml";

    /** The {@link Logger}. */
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BaseFailureHandler.class);

    private static AbstractUnmarschaller MARSHALLER = new AbstractUnmarschaller() {
	@Override
	protected String getContextPath() {
	    return FailureMessageListType.class.getPackage().getName();
	}
    };

    private static String checkUnnumberedBrackets(final String message) {
	String s = message.trim();
	int index = s.indexOf("{}");
	if (index > -1) {
	    int number = 0;
	    if (s.indexOf("{0}") > 0) {
		number = 0;
	    } else if (s.indexOf("{1}") > 0) {
		number = 1;
	    } else if (s.indexOf("{2}") > 0) {
		number = 2;
	    } else if (s.indexOf("{3}") > 0) {
		number = 3;
	    } else if (s.indexOf("{4}") > 0) {
		number = 4;
	    } else if (s.indexOf("{5}") > 0) {
		number = 5;
	    }
	    index = s.indexOf(new StringBuffer().append("{").append(number).append("}").toString());
	    while (index > -1) {
		number++;
		index = s.indexOf(new StringBuffer().append("{").append(number).append("}").toString());
	    }
	    index = s.indexOf("{}");
	    while (index > -1) {
		s = s.replaceFirst("\\{\\}", new StringBuffer().append("{").append(number).append("}").toString());
		number++;
		index = s.indexOf("{}");
	    }
	}
	return s;
    }

    private static final HashMap<String, FailureMessageType> messages = new HashMap<String, FailureMessageType>();

    private static final HashMap<String, String> phrases = new HashMap<String, String>();

    private static String format(final FailureMessagePhraseableType m, final Object... data) {
	StringBuffer sb = new StringBuffer(1024);
	if (m != null) {
	    if (m.getText() != null && m.getText().trim().length() > 0) {
		if (data != null && data.length > 0) {
		    MessageFormat mf = new MessageFormat(checkUnnumberedBrackets(m.getText()));
		    sb.append(mf.format(data));
		} else {
		    sb.append(m.getText());
		}
	    }
	    if (m.getPhraseKey() != null) {
		for (String key : m.getPhraseKey()) {
		    if (phrases.get(key) != null && phrases.get(key).length() > 0) {
			if (sb.length() > 0) {
			    sb.append(" ");
			}
		    }
		    sb.append(phrases.get(key));
		}
	    }
	}
	return sb.toString();
    }

    public static FailureDescription getFailure(final String code, final Object... data) {
	return getFailure(code, (Throwable) null, data);
    }

    public static FailureDescription getFailure(final String code, final Throwable t) {
	return getFailure(code, t, (Object[]) null);
    }

    public static FailureDescription getFailure(final String code, final Throwable t, final Object... data) {
	FailureDescription fd = new FailureDescription();
	StringBuffer supportInfo = new StringBuffer(128);
	FailureMessageType fm = null;
	if (code != null) {
	    fm = getFailureMessage(code);
	    if (fm == null) {
		fm = getFailureMessage(DEFAULT);
		supportInfo.append("No definition found for code ").append(code).append(" in failure-messages.xml.\n");
	    }
	} else {
	    fm = getFailureMessage(DEFAULT);
	}
	Object[] datax = data;
	if (datax == null) {
	    datax = new Object[] { "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };
	}

	supportInfo.insert(0, format(fm.getSupportInformation(), datax));

	fd.setWhatHappened(format(fm.getWhatHappened(), datax));
	fd.setWhyHappened(format(fm.getWhyHappened(), datax));
	fd.setWhatToDo(format(fm.getWhatToDo(), datax));
	if (fm.getSeverity() != null && fm.getSeverity().equals(FailureMessageSeverity.WARNING)) {
	    fd.setSeverity(FailureMessageSeverity.WARNING);
	} else {
	    fd.setSeverity(FailureMessageSeverity.ERROR);
	}

	fd.setErrorCode(code);

	fd.setSupportInformation(supportInfo.toString());

	if (fd.getSeverity().equals(FailureMessageSeverity.ERROR)) {
	    if (t != null) {
		showFailureDescription(t.getClass().getSimpleName(), t.getMessage(), fd.getErrorCode(),
			fd.getSeverity().name(), fd.getWhatHappened(), fd.getWhyHappened(), fd.getWhatToDo());
	    } else {
		showFailureDescription("", "", fd.getErrorCode(), fd.getSeverity().name(), fd.getWhatHappened(),
			fd.getWhyHappened(), fd.getWhatToDo());
	    }
	}

	StringBuffer sb = new StringBuffer(256);
	sb.append("Occurred at: ").append(new Date());
	if (t != null) {
	    sb.append("\n").append(t.getMessage()).append("\n");
	    sb.append(getStackTrace(t));
	}
	fd.setErrorDetails(sb.toString());
	return fd;
    }

    private static FailureMessageType getFailureMessage(final String code) {
	FailureMessageType fm = null;
	if (messages.size() == 0) {
	    addBaseMessages();
	}
	fm = messages.get(code);
	return fm;
    }

    private static void addBaseMessages() {
	InputStream is = BaseFailureHandler.class
		.getResourceAsStream(new StringBuffer(32).append("/").append(FAILURE_MESSAGES_XML_BASE).toString());
	if (is != null) {
	    try {
		FailureMessageListType failuresMessageList = (FailureMessageListType) MARSHALLER.getValue(is,
			FailureMessageListType.class);
		addFailureMessageList(failuresMessageList);
	    } catch (JAXBException e) {
		e.printStackTrace();
	    }
	}
    }

    private static void addFailureMessageList(final FailureMessageListType failuresMessageList) {
	if (failuresMessageList != null) {
	    for (FailureMessagePhraseType phr : failuresMessageList.getMessagePhrase()) {
		phrases.put(phr.getKey(), phr.getPhrase());
	    }
	    for (FailureMessageType msg : failuresMessageList.getMessage()) {
		messages.put(msg.getCode(), msg);
	    }
	}
    }

    public static void addFailureMessages(final InputStream is) {
	FailureMessageListType failuresMessageList;
	if (messages.size() == 0) {
	    addBaseMessages();
	}
	if (is != null) {
	    try {
		failuresMessageList = (FailureMessageListType) MARSHALLER.getValue(is, FailureMessageListType.class);
		addFailureMessageList(failuresMessageList);
	    } catch (JAXBException e) {
		e.printStackTrace();
	    }
	}
    }

    private static final FailureMessageListType getMessageListType(final String filename) throws JAXBException {
	InputStream is = BaseFailureHandler.class.getResourceAsStream(filename);

	FailureMessageListType edited = (FailureMessageListType) MARSHALLER.getValue(is, FailureMessageListType.class);
	return edited;
    }

    private static String getStackTrace(final Throwable t) {
	if (t != null) {
	    Writer w = new StringWriter();
	    PrintWriter pw = new PrintWriter(w);
	    t.printStackTrace(pw);
	    return w.toString();
	} else {
	    return "";
	}
    }

    /**
     * Handles the exception classes:
     * <ul>
     * <li>{@link FailureException}</li>
     * <li>{@link SoapFaultClientException}</li>
     * <li>{@link Exception}</li>
     * </ul>
     * 
     * @param e
     * @param rp
     * @param defaultCode
     * @param log
     */
    public static FailureDescription handleException(final Throwable e, final String defaultCode, final Logger log,
	    final Object... data) {
	FailureDescription fd = null;
	if (InvocationTargetException.class.isInstance(e)) {
	    fd = handleException(((InvocationTargetException) e).getTargetException(), defaultCode, log, data);
	} else if (MessageHandlingException.class.isInstance(e)) {
	    fd = handleException(((MessagingException) e).getCause(), defaultCode, log, data);
	} else if (MessagingException.class.isInstance(e)) {
	    fd = handleException(((MessagingException) e).getCause(), defaultCode, log, data);
	} else if (SoapFaultClientException.class.isInstance(e)) {
	    ArrayList<Object> list = new ArrayList<Object>(data == null ? 1 : data.length + 1);
	    if (data != null) {
		list.addAll(Arrays.asList(data));
	    }
	    if (e.getMessage() != null) {
		list.add(0, e.getMessage());
	    }
	    fd = getFailure("E_SOAP_FAULT_CLIENT_ERROR", e, list.toArray(new Object[list.size()]));
	    if (log.isDebugEnabled()) {
		log.error(e.getMessage(), e);
	    } else {
		log.error(e.getMessage());
	    }
	} else if (WebServiceIOException.class.isInstance(e)) {
	    fd = handleWebServiceIOException((WebServiceIOException) e, defaultCode, log, data);
	} else if (SoapFaultException.class.isInstance(e)) {
	    ArrayList<Object> list = new ArrayList<Object>(data == null ? 1 : data.length + 1);
	    if (data != null) {
		list.addAll(Arrays.asList(data));
	    }
	    if (e.getMessage() != null) {
		list.add(0, e.getMessage());
	    }
	    fd = handleSoapFaultException((SoapFaultException) e, defaultCode, log,
		    list.toArray(new Object[list.size()]));
	    if (fd == null) {
		fd = getFailure("E_SOAP_MESSAGE_VALIDATION_ERROR", e);
	    }
	    if (log.isDebugEnabled()) {
		log.error(e.getMessage(), e);
	    } else {
		log.error(e.getMessage());
	    }
	} else if (SQLException.class.isInstance(e)) {
	    fd = handleSQLException((SQLException) e, DEFAULT_DATABASE, log, data);
	} else if (e.getCause() != null) {
	    fd = handleException(e.getCause(), defaultCode, log, data);
	} else {
	    fd = getFailure(defaultCode == null ? DEFAULT : defaultCode, e);
	    if (log.isDebugEnabled()) {
		log.error(e.getMessage(), e);
	    } else {
		log.error(e.getMessage());
	    }
	}
	return fd;
    }

    private static FailureDescription handleSoapFaultException(final SoapFaultException e, final String defaultCode,
	    final Logger log, final Object... data) {
	FailureDescription fd = null;
	if (e.getCause() != null && WebServiceIOException.class.isInstance(e.getCause())) {
	    fd = handleWebServiceIOException((WebServiceIOException) e.getCause(), defaultCode, log, data);
	} else if (e.getCause() != null && WebServiceTransportException.class.isInstance(e.getCause())) {
	    fd = getFailure("E_SOAP_FAULT_CREDENTIAL_ERROR", e, data);
	} else if (e.getCause() != null && UnmarshallingFailureException.class.isInstance(e.getCause())) {
	    fd = getFailure("E_SOAP_FAULT_MARSHALLING_ERROR", e, data);
	}
	return fd;
    }

    /**
     * @param e
     *            {@link SQLException}
     * @param defaultCode
     * @param log
     * @param data
     * @return
     */
    public static FailureDescription handleSQLException(final SQLException e, final String defaultCode,
	    final Logger log, final Object... data) {
	FailureDescription fd = null;
	String code = defaultCode;
	if (code == null) {
	    code = DEFAULT_DATABASE;
	}
	fd = getFailure(code, e);
	log.error(e.getMessage(), e);
	return fd;
    }

    private static FailureDescription handleWebServiceIOException(final WebServiceIOException e,
	    final String defaultCode, final Logger log, final Object... data) {
	FailureDescription fd = null;
	if (e.getCause() != null && SSLHandshakeException.class.isInstance(e.getCause().getCause())) {
	    fd = getFailure("E_SOAP_FAULT_HTTPS_CERTIFICATE_ERROR", e, data);
	} else if (e.getCause() != null && ConnectException.class.isInstance(e.getCause().getCause())) {
	    fd = getFailure("E_SOAP_FAULT_SERVICE_NOT_AVAILABLE", e, data);
	} else if (e.getCause() != null && e.getCause().getClass().getPackage().getName().startsWith("java.net")) {
	    fd = getFailure("E_SOAP_FAULT_NETWORK_ERROR", e, data);
	} else {
	    fd = getFailure("E_SOAP_FAULT_SERVICE_NOT_AVAILABLE", e, data);
	}
	return fd;
    }

    /**
     * Show the {@link FailureDescription} in the insight stack
     * 
     * @param fd
     * @param code
     * @param data
     */
    @InsightOperation
    private static void showFailureDescription(final String exception, final String exceptionMessage,
	    final String errorCode, final String severity, final String whatHappened, final String whyHappened,
	    final String whatToDo) {
	// Only to be shown in insight.
    }

    /**
     * Throws a new {@link FailureException}.
     * 
     * @param code
     */
    public static void throwFailureException(final String code) {
	throwFailureException(code, (Throwable) null, (Object[]) null);
    }

    /**
     * Throws a new {@link FailureException}.
     * 
     * @param code
     * @param data
     */
    public static void throwFailureException(final String code, final Object... data) {
	throwFailureException(code, (Throwable) null, data);
    }

    /**
     * Throws a new {@link FailureException}.
     * 
     * @param code
     * @param cause
     */
    public static void throwFailureException(final String code, final Throwable cause) {
	throwFailureException(code, cause, (Object[]) null);
    }

    /**
     * Throws a new {@link FailureException}.
     * 
     * @param code
     * @param cause
     * @param data
     */
    public static void throwFailureException(final String code, final Throwable cause, final Object... data) {
	FailureDescription fd = getFailure(code, cause, data);
	FailureException e;
	if (cause == null) {
	    e = new FailureException(fd);
	} else {
	    e = new FailureException(fd, cause);
	}
	throw e;
    }
}
