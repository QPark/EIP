package com.samples.platform.serviceprovider.techsupport.flow.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * @author bhausen
 */
@RunWith(Suite.class)
@SuiteClasses({ FlowTestPositive.class })
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class TestSuiteSystemUserReportFlow {
}
