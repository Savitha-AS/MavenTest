/*
 * Created on Dec 15, 2009
 *
 * This file contains the utility class for handling various file operations.
 */
package com.mip.application.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.mip.application.constants.ReportKeys;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * This class is the utility class for handling various file operations.
 * 
 * @author THBS
 * 
 */
public class FileUtil {

	/**
	 * instance of <code>Log</code>.
	 */
	private static final MISPLogger logger = LoggerFactory.getInstance()
			.getLogger(FileUtil.class);

	/**
	 * This method will create a zip file with the given name for all the files
	 * present in the given directory path.
	 * 
	 * @param dirPath
	 *            Path of the directory which contains all the files to be
	 *            zipped.
	 * 
	 * @param zipFileName
	 *            Holds the name of the zip file.
	 * 
	 * @return true if file is created successfully else false.
	 * 
	 * @throws MISPException
	 *             If any Exception occurs.
	 */
	public static boolean createZipFile(String dirPath, String zipFileName)
			throws MISPException {

		logger.entering("createZipFile", dirPath, zipFileName);
		boolean retValue = false;
		int zippedFileCounter=0;
		File destDirPath = new File(dirPath);
		if (!destDirPath.isDirectory()) {
			logger.debug("Invalid directory path");
			return false;
		}
		ZipOutputStream zipOpStream = null;
		try {
			File[] dirFiles = destDirPath.listFiles();
			File zipFile = new File(dirPath + File.separator + zipFileName);
			zipOpStream = new ZipOutputStream(new FileOutputStream(zipFile));
			zipOpStream.setLevel(Deflater.DEFAULT_COMPRESSION);

			// Create a buffer for reading the files
			byte[] buffer = new byte[1024];

			for (int i = 0; i < dirFiles.length; i++) {
				if(!dirFiles[i].getName().contains(ReportKeys.REPORT_ZIP_FILE_TYPE)){
					zippedFileCounter++;
					FileInputStream fileIpStream = new FileInputStream(dirFiles[i]);
					zipOpStream.putNextEntry(new ZipEntry(dirFiles[i].getName()));
					// Transfer bytes from the file to the ZIP file
					int length;
					while ((length = fileIpStream.read(buffer)) > 0) {
						zipOpStream.write(buffer, 0, length);
					}
					zipOpStream.flush();
					zipOpStream.closeEntry();
					fileIpStream.close();
				}
			}
			retValue = true;
			logger.debug("zipped ",zippedFileCounter," files");
			
		} catch (FileNotFoundException e) {
			logger.error("Exception occured while creating zip file ", e);
			throw new MISPException(
					"Exception occured while creating zip file ", e);
		} catch (IOException e) {
			logger.error("Exception occured while creating zip file ", e);
			throw new MISPException(
					"Exception occured while creating zip file ", e);
		} finally {
			close(zipOpStream);
		}
		logger.exiting("createZipFile", retValue);
		return retValue;
	}

	/**
	 * This method will copy the file from source path to destination path. The
	 * source file will not be deleted after the copy operation.
	 * 
	 * @param srcFilePath
	 *            Full path of the souce file to be moved.
	 * 
	 * @param destFilePath
	 *            Full destination path
	 * 
	 * @throws MISPException
	 *             If any exception occurs.
	 */
	public static void moveFile(String srcFilePath, String destFilePath)
			throws MISPException {

		logger.entering("moveFile", srcFilePath, destFilePath);

		InputStream ipStream = null;
		OutputStream opStream = null;

		try {

			File srcFile = new File(srcFilePath);
			File destFile = new File(destFilePath);

			if (!destFile.exists()) {

				destFile.createNewFile();
			}

			ipStream = new FileInputStream(srcFile);
			opStream = new FileOutputStream(destFile);

			byte[] buffer = new byte[1024];
			int length;

			while ((length = ipStream.read(buffer)) > 0) {
				opStream.write(buffer, 0, length);
			}

		} catch (IOException e) {
			logger.debug("Exception occured while moving files", e);
			throw new MISPException("Exception occured while moving files", e);
		}

		finally {
			close(ipStream);
			close(opStream);
		}
		logger.exiting("moveFile");

	}

	/**
	 * This method deletes all the files present in the given directory.
	 * 
	 * @param dirPath
	 *            String containing the directory path from which files have to
	 *            be deleted.
	 */
	public static void deleteFilesInDir(String dirPath) {

		logger.entering("deleteFilesInDir", dirPath);

		File srcDir = new File(dirPath);
		if (!srcDir.isDirectory()) {
			logger.debug("Invalid directory path : " + dirPath);
		}

		File[] dirFiles = srcDir.listFiles();
		for (int i = 0; i < dirFiles.length; i++) {
			 dirFiles[i].delete();
		}

		logger.exiting("deleteFilesInDir");
	}

	/**
	 * This method deletes all the temporary files present in the given
	 * directory except the archive file
	 * 
	 * @param dirPath
	 *            String containing the directory path from which files have to
	 *            be deleted.
	 */
	public static void deleteAllTempFilesInDir(String dirPath,
			String reportArchiveFileName) {

		logger.entering("deleteAllTempFilesInDir", dirPath);

		File srcDir = new File(dirPath);
		if (!srcDir.isDirectory()) {
			logger.debug("Invalid directory path : " + dirPath);
		}

		File[] dirFiles = srcDir.listFiles();
		for (int i = 0; i < dirFiles.length; i++) {

			if (!dirFiles[i].getName().equals(reportArchiveFileName)) {
				 dirFiles[i].delete();
			}
		}

		logger.exiting("deleteAllTempFilesInDir");
	}
	
	/**
	 * This method closes the input stream.
	 * 
	 * @param ipStream
	 *            Input stream to be closed.
	 * 
	 * @throws MISPException
	 *             If any <code>IOException</code> occurs.
	 */
	public static void close(InputStream ipStream) throws MISPException {

		if (ipStream != null) {
			try {
				ipStream.close();
			} catch (IOException e) {
				logger.debug("Exception occured while closing input stream");
				throw new MISPException(
						"Exception occured while closing input stream");
			}
		}

	}

	/**
	 * This method closes the output stream.
	 * 
	 * @param opStream
	 *            Output stream to be closed.
	 * 
	 * @throws MISPException
	 *             If any <code>IOException</code> occurs.
	 */
	public static void close(OutputStream opStream) throws MISPException {

		if (opStream != null) {
			try {
				opStream.close();
			} catch (IOException e) {
				logger.debug("Exception occured while closing input stream");
				throw new MISPException(
						"Exception occured while closing input stream");
			}
		}
	}

}