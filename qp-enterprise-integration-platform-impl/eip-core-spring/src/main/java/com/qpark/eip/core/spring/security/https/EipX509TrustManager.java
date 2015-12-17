/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.spring.security.https;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.annotation.PostConstruct;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.springframework.core.io.Resource;

/**
 * If the SSL/TLS implementation's hostname verification logic (TrustManager)
 * fails, the verify method (HostnameVerifier) returns <code>false</code>.
 * 
 * <pre>
 * http://docs.oracle.com/javase/1.5.0/docs/guide/security/jsse/JSSERefGuide.html#TrustManager
 * http://docs.oracle.com/javase/1.5.0/docs/guide/security/jsse/JSSERefGuide.html#HostnameVerifier
 * </pre>
 * 
 * @author bhausen
 */
public class EipX509TrustManager implements X509TrustManager, HostnameVerifier {
    /** The {@link Logger}. */
    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(EipX509TrustManager.class);
    /** The key store password. */
    private char[] keystorePassword;

    /** {@link Resource} pointing to the service bus JKS key store. */
    private Resource keystore;

    /**
     * The default X509TrustManager returned by SunX509. We'll delegate
     * decisions to it, and fall back to the logic in this class if the default
     * X509TrustManager doesn't trust it.
     */
    private X509TrustManager sunJSSEX509TrustManager;

    /** The {@link KeyStore} itself. */
    private KeyStore ks;

    /**
     * Delegate to the default trust manager.
     * 
     * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.X509Certificate[],
     *      java.lang.String)
     */
    @Override
    public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
	try {
	    if (this.logger.isTraceEnabled()) {
		this.logger.trace("checkClientTrusted authType {}", authType);
		for (X509Certificate cert : chain) {
		    this.logger.trace("checkClientTrusted X509Certificate {}", cert);
		}
	    }
	    this.sunJSSEX509TrustManager.checkClientTrusted(chain, authType);
	    this.logger.debug("checkClientTrusted trusted");
	} catch (CertificateException e) {
	    this.logger.error("checkClientTrusted NOT trusted");
	    throw e;
	}
    }

    /**
     * Delegate to the default trust manager.
     * 
     * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.X509Certificate[],
     *      java.lang.String)
     */
    @Override
    public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
	try {
	    if (this.logger.isTraceEnabled()) {
		this.logger.trace("checkServerTrusted authType {}", authType);
		for (X509Certificate cert : chain) {
		    this.logger.trace("checkServerTrusted X509Certificate {}", cert);
		}
	    }
	    this.sunJSSEX509TrustManager.checkServerTrusted(chain, authType);
	    this.logger.debug("checkServerTrusted trusted");
	} catch (CertificateException e) {
	    this.logger.error("checkServerTrusted NOT trusted");
	    throw e;
	}
    }

    /**
     * Merely pass this through.
     * 
     * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
     */
    @Override
    public X509Certificate[] getAcceptedIssuers() {
	return this.sunJSSEX509TrustManager.getAcceptedIssuers();
    }

    /**
     * Initialize.
     * 
     * @throws Exception
     */
    @PostConstruct
    public void init() throws Exception {
	// create a "default" JSSE X509TrustManager.
	this.ks = KeyStore.getInstance("JKS");
	if (this.keystore != null) {
	    this.ks.load(this.keystore.getInputStream(), this.keystorePassword);
	}

	TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509", "SunJSSE");
	tmf.init(this.ks);

	TrustManager tms[] = tmf.getTrustManagers();

	/*
	 * Iterate over the returned trust managers, look for an instance of
	 * X509TrustManager. If found, use that as our "default" trust manager.
	 */
	for (TrustManager tm : tms) {
	    if (tm instanceof X509TrustManager) {
		this.sunJSSEX509TrustManager = (X509TrustManager) tm;
		return;
	    }
	}

	/*
	 * Find some other way to initialize, or else we have to fail the
	 * constructor.
	 */
	throw new Exception("Couldn't initialize");
    }

    /**
     * @param keystore
     *            the key store to set.
     */
    public void setKeystore(final Resource keystore) {
	this.keystore = keystore;
    }

    /**
     * @param keystorePassword
     *            the keystorePassword to set.
     */
    public void setKeystorePassword(final String keystorePassword) {
	if (keystorePassword != null) {
	    this.keystorePassword = keystorePassword.toCharArray();
	}
    }

    /**
     * <pre>
     * http://docs.oracle.com/javase/1.5.0/docs/guide/security/jsse/JSSERefGuide.html#HostnameVerifier
     * </pre>
     * 
     * If the SSL/TLS implementation's standard hostname verification logic
     * fails, the implementation will call the verify method of the class which
     * implements this interface and is assigned to this HttpsURLConnection
     * instance. If the callback class can determine that the hostname is
     * acceptable given the parameters, it should report that the connection
     * should be allowed. An unacceptable response will cause the connection to
     * be terminated.
     * 
     * @see javax.net.ssl.HostnameVerifier#verify(java.lang.String,
     *      javax.net.ssl.SSLSession)
     */
    @Override
    public boolean verify(final String hostname, final SSLSession session) {
	try {
	    this.logger.debug("verify hostname={}", hostname);
	    if (hostname != null && session != null && session.getPeerCertificateChain() != null
		    && session.getPeerCertificateChain().length > 0 && session.getPeerCertificateChain()[0] != null
		    && session.getPeerCertificateChain()[0].getPublicKey() != null) {
		Certificate cert = this.ks.getCertificate(hostname);
		if (cert != null && cert.getPublicKey() != null) {
		    String ksPublicKey = cert.getPublicKey().toString();
		    String serverPublicKey = session.getPeerCertificateChain()[0].getPublicKey().toString();
		    if (ksPublicKey.equals(serverPublicKey)) {
			return true;
		    } else {
			this.logger.debug("verify not matching public keys!");
			this.logger.debug("verify public key from keystore={}", ksPublicKey);
			this.logger.debug("verify public key from server  ={}", serverPublicKey);
		    }
		} else {
		    this.logger.debug("verify no cert({}) with PublicKey found.", cert);
		}
	    } else {
		this.logger.debug("verify no hostname({}) or session with PeerCertificateChain and PublicKey.",
			hostname);
	    }
	} catch (KeyStoreException e) {
	    this.logger.debug("verify {}", e.getMessage());
	} catch (SSLPeerUnverifiedException e) {
	    this.logger.debug("verify {}", e.getMessage());
	}
	return false;
    }
}
