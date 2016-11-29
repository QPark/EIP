package com.qpark.eip.core.sftp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;

/**
 * @author bhausen
 */
public interface SftpGateway {
	/** The done directory name. */
	String DIRECTORY_DONE = "done";
	/** The failed directory name. */
	String DIRECTORY_FAILED = "failed";
	/** The open directory name. */
	String DIRECTORY_OPEN = "open";
	/** The temporary directory name. */
	String DIRECTORY_TEMP = "tmp";
	/** Characters illegal in file names. */
	char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f', '`', '?',
			'\\', '<', '>', '|', '\"', ':' };

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
	default File createAndWriteTmpFile(final String prefix, final String suffix,
			final byte[] content) throws IOException {
		final File f = File.createTempFile(prefix, suffix);
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
	boolean delete(String filePath) throws Exception;

	/**
	 * Tests, if the file or directory with the given path exists.
	 *
	 * @param filePath
	 *            the file path to test.
	 * @return <code>true</code>, if exists, else <code>false</code>.
	 * @throws Exception
	 */
	boolean exists(String filePath) throws Exception;

	/**
	 * Get a {@link File} with the content of the file path.
	 *
	 * @param filePath
	 *            the path of the file.
	 * @return the content.
	 * @throws Exception
	 */
	File getContent(String filePath) throws Exception;

	/**
	 * Get the content of the file.
	 *
	 * @param filePath
	 *            the path of the file.
	 * @return the content.
	 * @throws Exception
	 */
	byte[] getContentBytes(String filePath) throws Exception;

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
	default String getDateBasedTargetDirectory(final String baseDirectory,
			final Date date) {
		final SimpleDateFormat year = new SimpleDateFormat("yyyy");
		final SimpleDateFormat dayInYear = new SimpleDateFormat("yyyyMMdd");
		final String value = this.getFilePath(baseDirectory, year.format(date),
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
	default String getFilePath(final String... strings) {
		String value = "";
		if (Objects.nonNull(strings)) {
			final StringBuffer sb = new StringBuffer(512);
			final String separator = this.getRemoteFileSeparator();
			Arrays.asList(strings).stream()
					.filter(s -> Objects.nonNull(s) && s.trim().length() > 0)
					.forEach(s -> sb.append(s).append(separator));
			value = sb.toString();
			value = value.substring(0, value.length() - separator.length());
		}
		return value;
	}

	/**
	 * Get the remote file separator string.
	 *
	 * @return the remote file separator string.
	 */
	String getRemoteFileSeparator();

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
	default String getTargetFilePath(final String baseDirectory,
			final Date date, final String baseFileName,
			final String fileFormat) {
		final SimpleDateFormat timestamp = new SimpleDateFormat(
				"yyyyMMdd-HHmmss.SSS");
		final String fileName = String.format("%s-%s.%s",
				timestamp.format(date), baseFileName, fileFormat);
		final String value = this.getFilePath(
				this.getDateBasedTargetDirectory(baseDirectory, date),
				fileName);
		return value;
	}

	/**
	 * Get the tree of all empty directories under the base directory.
	 *
	 * @param baseDirectory
	 *            the base directory.
	 * @return the list off all empty directories.
	 * @throws Exception
	 */
	List<String> getTreeOfEmptyDirectories(String baseDirectory)
			throws Exception;

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
	List<String> getTreeOfFiles(String baseDirectory, String fileNamePattern)
			throws Exception;

	/**
	 * Create a new directory with the given name.
	 *
	 * @param directory
	 *            the name of the directory to create (could start with /).
	 * @return <code>true</code>, if successfully created, else
	 *         <code>false</code>.
	 * @throws Exception
	 */
	boolean mkdir(String directory) throws Exception;

	/**
	 * Removes the empty directories from the baseDirectory.
	 *
	 * @param baseDirectory
	 *            the base directory to delete empty directories from.
	 * @throw Exception
	 */
	default void removeEmptyDirectories(final String baseDirectory)
			throws Exception {
		getTreeOfEmptyDirectories(baseDirectory).stream()
				.sorted(Comparator.reverseOrder()).forEach(s -> {
					try {
						delete(s);
					} catch (final Exception e) {
						e.printStackTrace();
					}
				});

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
	void rename(String fromFilePath, String toFilePath) throws Exception;

	/**
	 * Retrieve the file and provide the content as a byte array.
	 *
	 * @param filePath
	 *            the file to retrieve.
	 * @return the content of the file.
	 * @throws Exception
	 */
	default byte[] retrieveAndRead(final String filePath) throws Exception {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final File f = this.getContent(filePath);
		if (Objects.nonNull(f)) {
			try (FileInputStream fis = new FileInputStream(f)) {
				int readBufferLenght = 0;
				final byte[] buffer = new byte[2048];
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
	boolean save(File file, String remoteDirectory) throws Exception;

	/**
	 * Set the {@link SftpRemoteFileTemplate}.
	 *
	 * @param template
	 *            the {@link SftpRemoteFileTemplate}.
	 */
	void setTemplate(SftpRemoteFileTemplate template);

	/**
	 * Validates the file name and returns the valid one.
	 *
	 * @param fileName
	 *            the file name.
	 * @return the valid file name.
	 */
	default String validateFileName(final String fileName) {
		String value = fileName;
		if (Objects.nonNull(fileName)) {
			value = value.replace(' ', '_');
			for (final Character c : ILLEGAL_CHARACTERS) {
				value = value.replace(c, '*');
			}
			value = value.replaceAll("\\*", "");
		}
		return value;
	}
}