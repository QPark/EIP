/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.jaxb.validation;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.w3c.dom.ls.LSInput;

/**
 * {@link LSInput} taking an {@link InputStream} to get the content.
 *
 * @author bhausen
 */
class InputStreamLSInput implements LSInput {
	/** Resolved resource. */
	private final BufferedInputStream bis;
	/** Public ID of the resolved resource. */
	private final String publicId;
	/** System ID of the resolved resource. */
	private final String systemId;

	/**
	 * @param publicId
	 *            public id of the resolved resource.
	 * @param systemId
	 *            system id of the resolved resource.
	 * @param is
	 *            the {@link InputStream} of the resolved resource.
	 */
	public InputStreamLSInput(final String publicId, final String systemId,
			final InputStream is) {
		this.publicId = publicId;
		this.systemId = systemId;
		this.bis = new BufferedInputStream(is);
	}

	/**
	 * @see org.w3c.dom.ls.LSInput#getBaseURI()
	 */
	@Override
	public String getBaseURI() {
		return null;
	}

	/**
	 * @see org.w3c.dom.ls.LSInput#getByteStream()
	 */
	@Override
	public InputStream getByteStream() {
		return this.bis;
	}

	/**
	 * @see org.w3c.dom.ls.LSInput#getCertifiedText()
	 */
	@Override
	public boolean getCertifiedText() {
		return false;
	}

	/**
	 * @see org.w3c.dom.ls.LSInput#getCharacterStream()
	 */
	@Override
	public Reader getCharacterStream() {
		return new InputStreamReader(this.bis);
	}

	/**
	 * @see org.w3c.dom.ls.LSInput#getEncoding()
	 */
	@Override
	public String getEncoding() {
		return null;
	}

	/**
	 * @see org.w3c.dom.ls.LSInput#getPublicId()
	 */
	@Override
	public String getPublicId() {
		return this.publicId;
	}

	/**
	 * @see org.w3c.dom.ls.LSInput#getStringData()
	 */
	@Override
	public String getStringData() {
		synchronized (this.bis) {
			try {
				this.bis.reset();
				byte[] input = new byte[this.bis.available()];
				this.bis.read(input);
				return new String(input);
			} catch (IOException e) {
				return null;
			}
		}
	}

	/**
	 * @see org.w3c.dom.ls.LSInput#getSystemId()
	 */
	@Override
	public String getSystemId() {
		return this.systemId;
	}

	/**
	 * @see org.w3c.dom.ls.LSInput#setBaseURI(java.lang.String)
	 */
	@Override
	public void setBaseURI(final String uri) {
		// Nothing here.
	}

	/**
	 * @see org.w3c.dom.ls.LSInput#setByteStream(java.io.InputStream)
	 */
	@Override
	public void setByteStream(final InputStream byteStream) {
		// Nothing here.
	}

	/**
	 * @see org.w3c.dom.ls.LSInput#setCertifiedText(boolean)
	 */
	@Override
	public void setCertifiedText(final boolean isCertifiedText) {
		// Nothing here.
	}

	/**
	 * @see org.w3c.dom.ls.LSInput#setCharacterStream(java.io.Reader)
	 */
	@Override
	public void setCharacterStream(final Reader characterStream) {
		// Nothing here.
	}

	/**
	 * @see org.w3c.dom.ls.LSInput#setEncoding(java.lang.String)
	 */
	@Override
	public void setEncoding(final String encoding) {
		// Nothing here.
	}

	/**
	 * @see org.w3c.dom.ls.LSInput#setPublicId(java.lang.String)
	 */
	@Override
	public void setPublicId(final String id) {
		// Nothing here.
	}

	/**
	 * @see org.w3c.dom.ls.LSInput#setStringData(java.lang.String)
	 */
	@Override
	public void setStringData(final String stringData) {
		// Nothing here.
	}

	/**
	 * @see org.w3c.dom.ls.LSInput#setSystemId(java.lang.String)
	 */
	@Override
	public void setSystemId(final String id) {
		// Nothing here.
	}
}
