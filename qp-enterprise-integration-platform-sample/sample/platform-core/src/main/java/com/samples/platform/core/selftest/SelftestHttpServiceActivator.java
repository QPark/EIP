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
package com.samples.platform.core.selftest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.Message;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.message.GenericMessage;

import com.springsource.insight.annotation.InsightEndPoint;

/**
 * @author bhausen
 */
public class SelftestHttpServiceActivator {
	/** The {@link Selftest}er. */
	@Autowired
	@Qualifier("selftest")
	private Selftest selftest;

	/**
	 * Test the common service.
	 * @return {@link Message} with HTML.
	 */
	@InsightEndPoint
	@ServiceActivator
	public Message<String> selftest(final Message<?> message) {
		String html = null;
		Map<String, Object> headers = new HashMap<String, Object>();
		String contentType = "text/html; charset=utf-8";
		String url = (String) message.getHeaders().get("http_requestUrl");
		if (url != null && url.contains("/QPark")) {
			try {
				String xsdLocation = url.substring(url.indexOf("/QPark"),
						url.length()).replace('?', '/');
				InputStream is = SelftestHttpServiceActivator.class
						.getResourceAsStream(xsdLocation);
				if (is != null) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					IOUtils.copy(is, baos);
					html = new String(baos.toByteArray(), "UTF-8");
					contentType = "text/xml; charset=utf-8";
				}
			} catch (IOException e) {
				html = null;
			}
		}
		if (html == null) {
			html = this.selftest.selftest(message);
		}
		headers.put("Content-Type", contentType);
		Message<String> m = new GenericMessage<String>(html, headers);
		return m;
	}
}
