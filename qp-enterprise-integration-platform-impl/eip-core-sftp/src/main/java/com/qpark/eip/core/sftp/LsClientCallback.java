/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.sftp;

import java.util.Objects;
import java.util.Vector;

import org.springframework.integration.file.remote.ClientCallback;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpException;

/**
 * The {@link ClientCallback} to use when executing the ls command.
 *
 * @author bhausen
 */
public class LsClientCallback
		implements ClientCallback<ChannelSftp, Vector<LsEntry>> {
	/** The file pattern to use when executing the ls command. */
	@SuppressWarnings("unused")
	private String lsPattern;
	/** The path to use when executing the ls command. */
	private String path;
	/** A possibly thrown {@link SftpException}. */
	private SftpException sftpException;

	/**
	 * Create the {@link LsClientCallback}. The {@link #lsPattern} will default
	 * to <i>*</i>
	 *
	 * @param path
	 *            the path to use when executing the ls command.
	 */
	public LsClientCallback(final String path) {
		this(path, null);
	}

	/**
	 * Create the {@link LsClientCallback}.
	 *
	 * @param path
	 *            the path to use when executing the ls command.
	 * @param lsPattern
	 *            the file pattern to use when executing the ls command.
	 */
	public LsClientCallback(final String path, final String lsPattern) {
		this.path = path;
		this.lsPattern = Objects.isNull(lsPattern)
				|| lsPattern.trim().length() == 0 ? "*" : lsPattern.trim();
	}

	/**
	 * @see org.springframework.integration.file.remote.ClientCallback#doWithClient(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<LsEntry> doWithClient(final ChannelSftp client) {
		Vector<LsEntry> content = null;
		try {
			content = client.ls(this.path);
		} catch (SftpException e) {
			this.sftpException = e;
		}
		return content;
	}

	/**
	 * Get the possibly thrown {@link SftpException}.
	 *
	 * @return the possibly thrown {@link SftpException} or <code>null</code>.
	 */
	public SftpException getSftpException() {
		return this.sftpException;
	}
}
