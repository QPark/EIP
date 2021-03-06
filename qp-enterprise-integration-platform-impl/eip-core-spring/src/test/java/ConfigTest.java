/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * The client API.
 *
 * @author bhausen
 */
public class ConfigTest {
	/** The {@link org.slf4j.Logger}. */
	private final Logger logger = LoggerFactory.getLogger(ConfigTest.class);

	/**
	 * Start test of the client.
	 *
	 * @param args
	 *            no args expected.
	 */
	public static void main(final String[] args) {
		try {
			new ConfigTest().run();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	/** Create the client test. */
	public ConfigTest() {
		this.applicationContext = new AnnotationConfigApplicationContext(
				ConfigA2.class);

		this.logger.info("Default timezone is {}",
				TimeZone.getDefault().getID());
	}

	/** The client tests {@link ApplicationContext}. */
	private final ApplicationContext applicationContext;

	/** Run the test. */
	private void run() {
		this.logger.info("+run");
		Object configAStringBean = this.applicationContext
				.getBean("ConfigAStringBean");

		System.out.println(String.valueOf(configAStringBean));
		this.logger.info("-run");
	}
}
