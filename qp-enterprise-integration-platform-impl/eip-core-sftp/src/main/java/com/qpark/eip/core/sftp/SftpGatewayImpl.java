/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.sftp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import com.jcraft.jsch.ChannelSftp.LsEntry;

/**
 * The {@link SftpGatewayImpl} to up- and download content.
 *
 * @author bhausen
 */
public class SftpGatewayImpl implements SftpGateway {
	/** The {@link org.slf4j.Logger}. */
	private final Logger logger = LoggerFactory
			.getLogger(SftpGatewayImpl.class);

	/** The {@link SftpRemoteFileTemplate}. */
	private SftpRemoteFileTemplate template;

	/**
	 * @see com.qpark.eip.core.sftp.SftpGateway#delete(java.lang.String)
	 */
	@Override
	public boolean delete(final String filePath) throws Exception {
		boolean success = false;
		final RemoveClientCallback callback = new RemoveClientCallback(
				filePath);
		this.template.executeWithClient(callback);
		if (callback.getSftpException() != null) {
			this.logger.error("rm/rmdir {} {}", filePath,
					callback.getSftpException().getMessage());
			throw callback.getSftpException();
		}
		success = true;
		this.logger.debug("delete {} {}", filePath, success);
		return success;
	}

	/**
	 * @see com.qpark.eip.core.sftp.SftpGateway#exists(java.lang.String)
	 */
	@Override
	public boolean exists(final String filePath) throws Exception {
		final boolean exists = this.template.exists(filePath);
		return exists;
	}

	/**
	 * @deprecated Returns a temporary file which needs to be deleted
	 *             afterwards. Use {@link #getContentBytes(String)} instead.
	 * @see com.qpark.eip.core.sftp.SftpGateway#getContent(java.lang.String)
	 */
	@Override
	@Deprecated
	public File getContent(final String filePath) throws Exception {
		final File value = File.createTempFile("EIP-TemporarySftp", ".txt");
		boolean success = false;
		try (FileOutputStream baos = new FileOutputStream(value);) {
			success = this.template.get(filePath,
					new InputStreamCallbackImpl(baos));
		} catch (final Exception e) {
			throw e;
		}
		if (!success) {
			throw new IllegalStateException(new StringBuffer(64)
					.append("Could not read ").append(filePath).toString());
		}
		return value;
	}

	/**
	 * @see com.qpark.eip.core.sftp.SftpGateway#getContentBytes(java.lang.String)
	 */
	@Override
	public byte[] getContentBytes(final String filePath) throws Exception {
		byte[] content = null;
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final boolean success = this.template.get(filePath,
				new InputStreamCallbackImpl(baos));
		if (success) {
			content = baos.toByteArray();
		} else {
			throw new IllegalStateException(new StringBuffer(64)
					.append("Could not read ").append(filePath).toString());
		}
		return content;
	}

	/**
	 * Get the file/directory listing according to the parameters.
	 *
	 * @param directory
	 *            the directory name to look at.
	 * @param fileNamePattern
	 *            the file name pattern the files need to apply (if any).
	 * @param directories
	 *            only directories should be taken into account.
	 * @return the list of file/directory names.
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private List<String> getLs(final String directory,
			final String fileNamePattern, final boolean directories)
			throws Exception {
		final List<String> value = new ArrayList<>();
		final LsClientCallback callback = new LsClientCallback(directory,
				fileNamePattern);
		final Vector<LsEntry> entries = this.template
				.executeWithClient(callback);
		if (callback.getSftpException() != null) {
			this.logger.error("getLs {} {} {}", directory, fileNamePattern,
					callback.getSftpException().getMessage());
			throw callback.getSftpException();
		} else if (entries != null) {
			for (final LsEntry lsEntry : entries) {
				if (directories && lsEntry.getAttrs().isDir()) {
					value.add(lsEntry.getFilename());
				} else {
					value.add(lsEntry.getFilename());
				}
			}
		}
		return value;
	}

	/**
	 * @see com.qpark.eip.core.sftp.SftpGateway#getRemoteFileSeparator()
	 */
	@Override
	public String getRemoteFileSeparator() {
		return this.template.getRemoteFileSeparator();
	}

	/**
	 * @param emptyDirectories
	 * @param parent
	 * @param currentDirectory
	 * @throws Exception
	 */
	private void getTreeOfEmptyDirectories(final List<String> emptyDirectories,
			final String parent, final String currentDirectory)
			throws Exception {
		String remotePath = parent;
		if (Objects.nonNull(currentDirectory)) {
			remotePath = String.format("%s%s%s", parent,
					this.getRemoteFileSeparator(), currentDirectory);
		}
		this.logger.trace("getTreeOfEmptyDirectories {} {}", parent,
				currentDirectory);
		final LsClientCallback callback = new LsClientCallback(remotePath,
				null);
		final Vector<LsEntry> entries = this.template
				.executeWithClient(callback);
		if (callback.getSftpException() != null) {
			this.logger.error("ls {} {}", remotePath,
					callback.getSftpException().getMessage());
			throw callback.getSftpException();
		} else if (Objects.nonNull(entries)) {
			final List<LsEntry> content = entries.stream()
					.filter(lsEntry -> !lsEntry.getFilename().equals(".")
							&& !lsEntry.getFilename().equals(".."))
					.collect(Collectors.toList());
			if (content.isEmpty()) {
				emptyDirectories.add(remotePath);
				this.logger.trace("getTreeOfEmptyDirectories {} {} added {}",
						parent, currentDirectory, remotePath);
			} else {
				final String rPath = remotePath;
				content.stream().filter(lsEntry -> lsEntry.getAttrs().isDir())
						.forEach(lsEntry -> {
							try {
								this.getTreeOfEmptyDirectories(emptyDirectories,
										rPath, lsEntry.getFilename());
							} catch (final Exception e) {
								// Nothing.
							}
						});
			}
		}
	}

	/**
	 * @see com.qpark.eip.core.sftp.SftpGateway#getTreeOfEmptyDirectories(java.lang.String)
	 */
	@Override
	public List<String> getTreeOfEmptyDirectories(final String baseDirectory)
			throws Exception {
		final List<String> value = new ArrayList<>();
		this.getTreeOfEmptyDirectories(value, baseDirectory, null);
		return value;
	}

	/**
	 * @param filePathes
	 * @param parent
	 * @param currentDirectory
	 * @param fileNamePattern
	 * @throws Exception
	 */
	private void getTreeOfFiles(final List<String> filePathes,
			final String parent, final String currentDirectory,
			final String fileNamePattern) throws Exception {
		String remotePath = parent;
		if (currentDirectory != null) {
			remotePath = String.format("%s%s%s", parent,
					this.getRemoteFileSeparator(), currentDirectory);
		}
		this.logger.trace("getTreeOfFiles {} {}", parent, currentDirectory);
		final LsClientCallback callback = new LsClientCallback(remotePath,
				null);
		final Vector<LsEntry> entries = this.template
				.executeWithClient(callback);
		if (callback.getSftpException() != null) {
			this.logger.error("ls {} {}", remotePath,
					callback.getSftpException().getMessage());
			throw callback.getSftpException();
		} else if (Objects.nonNull(entries)) {
			final String rPath = remotePath;
			entries.stream()
					.filter(lsEntry -> !lsEntry.getFilename().equals(".")
							&& !lsEntry.getFilename().equals(".."))
					.forEach(lsEntry -> {
						if (lsEntry.getAttrs().isDir()) {
							try {
								this.getTreeOfFiles(filePathes, rPath,
										lsEntry.getFilename(), fileNamePattern);
							} catch (final Exception e) {
								// Nothing.
							}
						} else if (lsEntry.getFilename()
								.matches(fileNamePattern)) {
							filePathes.add(new StringBuffer().append(rPath)
									.append(this.template
											.getRemoteFileSeparator())
									.append(lsEntry.getFilename()).toString());
							this.logger.trace("getTreeOfFiles {} {} added {}",
									parent, currentDirectory,
									lsEntry.getFilename());
						}
					});
		}
	}

	/**
	 * @see com.qpark.eip.core.sftp.SftpGateway#getTreeOfFiles(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<String> getTreeOfFiles(final String baseDirectory,
			final String fileNamePattern) throws Exception {
		final List<String> value = new ArrayList<>();
		this.getTreeOfFiles(value, baseDirectory, null, fileNamePattern);
		return value;
	}

	/**
	 * @see com.qpark.eip.core.sftp.SftpGateway#mkdir(java.lang.String)
	 */
	@Override
	public boolean mkdir(final String directory) throws Exception {
		boolean success = false;
		final MkdirClientCallback callback = new MkdirClientCallback(directory);
		this.template.executeWithClient(callback);
		if (callback.getSftpException() != null) {
			this.logger.error("mkdir {} {}", directory,
					callback.getSftpException().getMessage());
			throw callback.getSftpException();
		}
		success = true;
		this.logger.debug("mkdir {} {}", directory, success);
		return success;
	}

	/**
	 * @see com.qpark.eip.core.sftp.SftpGateway#rename(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void rename(final String fromFilePath, final String toFilePath)
			throws Exception {
		this.logger.debug("rename {} -> {}", fromFilePath, toFilePath);
		this.template.rename(fromFilePath, toFilePath);
	}

	/**
	 * @see com.qpark.eip.core.sftp.SftpGateway#save(java.io.File,
	 *      java.lang.String)
	 */
	@Override
	public boolean save(final File file, final String remoteDirectory)
			throws Exception {
		boolean success = false;
		final Map<String, Object> headers = new HashMap<>();
		headers.put("directory", "");
		final Message<File> message = MessageBuilder.createMessage(file,
				new MessageHeaders(headers));
		if (!this.template.exists(remoteDirectory)) {
			this.mkdir(remoteDirectory);
		}
		this.logger.debug("save {} {}", remoteDirectory, file.getName());
		final String remotePath = this.template.send(message, remoteDirectory,
				FileExistsMode.REPLACE);
		if (remotePath != null) {
			success = true;
		}
		return success;
	}

	/**
	 * @see com.qpark.eip.core.sftp.SftpGateway#setTemplate(org.springframework.integration.sftp.session.SftpRemoteFileTemplate)
	 */
	@Override
	public void setTemplate(final SftpRemoteFileTemplate template) {
		this.template = template;
	}
}
