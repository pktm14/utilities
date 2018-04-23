package edu.scripps.yates.utilities.files;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

public class ZipManager {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");
	private static final int BUFFER_SIZE = 8192;

	/**
	 * Compress a file in a gzip file
	 * 
	 * @throws IOException
	 */

	public static File compressGZipFile(File inputFile) throws IOException {

		return ZipManager.compressGZipFile(inputFile, null);

	}

	public static File compressZipFile(File inputFile) throws IOException {

		return ZipManager.compressZipFile(inputFile, null);

	}

	public static File compressGZipFile(File inputFile, File outputFile) throws IOException {
		// if it is already compressed, return the input file
		if (FilenameUtils.getExtension(inputFile.getName()).equalsIgnoreCase("gz"))
			return inputFile;

		if (outputFile == null)
			outputFile = new File(inputFile.getAbsolutePath() + ".gz");

		// Create the GZIP file
		final BufferedOutputStream bufferedOut = new BufferedOutputStream(
				new GZIPOutputStream(new FileOutputStream(outputFile)));
		final BufferedInputStream bufferedIn = new BufferedInputStream(new FileInputStream(inputFile));

		copyInputStream(bufferedIn, bufferedOut);

		return outputFile;

	}

	public static File compressZipFile(File inputFile, File outputFile) throws IOException {
		// if it is already compressed, return the input file
		if (FilenameUtils.getExtension(inputFile.getName()).equalsIgnoreCase("gz"))
			return inputFile;

		if (outputFile == null)
			outputFile = new File(inputFile.getAbsolutePath() + ".gz");

		// Create the GZIP file
		final BufferedOutputStream bufferedOut = new BufferedOutputStream(
				new ZipOutputStream(new FileOutputStream(outputFile)));
		final BufferedInputStream bufferedIn = new BufferedInputStream(new FileInputStream(inputFile));

		copyInputStream(bufferedIn, bufferedOut);

		return outputFile;

	}

	/**
	 * Decompress the FIRST entry of a zip file in the same folder as the zip,
	 * or in a temp file if there is some problem writting in that folder
	 * 
	 * @param file
	 * @return the first entry of the zipped file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */

	public static File decompressGZipFile(File file) throws FileNotFoundException, IOException {
		log.debug("processing GZip file: " + file.getAbsolutePath());

		final FileInputStream fin = new FileInputStream(file);
		final BufferedInputStream bufferedIn = new BufferedInputStream(new GZIPInputStream(fin));

		String outputName = FilenameUtils.getFullPath(file.getAbsolutePath())
				+ FilenameUtils.getBaseName(file.getAbsolutePath());
		if (outputName.equals(file.getAbsolutePath()))
			outputName = outputName + "_";
		final BufferedOutputStream bufferedOut = new BufferedOutputStream(new FileOutputStream(outputName));
		log.info("Decompressing to : " + outputName);

		copyInputStream(bufferedIn, bufferedOut);

		return new File(outputName);

	}

	public static File decompressZipFile(File file) throws FileNotFoundException, IOException {
		log.debug("processing Zip file: " + file.getAbsolutePath());
		ZipFile zip = null;
		try {
			zip = new ZipFile(file);
			for (final Enumeration<? extends ZipEntry> e = zip.entries(); e.hasMoreElements();) {
				final ZipEntry entry = e.nextElement();
				if (!entry.isDirectory()) {
					final InputStream inputStream = zip.getInputStream(entry);
					final BufferedInputStream bufferedIn = new BufferedInputStream(inputStream);

					String outputName = FilenameUtils.getFullPath(file.getAbsolutePath())
							+ FilenameUtils.getBaseName(file.getAbsolutePath());
					if (outputName.equals(file.getAbsolutePath()))
						outputName = outputName + "_";
					final BufferedOutputStream bufferedOut = new BufferedOutputStream(new FileOutputStream(outputName));
					log.debug("Decompressing to : " + outputName);

					copyInputStream(bufferedIn, bufferedOut);
					return new File(outputName);
				}
			}
			throw new IOException("Error decompressing zip file");
		} finally {
			if (zip != null) {
				zip.close();
			}
		}
	}

	public static final void copyInputStream(BufferedInputStream bufferedIn, BufferedOutputStream bufferedOut)
			throws IOException {
		int totalAmountRead = 0;
		final byte[] buffer = new byte[BUFFER_SIZE];
		try {

			while (true) {
				final int amountRead = bufferedIn.read(buffer);
				if (amountRead == -1) {
					break;
				}
				totalAmountRead += amountRead;
				bufferedOut.write(buffer, 0, amountRead);
			}
		} finally {
			// CLose streams
			if (bufferedIn != null)
				bufferedIn.close();
			if (bufferedOut != null)
				bufferedOut.close();
			if (totalAmountRead == 0) {
				throw new IOException("Error copying files between streams");
			}

		}
	}

	public static File decompressGZipFileIfNeccessary(File file) {
		return decompressGZipFileIfNeccessary(file, false, false);
	}

	public static File decompressFileIfNeccessary(File file) {
		return decompressFileIfNeccessary(file, false);
	}

	public static File decompressFileIfNeccessary(File file, boolean ignoreExtension) {
		return decompressFileIfNeccessary(file, ignoreExtension, false);
	}

	private static boolean isCompressedFileExtension(String extension) {
		// the most common
		if ("xml".equals(extension) || "txt".equals(extension)) {
			return false;
		}
		if ("gz".equals(extension) || "tar.gz".equals(extension) || "rar".equals(extension) || "zip".equals(extension)
				|| "gzip".equals(extension)) {
			return true;
		}
		return false;
	}

	public static File decompressZipFileIfNeccessary(File file, boolean ignoreExtension) {
		log.info("Decompressing file " + file.getAbsolutePath());

		try {
			if (!ignoreExtension && !isCompressedFileExtension(FilenameUtils.getExtension(file.getAbsolutePath()))) {
				return file;
			}
			return ZipManager.decompressZipFile(file);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			log.info("The file " + file.getAbsolutePath() + " has not found");
		} catch (final IOException e) {
			log.info("The file " + file.getAbsolutePath() + " is not a zipped file. Returning the original file");
		}

		return file;
	}

	/**
	 * 
	 * @param file
	 * @param deleteOnExit
	 *            indicates if the decompressed file (if the source is is a gzip
	 *            file) will be delete on exit or not
	 * @return
	 */
	public static File decompressGZipFileIfNeccessary(File file, boolean ignoreExtension, boolean deleteOnExit) {
		log.info("Decompressing file " + file.getAbsolutePath());

		try {
			if (!ignoreExtension && !isCompressedFileExtension(FilenameUtils.getExtension(file.getAbsolutePath()))) {
				return file;
			}
			final File decompressGZipFile = ZipManager.decompressGZipFile(file);
			if (decompressGZipFile != null && deleteOnExit) {
				if (decompressGZipFile.getAbsolutePath() != file.getAbsolutePath()) {
					log.info("Setting 'deleteOnExit' to file: '" + decompressGZipFile.getAbsolutePath() + "'");
					decompressGZipFile.deleteOnExit();
					file.deleteOnExit();
				}
			}
			return decompressGZipFile;
		} catch (final Exception e) {
			log.info("The file " + file.getAbsolutePath() + " is not a zipped file. Returning the original file");
			log.warn(e.getMessage());
		}

		return file;
	}

	/**
	 * 
	 * @param file
	 * @param deleteOnExit
	 *            indicates if the decompressed file (if the source is is a gzip
	 *            file) will be delete on exit or not
	 * @return
	 */
	public static File decompressZipFileIfNeccessary(File file, boolean ignoreExtension, boolean deleteOnExit) {
		log.info("Decompressing file " + file.getAbsolutePath());

		try {
			if (!ignoreExtension && !isCompressedFileExtension(FilenameUtils.getExtension(file.getAbsolutePath()))) {
				return file;
			}
			final File decompressZipFile = ZipManager.decompressZipFile(file);
			if (decompressZipFile != null && deleteOnExit) {
				if (decompressZipFile.getAbsolutePath() != file.getAbsolutePath()) {
					log.info("Setting 'deleteOnExit' to file: '" + decompressZipFile.getAbsolutePath() + "'");
					decompressZipFile.deleteOnExit();
					file.deleteOnExit();
				}
			}
			return decompressZipFile;
		} catch (final Exception e) {
			log.info("The file " + file.getAbsolutePath() + " is not a zipped file. Returning the original file");
			log.warn(e.getMessage());
		}

		return file;
	}

	/**
	 * 
	 * @param file
	 * @param deleteOnExit
	 *            indicates if the decompressed file (if the source is is a gzip
	 *            file) will be delete on exit or not
	 * @return
	 */
	public static File decompressFileIfNeccessary(File file, boolean ignoreExtension, boolean deleteOnExit) {
		log.info("Decompressing file " + file.getAbsolutePath());
		if (!ignoreExtension && !isCompressedFileExtension(FilenameUtils.getExtension(file.getAbsolutePath()))) {
			return file;
		}
		try {
			final File decompressZipFile = ZipManager.decompressZipFile(file);
			if (decompressZipFile != null && deleteOnExit) {
				if (decompressZipFile.getAbsolutePath() != file.getAbsolutePath()) {
					log.info("Setting 'deleteOnExit' to file: '" + decompressZipFile.getAbsolutePath() + "'");
					decompressZipFile.deleteOnExit();
					file.deleteOnExit();
				}
			}
			return decompressZipFile;
		} catch (final Exception e) {
			log.info("The file " + file.getAbsolutePath() + " is not a zipped file. Returning the original file");
			log.warn(e.getMessage());
		}
		try {
			final File decompressGZipFile = ZipManager.decompressGZipFile(file);
			if (decompressGZipFile != null && deleteOnExit) {
				if (decompressGZipFile.getAbsolutePath() != file.getAbsolutePath()) {
					log.info("Setting 'deleteOnExit' to file: '" + decompressGZipFile.getAbsolutePath() + "'");
					decompressGZipFile.deleteOnExit();
					file.deleteOnExit();
				}
			}
			return decompressGZipFile;
		} catch (final Exception e) {
			log.info("The file " + file.getAbsolutePath() + " is not a zipped file. Returning the original file");
			log.warn(e.getMessage());
		}
		return file;
	}

	public static String getExtension(String name) {
		if (name == null || "".equals(name)) {
			return null;
		}
		return FilenameUtils.getExtension(name);
	}

}
