/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.sftp;

import java.io.File;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author bhausen
 */
public class SftpUpload {
	/** The {@link org.slf4j.Logger}. */
	private Logger logger = LoggerFactory.getLogger(SftpUpload.class);
	/** The {@link SftpGateway}. */
	@Autowired
	private SftpGateway sftpGateway;

	/**
	 * Upload the content to SFTP.
	 *
	 * @param baseDir
	 *            base source directory to get the files from (e.g. incoming)
	 * @param timestamp
	 *            the time stamp for the file name.
	 * @param fileNamePart
	 *            the part, the file name need to contain (e.g. xyz-logfile ).
	 * @param fileFormat
	 *            the file format (e.g. xml, txt, json ...)
	 * @param content
	 *            the content to upload.
	 */
	public void putContent(final String baseDir, final Date timestamp,
			final String fileNamePart, final String fileFormat,
			final byte[] content) {
		this.logger.debug("+putContent");
		String sourceDir = this.sftpGateway.getFilePath(baseDir,
				SftpGateway.DIRECTORY_TEMP);
		String destinationDir = this.sftpGateway.getFilePath(baseDir,
				SftpGateway.DIRECTORY_OPEN);
		String destinationFilePath = this.sftpGateway.getTargetFilePath(
				destinationDir, timestamp, fileNamePart, fileFormat);
		try {

			/* Upload file in SFTP. */
			File f = this.sftpGateway.createAndWriteTmpFile(fileNamePart,
					fileFormat, content);
			this.sftpGateway.save(f, sourceDir);

			/* Rename file in SFTP. */
			String sourceFilePath = this.sftpGateway.getFilePath(sourceDir,
					f.getName());
			this.sftpGateway.rename(sourceFilePath, destinationFilePath);
		} catch (Exception e) {
			this.logger.error(" putContent {} {}", destinationFilePath,
					e.getMessage());
			this.logger.error(e.getMessage(), e);
		} finally {
			this.logger.debug("+putContent {}", destinationFilePath);
		}
	}
}
