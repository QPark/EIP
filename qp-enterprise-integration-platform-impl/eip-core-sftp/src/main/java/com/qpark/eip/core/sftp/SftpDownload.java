/*******************************************************************************
 * Copyright (c) 2013, 2014, 2015 QPark Consulting  S.a r.l.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 ******************************************************************************/
package com.qpark.eip.core.sftp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author bhausen
 */
public class SftpDownload {
	/** The {@link org.slf4j.Logger}. */
	private final Logger logger = LoggerFactory.getLogger(SftpDownload.class);
	/** The {@link SftpGateway}. */
	@Autowired
	private SftpGateway sftpGateway;

	/**
	 * Get the {@link Optional} of the file content. This could be empty,
	 * because possibly another thread already read the content of the file path
	 * and removed it.
	 *
	 * @param filePath
	 *            the file path.
	 * @return the {@link Optional} of the file content.
	 */
	private Optional<byte[]> getFilePathContent(final String filePath) {
		Optional<byte[]> value = Optional.empty();
		try {
			final byte[] fileContent = this.sftpGateway
					.retrieveAndRead(filePath);
			value = Optional.of(fileContent);
		} catch (final Exception e) {
			// The file was eventually read by another thread.
		}
		return value;
	}

	/**
	 * Random object used by random method. This has to be not local to the
	 * random method so as to not return the same value in the same millisecond.
	 */
	private static final Random RANDOM = new Random();

	private static int nextRandomInt(final int startInclusive,
			final int endExclusive) {
		return startInclusive + RANDOM.nextInt(endExclusive - startInclusive);
	}

	/**
	 * Get the file content, parse it and move the file to failed or done
	 * directory.
	 *
	 * @param typeFactory
	 *            the factory creating a list of T.
	 * @param baseDir
	 *            base source directory to get the files from (e.g. incoming)
	 * @param filePath
	 *            the file path.
	 * @return the Optional of the parsed object of type.
	 */
	public <T> List<T> getObject(final TypeFactory<T> typeFactory,
			final String baseDir, final String filePath) {
		this.logger.debug("+getObject {}", filePath);
		List<T> value = new ArrayList<>();
		String filePathDestination;
		final String filePathLoading = String.format("%s.%03d.loading",
				filePath, nextRandomInt(0, 999));
		try {
			filePathDestination = filePath.replace(SftpGateway.DIRECTORY_OPEN,
					SftpGateway.DIRECTORY_DONE);

			this.sftpGateway.rename(filePath, filePathLoading);
			final Optional<byte[]> filePathContent = this
					.getFilePathContent(filePathLoading);

			try {
				value = typeFactory.create(filePathContent);
			} catch (final Exception e) {
				this.logger.error(e.getMessage(), e);
				filePathDestination = filePath.replace(
						SftpGateway.DIRECTORY_OPEN,
						SftpGateway.DIRECTORY_FAILED);
			}
			try {
				this.sftpGateway.rename(filePathLoading, filePathDestination);
			} catch (final Exception e) {
				this.logger.error(String.format("%s:%s", filePathDestination,
						e.getMessage()), e);
			}
		} catch (final Exception e) {
			filePathDestination = filePath.replace(SftpGateway.DIRECTORY_OPEN,
					SftpGateway.DIRECTORY_FAILED);
			try {
				this.sftpGateway.rename(filePathLoading, filePathDestination);
			} catch (final Exception ex) {
				this.logger.error(String.format("%s:%s", filePathDestination,
						e.getMessage()), e);
			}
			this.logger.error(String.format("%s:%s", filePath, e.getMessage()),
					e);
		} finally {
			this.logger.debug("-getObject {}", filePath);
		}
		return value;
	}

	/**
	 * Get the list of Objects from the SFTP.
	 *
	 * @param typeFactory
	 *            the factory creating a list of T.
	 * @param baseDir
	 *            base source directory to get the files from (e.g. incoming)
	 * @param fileNamePart
	 *            the part, the file name need to contain (e.g. logs).
	 * @param fileFormat
	 *            the file format (e.g. xml, txt, json ...)
	 * @return the list of Objects.
	 */
	public <T> List<T> getObjects(final TypeFactory<T> typeFactory,
			final String baseDir, final String fileNamePart,
			final String fileFormat) {
		this.logger.debug("+getObjects");
		final List<T> value = new ArrayList<>();
		final String sourceDir = this.sftpGateway.getFilePath(baseDir,
				SftpGateway.DIRECTORY_OPEN);
		try {
			final Collection<String> filePathes = this.sftpGateway
					.getTreeOfFiles(sourceDir, String.format("20.*?%s.%s",
							fileNamePart, fileFormat));
			filePathes.stream().sorted().forEach(filePath -> value
					.addAll(this.getObject(typeFactory, baseDir, filePath)));
		} catch (final Exception e) {
			this.logger.error(String.format("%s:%s", sourceDir, e.getMessage()),
					e);
		} finally {
			this.logger.debug("-getObjects {} #{}", sourceDir, value.size());
		}
		return value;
	}
}
