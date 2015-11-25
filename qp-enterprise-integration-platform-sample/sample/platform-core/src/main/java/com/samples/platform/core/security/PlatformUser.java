/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting S.a r.l. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0. The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html. Contributors: Bernhard Hausen -
 * Initial API and implementation
 ******************************************************************************/
package com.samples.platform.core.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * @author bhausen
 */
public class PlatformUser extends User {

	/**
	 * @param username
	 * @param password
	 * @param enabled
	 * @param accountNonExpired
	 * @param credentialsNonExpired
	 * @param accountNonLocked
	 * @param authorities
	 */
	public PlatformUser(final String username, final String password,
			final boolean enabled, final boolean accountNonExpired,
			final boolean credentialsNonExpired, final boolean accountNonLocked,
			final Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked, authorities);
	}

	/*
	 * <pre> ####################################
	 * ###########eraseCredentials######### java.lang.Exception:
	 * org.springframework.security.core.userdetails.User.eraseCredentials
	 * called at
	 * com.samples.platform.core.security.PlatformUser.eraseCredentials
	 * (BusUser.java:35) at
	 * org.springframework.security.authentication.AbstractAuthenticationToken
	 * .eraseSecret(AbstractAuthenticationToken.java:115) at
	 * org.springframework.
	 * security.authentication.AbstractAuthenticationToken.eraseCredentials
	 * (AbstractAuthenticationToken.java:109) at
	 * org.springframework.security.authentication
	 * .UsernamePasswordAuthenticationToken
	 * .eraseCredentials(UsernamePasswordAuthenticationToken.java:96) at
	 * org.springframework
	 * .security.authentication.ProviderManager.authenticate(ProviderManager
	 * .java:186) at
	 * org.springframework.security.access.intercept.AbstractSecurityInterceptor
	 * .authenticateIfRequired(AbstractSecurityInterceptor.java:316) at
	 * org.springframework
	 * .security.access.intercept.AbstractSecurityInterceptor.
	 * beforeInvocation(AbstractSecurityInterceptor.java:202) at
	 * org.springframework
	 * .integration.security.channel.ChannelSecurityInterceptor
	 * .invokeWithAuthorizationCheck(ChannelSecurityInterceptor.java:61) at
	 * org.springframework
	 * .integration.security.channel.ChannelSecurityInterceptor
	 * .invoke(ChannelSecurityInterceptor.java:54) at
	 * org.springframework.aop.framework
	 * .ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:172)
	 * at org.springframework.aop.framework.JdkDynamicAopProxy.invoke(
	 * JdkDynamicAopProxy.java:204) at $Proxy196.send(Unknown Source) at
	 * org.springframework
	 * .integration.core.MessagingTemplate.doSend(MessagingTemplate.java:288) at
	 * org.springframework.integration.core.MessagingTemplate.doSendAndReceive(
	 * MessagingTemplate.java:318) at
	 * org.springframework.integration.core.MessagingTemplate
	 * .sendAndReceive(MessagingTemplate.java:239) at
	 * org.springframework.integration
	 * .gateway.MessagingGatewaySupport.doSendAndReceive
	 * (MessagingGatewaySupport.java:233) at
	 * org.springframework.integration.gateway
	 * .MessagingGatewaySupport.sendAndReceiveMessage
	 * (MessagingGatewaySupport.java:207) at
	 * org.springframework.integration.ws.MarshallingWebServiceInboundGateway
	 * .doInvoke(MarshallingWebServiceInboundGateway.java:101) at
	 * org.springframework
	 * .integration.ws.AbstractWebServiceInboundGateway.invoke
	 * (AbstractWebServiceInboundGateway.java:53) at
	 * org.springframework.ws.server
	 * .endpoint.adapter.MessageEndpointAdapter.invoke
	 * (MessageEndpointAdapter.java:41) at
	 * org.springframework.ws.server.MessageDispatcher
	 * .dispatch(MessageDispatcher.java:233) at
	 * org.springframework.ws.server.MessageDispatcher
	 * .receive(MessageDispatcher.java:173) at
	 * org.springframework.ws.transport.support
	 * .WebServiceMessageReceiverObjectSupport
	 * .handleConnection(WebServiceMessageReceiverObjectSupport.java:88) at
	 * org.springframework
	 * .ws.transport.http.WebServiceMessageReceiverHandlerAdapter
	 * .handle(WebServiceMessageReceiverHandlerAdapter.java:59) at
	 * org.springframework.ws.transport.http.MessageDispatcherServlet.doService(
	 * MessageDispatcherServlet.java:239) at
	 * org.springframework.web.servlet.FrameworkServlet
	 * .processRequest(FrameworkServlet.java:936) at
	 * org.springframework.web.servlet
	 * .FrameworkServlet.doPost(FrameworkServlet.java:838) at
	 * javax.servlet.http.HttpServlet.service(HttpServlet.java:641) : </pre>
	 */
	@Override
	public void eraseCredentials() {
		// System.out.println("####################################");
		//System.out.println("###########eraseCredentials#########");
		// new Exception(
		// "org.springframework.security.core.userdetails.User.eraseCredentials
		// called")
		// .printStackTrace();
		// System.out.println("####################################");
		// this.password = null;
	}
}
