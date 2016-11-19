/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.sftp;

import org.springframework.integration.file.remote.ClientCallback;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

/**
 * The {@link ClientCallback} to use when executing the rm/rmdir command.
 *
 * @author bhausen
 */
public class RemoveClientCallback implements ClientCallback<ChannelSftp, Void> {
	/** The path to remove. */
	private String remotePath;
	/** A possibly thrown {@link SftpException}. */
	private SftpException sftpException;

	/**
	 * Create the {@link RemoveClientCallback}.
	 *
	 * @param remotePath
	 *            the path to remove.
	 */
	public RemoveClientCallback(final String remotePath) {
		this.remotePath = remotePath;
	}

	/**
	 * @see org.springframework.integration.file.remote.ClientCallback#doWithClient(java.lang.Object)
	 */
	@Override
	public Void doWithClient(final ChannelSftp client) {
		try {
			SftpATTRS attrs = client.lstat(this.remotePath);
			if (attrs.isDir()) {
				client.rmdir(this.remotePath);
			} else {
				client.rm(this.remotePath);
			}
		} catch (SftpException e) {
			this.sftpException = e;
		}
		return null;
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
