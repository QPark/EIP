/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.sftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.integration.file.remote.InputStreamCallback;
import org.springframework.util.FileCopyUtils;

/**
 * The {@link InputStreamCallback} copying the content to the
 * {@link OutputStream}.
 *
 * @author bhausen
 */
public class InputStreamCallbackImpl implements InputStreamCallback {
	/** The {@link OutputStream}. */
	private final OutputStream os;

	/**
	 * The {@link InputStreamCallback} copying the content to the
	 * {@link OutputStream}
	 *
	 * @param os
	 *            the {@link OutputStream}.
	 */
	public InputStreamCallbackImpl(final OutputStream os) {
		this.os = os;
	}

	/**
	 * @see org.springframework.integration.file.remote.InputStreamCallback#doWithInputStream(java.io.InputStream)
	 */
	@Override
	public void doWithInputStream(final InputStream stream) throws IOException {
		FileCopyUtils.copy(stream, this.os);
	}
}
