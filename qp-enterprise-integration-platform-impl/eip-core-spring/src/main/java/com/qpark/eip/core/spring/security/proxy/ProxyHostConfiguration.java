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
package com.qpark.eip.core.spring.security.proxy;

import org.apache.commons.httpclient.HostConfiguration;

public class ProxyHostConfiguration extends HostConfiguration {
	private ProxyBean proxyBean;

	/**
	 * @return the proxyBean.
	 */
	public ProxyBean getProxyBean() {
		return this.proxyBean;
	}

	/**
	 * @see org.apache.commons.httpclient.HostConfiguration#getProxyHost()
	 */
	@Override
	public synchronized String getProxyHost() {
		return this.proxyBean.getProxyHost();
	}

	/**
	 * @see org.apache.commons.httpclient.HostConfiguration#getProxyPort()
	 */
	@Override
	public synchronized int getProxyPort() {
		String s = this.proxyBean.getProxyPort();
		if (s == null) {
			return 0;
		} else {
			return Integer.parseInt(s);
		}
	}

	/**
	 * @param proxyBean the proxyBean to set.
	 */
	public void setProxyBean(final ProxyBean proxyBean) {
		this.proxyBean = proxyBean;
	}
}
