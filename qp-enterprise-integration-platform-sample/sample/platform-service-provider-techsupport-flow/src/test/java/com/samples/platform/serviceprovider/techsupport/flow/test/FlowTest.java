/*******************************************************************************
<<<<<<< HEAD
 * Copyright (c) 2013, 2014, 2015 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
=======
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
>>>>>>> d2e28feb83823d2f089847490a12e7352b2037ba
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.samples.platform.serviceprovider.techsupport.flow.test;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.qpark.eip.core.DateUtil;
<<<<<<< HEAD
import com.qpark.eip.inf.FlowContext;
import com.qpark.eip.service.base.msg.FailureType;
import com.samples.platform.core.flow.FlowContextImpl;
=======
import com.qpark.eip.service.base.msg.FailureType;
>>>>>>> d2e28feb83823d2f089847490a12e7352b2037ba
import com.samples.platform.inf.iss.tech.support.ext.type.ExtSystemUserLogType;
import com.samples.platform.inf.iss.tech.support.flow.SystemUserReportFlow;
import com.samples.platform.inf.iss.tech.support.flow.SystemUserReportFlowRequestType;
import com.samples.platform.inf.iss.tech.support.flow.SystemUserReportFlowResponseType;
import com.samples.platform.inf.iss.tech.support.msg.SystemUserLogResponseType;
import com.samples.platform.model.iss.tech.support.SystemUserReportCriteriaType;
import com.samples.platform.service.iss.tech.support.msg.GetSystemUserReportRequestType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class,
		loader = AnnotationConfigContextLoader.class)
public class FlowTest {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory.getLogger(FlowTest.class);

	@Autowired
	private SystemUserReportFlow flow;
	@Autowired
	private MockFlowGateway flowGateway;

	@Test
	public void testPositive() {
		this.logger.debug("+testPositive");
		XMLGregorianCalendar requestDate = DateUtil.get(new Date());

<<<<<<< HEAD
		String userName = "userName";

		FlowContext flowContext = this.setupFlowContext(userName);
=======
>>>>>>> d2e28feb83823d2f089847490a12e7352b2037ba
		this.setupFlowGateway(requestDate);

		SystemUserReportFlowRequestType request = new SystemUserReportFlowRequestType();
		request.setIn(new GetSystemUserReportRequestType());
		request.getIn().setUserName("userName");
		request.getIn().setCriteria(new SystemUserReportCriteriaType());
		request.getIn().getCriteria().setDate(requestDate);

		SystemUserReportFlowResponseType response = this.flow
<<<<<<< HEAD
				.invokeFlow(request, flowContext);
=======
				.invokeFlow(request);
>>>>>>> d2e28feb83823d2f089847490a12e7352b2037ba

		Assert.assertEquals("Request date is not as expected",
				String.valueOf(requestDate), String.valueOf(this.flowGateway
						.getRequest().getCriteria().getLogDate()));
		Assert.assertEquals("Response size not as expected",
				response.getOut().getReport().size(),
				this.flowGateway.getResponse().getSystemUserLog().size());
		Assert.assertEquals("Response order is not as expected",
				response.getOut().getReport().get(0).getOperation(),
				this.flowGateway.getResponse().getSystemUserLog().get(0)
						.getOperationName());
		Assert.assertEquals("Response failure size not as expected",
				response.getOut().getFailure().size(),
				this.flowGateway.getResponse().getFailure().size());
		this.logger.debug("-testPositive");
	}

<<<<<<< HEAD
	private FlowContext setupFlowContext(final String userName) {
		FlowContext flowContext = new FlowContextImpl();
		flowContext.setRequesterOperationName("techSupportFlowTest");
		flowContext.setRequesterServiceName("iss.tech.support");
		flowContext.setRequesterServiceVersion("2.0.0");
		flowContext.setRequesterUserName(userName);
		return flowContext;
	}

=======
>>>>>>> d2e28feb83823d2f089847490a12e7352b2037ba
	private void setupFlowGateway(final XMLGregorianCalendar requestDate) {
		SystemUserLogResponseType gatewayResponse = new SystemUserLogResponseType();

		ExtSystemUserLogType log;
		log = new ExtSystemUserLogType();
		gatewayResponse.getSystemUserLog().add(log);
		log.setContext("context0");
		log.setLogDate(requestDate);
		log.setRequestsDenied(10);
		log.setRequestsGranted(20);
		log.setResponseFaults(30);
		log.setServiceName("service0");
		log.setUserName("userName0");
		log.setVersion("version0");

		log = new ExtSystemUserLogType();
		gatewayResponse.getSystemUserLog().add(log);
		log.setContext("context1");
		log.setLogDate(requestDate);
		log.setRequestsDenied(11);
		log.setRequestsGranted(21);
		log.setResponseFaults(31);
		log.setServiceName("service1");
		log.setUserName("userName1");
		log.setVersion("version1");

		FailureType f = new FailureType();
		f.setErrorDetails("details");
		f.setSeverity("ERROR");
		f.setUserMessage("userMessage");
		gatewayResponse.getFailure().add(f);

		RuntimeException runtimeException = null;

		this.flowGateway.setRequest(null);
		this.flowGateway.setResponse(gatewayResponse);
		this.flowGateway.setRuntimeException(runtimeException);
	}
}
