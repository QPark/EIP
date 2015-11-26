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
import com.samples.platform.inf.iss.tech.support.flow.SystemUserReportFlow;
import com.samples.platform.inf.iss.tech.support.flow.SystemUserReportFlowRequestType;
import com.samples.platform.model.iss.tech.support.SystemUserReportCriteriaType;
import com.samples.platform.service.iss.tech.support.msg.GetSystemUserReportRequestType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class,
		loader = AnnotationConfigContextLoader.class)
public class FlowTestPositive {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory.getLogger(FlowTestPositive.class);

	@Autowired
	private SystemUserLogFlowGatewayImpl gateway;

	@Autowired
	private SystemUserReportFlow flow;

	@Test
	public void test() {
		this.logger.debug("+test");
		XMLGregorianCalendar requestDate = DateUtil.get(new Date());
		SystemUserReportFlowRequestType request = new SystemUserReportFlowRequestType();
		request.setIn(new GetSystemUserReportRequestType());
		request.getIn().setUserName("userName");
		request.getIn().setCriteria(new SystemUserReportCriteriaType());
		request.getIn().getCriteria().setDate(requestDate);
		this.flow.invokeFlow(request);

		Assert.assertEquals("Request date is not the same",
				String.valueOf(requestDate), String.valueOf(
						this.gateway.getRequest().getCriteria().getLogDate()));
		this.logger.debug("-test");
	}
}
