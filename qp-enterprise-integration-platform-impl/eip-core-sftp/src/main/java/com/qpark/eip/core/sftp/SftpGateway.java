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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
 * The {@link SftpGateway} to up- and download content.
 *
 * @author bhausen
 */
public class SftpGateway {
	/** The done directory name. */
	public static final String DIRECTORY_DONE = "done";

	/** The failed directory name. */
	public static final String DIRECTORY_FAILED = "failed";

	/** The open directory name. */
	public static final String DIRECTORY_OPEN = "open";

	/** The temporary directory name. */
	public static final String DIRECTORY_TEMP = "tmp";

	/** Characters illegal in file names. */
	public static final char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t',
			'\0', '\f', '`', '?', '\\', '<', '>', '|', '\"', ':' };

	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory.getLogger(SftpGateway.class);

	/** The {@link SftpRemoteFileTemplate}. */
	private SftpRemoteFileTemplate template;

	/**
	 * Create the local temporary file and write the content into. This is the
	 * basis to upload the file.
	 *
	 * @param prefix
	 *            prefix of {@link File#createTempFile(String, String)}
	 * @param suffix
	 *            suffix of {@link File#createTempFile(String, String)}
	 * @param content
	 *            the content to write.
	 * @return the temporary {@link File}.
	 * @throws IOException
	 */
	public File createAndWriteTmpFile(final String prefix, final String suffix,
			final byte[] content) throws IOException {
		File f = File.createTempFile(prefix, suffix);
		/* Write file. */
		try (FileOutputStream fos = new FileOutputStream(f, false)) {
			if (!f.exists()) {
				f.createNewFile();
			}
			fos.write(content);
		}
		return f;
	}

	/**
	 * Delete the file or directory with the given path.
	 *
	 * @param filePath
	 *            the path of the file to delete.
	 * @return <code>true</code>, if successfully deleted, else
	 *         <code>false</code>.
	 * @throws Exception
	 */
	public boolean delete(final String filePath) throws Exception {
		boolean success = false;
		RemoveClientCallback callback = new RemoveClientCallback(filePath);
		this.template.executeWithClient(callback);
		if (callback.getSftpException() != null) {
			this.logger.error("rm/rmdir {} {}", filePath,
					callback.getSftpException().getMessage());
			throw callback.getSftpException();
		} else {
			success = true;
		}
		this.logger.debug("delete {} {}", filePath, success);
		return success;
	}

	/**
	 * Tests, if the file or directory with the given path exists.
	 *
	 * @param filePath
	 *            the file path to test.
	 * @return <code>true</code>, if exists, else <code>false</code>.
	 * @throws Exception
	 */
	public boolean exists(final String filePath) throws Exception {
		boolean exists = this.template.exists(filePath);
		return exists;
	}

	/**
	 * Get a {@link File} with the content of the file path.
	 *
	 * @param filePath
	 *            the path of the file.
	 * @return the content.
	 * @throws Exception
	 */
	public File getContent(final String filePath) throws Exception {
		File content = File.createTempFile("EIP-TemporarySftp", ".txt");
		boolean success = false;
		try (FileOutputStream baos = new FileOutputStream(content);) {
			success = this.template.get(filePath,
					new InputStreamCallbackImpl(baos));
		} catch (Exception e) {
			throw e;
		}
		if (!success) {
			throw new IllegalStateException(new StringBuffer(64)
					.append("Could not read ").append(filePath).toString());
		}
		return content;
	}

	/**
	 * Get the content of the file.
	 *
	 * @param filePath
	 *            the path of the file.
	 * @return the content.
	 * @throws Exception
	 */
	public byte[] getContentBytes(final String filePath) throws Exception {
		byte[] content = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		boolean success = this.template.get(filePath,
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
	 * Get the date based target directory name in format
	 * <i>baseDirectory/yyyy/yyyyMMdd</i>
	 *
	 * @param baseDirectory
	 *            the base directory to start the date structure.
	 * @param date
	 *            the date to create the structure.
	 * @return the date based target directory name.
	 */
	public String getDateBasedTargetDirectory(final String baseDirectory,
			final Date date) {
		SimpleDateFormat year = new SimpleDateFormat("yyyy");
		SimpleDateFormat dayInYear = new SimpleDateFormat("yyyyMMdd");
		String value = this.getFilePath(baseDirectory, year.format(date),
				dayInYear.format(date));
		return value;
	}

	/**
	 * Get the file path with the corresponding separator.
	 *
	 * @param strings
	 *            the strings which should be bound together to the file path.
	 * @return the file path.
	 */
	public String getFilePath(final String... strings) {
		String value = "";
		if (Objects.nonNull(strings)) {
			StringBuffer sb = new StringBuffer(512);
			String separator = this.getRemoteFileSeparator();
			Arrays.asList(strings).stream()
					.filter(s -> Objects.nonNull(s) && s.trim().length() > 0)
					.forEach(s -> sb.append(s).append(separator));
			value = sb.toString();
			value = value.substring(0, value.length() - separator.length());
		}
		return value;
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
		List<String> value = new ArrayList<>();
		LsClientCallback callback = new LsClientCallback(directory,
				fileNamePattern);
		Vector<LsEntry> entries = this.template.executeWithClient(callback);
		if (callback.getSftpException() != null) {
			this.logger.error("getLs {} {} {}", directory, fileNamePattern,
					callback.getSftpException().getMessage());
			throw callback.getSftpException();
		} else if (entries != null) {
			for (LsEntry lsEntry : entries) {
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
	 * Get the remote file separator string.
	 *
	 * @return the remote file separator string.
	 */
	public String getRemoteFileSeparator() {
		return this.template.getRemoteFileSeparator();
	}

	/**
	 * Get the final target file path in format
	 * <i>baseDirectory/yyyy/yyyyMMdd/yyyyMMdd-HHmmss.SSS-fileName.fileFormat
	 * </i>.
	 *
	 * @param baseDirectory
	 *            the base directory to start the date structure.
	 * @param date
	 *            the date to create the structure.
	 * @param baseFileName
	 *            the base name of the file
	 * @param fileFormat
	 *            the format of the file like json, xml, txt ...
	 * @return the target file path.
	 */
	public String getTargetFilePath(final String baseDirectory, final Date date,
			final String baseFileName, final String fileFormat) {
		SimpleDateFormat timestamp = new SimpleDateFormat(
				"yyyyMMdd-HHmmss.SSS");
		String fileName = String.format("%s-%s.%s", timestamp.format(date),
				baseFileName, fileFormat);
		String value = this.getFilePath(
				this.getDateBasedTargetDirectory(baseDirectory, date),
				fileName);
		return value;
	}

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
		LsClientCallback callback = new LsClientCallback(remotePath, null);
		Vector<LsEntry> entries = this.template.executeWithClient(callback);
		if (callback.getSftpException() != null) {
			this.logger.error("ls {} {}", remotePath,
					callback.getSftpException().getMessage());
			throw callback.getSftpException();
		} else if (Objects.nonNull(entries)) {
			List<LsEntry> content = entries.stream()
					.filter(lsEntry -> !lsEntry.getFilename().equals(".")
							&& !lsEntry.getFilename().equals(".."))
					.collect(Collectors.toList());
			if (content.isEmpty()) {
				emptyDirectories.add(remotePath);
				this.logger.trace("getTreeOfEmptyDirectories {} {} added {}",
						parent, currentDirectory, remotePath);
			} else {
				String rPath = remotePath;
				content.stream().filter(lsEntry -> lsEntry.getAttrs().isDir())
						.forEach(lsEntry -> {
							try {
								this.getTreeOfEmptyDirectories(emptyDirectories,
										rPath, lsEntry.getFilename());
							} catch (Exception e) {
							}
						});
			}
		}
	}

	/**
	 * Get the tree of all empty directories under the base directory.
	 *
	 * @param baseDirectory
	 *            the base directory.
	 * @return the list off all empty directories.
	 * @throws Exception
	 */
	public List<String> getTreeOfEmptyDirectories(final String baseDirectory)
			throws Exception {
		List<String> value = new ArrayList<>();
		this.getTreeOfEmptyDirectories(value, baseDirectory, null);
		return value;
	}

	private void getTreeOfFiles(final List<String> filePathes,
			final String parent, final String currentDirectory,
			final String fileNamePattern) throws Exception {
		String remotePath = parent;
		if (currentDirectory != null) {
			remotePath = String.format("%s%s%s", parent,
					this.getRemoteFileSeparator(), currentDirectory);
		}
		this.logger.trace("getTreeOfFiles {} {}", parent, currentDirectory);
		LsClientCallback callback = new LsClientCallback(remotePath, null);
		Vector<LsEntry> entries = this.template.executeWithClient(callback);
		if (callback.getSftpException() != null) {
			this.logger.error("ls {} {}", remotePath,
					callback.getSftpException().getMessage());
			throw callback.getSftpException();
		} else if (Objects.nonNull(entries)) {
			String rPath = remotePath;
			entries.stream()
					.filter(lsEntry -> !lsEntry.getFilename().equals(".")
							&& !lsEntry.getFilename().equals(".."))
					.forEach(lsEntry -> {
						if (lsEntry.getAttrs().isDir()) {
							try {
								this.getTreeOfFiles(filePathes, rPath,
										lsEntry.getFilename(), fileNamePattern);
							} catch (Exception e) {
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
	 * Get the tree of all files - not directories - matching the file name
	 * pattern under the base directory.
	 *
	 * @param baseDirectory
	 *            the base directory.
	 * @param fileNamePattern
	 *            the pattern the file names need to apply.
	 * @return the list of file names.
	 * @throws Exception
	 */
	public List<String> getTreeOfFiles(final String baseDirectory,
			final String fileNamePattern) throws Exception {
		List<String> value = new ArrayList<>();
		this.getTreeOfFiles(value, baseDirectory, null, fileNamePattern);
		return value;
	}

	/**
	 * Create a new directory with the given name.
	 *
	 * @param directory
	 *            the name of the directory to create (could start with /).
	 * @return <code>true</code>, if successfully created, else
	 *         <code>false</code>.
	 * @throws Exception
	 */
	public boolean mkdir(final String directory) throws Exception {
		boolean success = false;
		MkdirClientCallback callback = new MkdirClientCallback(directory);
		this.template.executeWithClient(callback);
		if (callback.getSftpException() != null) {
			this.logger.error("mkdir {} {}", directory,
					callback.getSftpException().getMessage());
			throw callback.getSftpException();
		} else {
			success = true;
		}
		this.logger.debug("mkdir {} {}", directory, success);
		return success;
	}

	/**
	 * Rename the file or directory with the given path.
	 *
	 * @param fromFilePath
	 *            current path name.
	 * @param toFilePath
	 *            target path name.
	 * @throws Exception
	 */
	public void rename(final String fromFilePath, final String toFilePath)
			throws Exception {
		this.logger.debug("rename {} -> {}", fromFilePath, toFilePath);
		this.template.rename(fromFilePath, toFilePath);
	}

	/**
	 * Retrieve the file and provide the content as a byte array.
	 *
	 * @param filePath
	 *            the file to retrieve.
	 * @return the content of the file.
	 * @throws Exception
	 */
	public byte[] retrieveAndRead(final String filePath) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		File f = this.getContent(filePath);
		if (Objects.nonNull(f)) {
			try (FileInputStream fis = new FileInputStream(f)) {
				int readBufferLenght = 0;
				byte[] buffer = new byte[2048];
				while ((readBufferLenght = fis.read(buffer)) > 0) {
					baos.write(buffer, 0, readBufferLenght);
				}
			}
		}
		return baos.toByteArray();
	}

	/**
	 * Save the file in the remote directory.
	 *
	 * @param file
	 *            the file to save.
	 * @param remoteDirectory
	 *            the remote directory name to store the file.
	 * @return <code>true</code>, if successfully stored, else
	 *         <code>false</code>.
	 * @throws Exception
	 */
	public boolean save(final File file, final String remoteDirectory)
			throws Exception {
		boolean success = false;
		Map<String, Object> headers = new HashMap<>();
		headers.put("directory", "");
		Message<File> message = MessageBuilder.createMessage(file,
				new MessageHeaders(headers));
		if (!this.template.exists(remoteDirectory)) {
			this.mkdir(remoteDirectory);
		}
		this.logger.debug("save {} {}", remoteDirectory, file.getName());
		String remotePath = this.template.send(message, remoteDirectory,
				FileExistsMode.REPLACE);
		if (remotePath != null) {
			success = true;
		}
		return success;
	}

	/**
	 * Set the {@link SftpRemoteFileTemplate}.
	 *
	 * @param template
	 *            the {@link SftpRemoteFileTemplate}.
	 */
	public void setTemplate(final SftpRemoteFileTemplate template) {
		this.template = template;
	}

	/**
	 * Validates the file name and returns the valid one.
	 *
	 * @param fileName
	 *            the file name.
	 * @return the valid file name.
	 */
	public String validateFileName(final String fileName) {
		String value = fileName;
		if (Objects.nonNull(fileName)) {
			value = value.replace(' ', '_');
			for (Character c : ILLEGAL_CHARACTERS) {
				value = value.replace(c, '*');
			}
			value = value.replaceAll("\\*", "");
		}
		return value;
	}
}
