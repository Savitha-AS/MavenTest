package com.mip.framework.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;

import com.mip.framework.exceptions.ConfigFileNotFoundException;
import com.mip.framework.logger.Logger;
import com.mip.framework.logger.LoggerFactory;



/**
 * This class gives basic IO utilities.
 * 
 * @author T H B S
 */
public final class IOUtil {

	// The logger instance for this class
    private static final Logger logger = LoggerFactory.getInstance().getLogger(
            IOUtil.class);
    
    /**
     * Default constructor.
     */
    private IOUtil() {

        /*
         * Private Constructor to prevent instantiation of the class.
         */
    }

    /**
     * Closes an input stream.
     * 
     * @param inputStream
     *            The inputStream.
     * @throws IOException
     *             IOException.
     */
    public static void close(final InputStream inputStream)
        throws IOException {

    	try{
        logger.entering("close");

        if (inputStream != null) {
            inputStream.close();
        }
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        logger.exiting("close");
    }

    /**
     * Closes a writer.
     * 
     * @param writer
     *            Holds the stream writer.
     * @throws IOException
     *             IOException.
     */
    public static void close(final Writer writer) throws IOException {

    	logger.entering("close");

        if (writer != null) {
            writer.close();
        }

        logger.exiting("close");
    }

    /**
     * Gets the contents of the input stream as string.
     * 
     * @param in
     *            instance of <code>InputStream</code>
     * @return instance of <code>String</code>
     * @throws IOException
     *             IOException.
     */
    public static String getInputStreamContents(final InputStream in)
        throws IOException {

        logger.entering("getInputStreamContents");

        if (null == in) {
            return "";
        }

        // StringWriter is used to preserve the newline and special
        // chars

        StringWriter sw = new StringWriter();

        for (int c = in.read(); c != -1; c = in.read()) {
            sw.write(c);
        }

        logger.exiting("getInputStreamContents");

        return sw.toString();
    }
    
    /**
     * A generic URL locator which can find and return location of the specified
     * resource by searching the absolute path, the current classpath and the
     * system classpath.
     * 
     * @param fileName
     *            the name of the resource
     * @return the location of the resource
     * @throws ConfigException
     *             if it cannot locate a specified resource
     * @see #locate(String, String)
     */

    public static URL locate(String fileName) throws ConfigFileNotFoundException {
        return locate(null, fileName);
    }

    /**
     * A generic URL locator which can find and return location of the specified
     * resource by searching the absolute path, the current classpath and the
     * system classpath.
     * 
     * @param basePath
     *            The basePath to search for a file
     * @param fileName
     *            the name of the resource
     * @return the location of the resource
     * @throws ConfigException
     *             if locator cannot find the specified resource
     */

    public static URL locate(String basePath, String fileName)
            throws ConfigFileNotFoundException { 
        // returns null if fileName is undefined
        if (fileName == null) {
            return null;
        }

        URL url = null;

        // attempt to create an URL directly, on exception reset to null and try
        // other methods
        try {
            if (basePath == null) {
                url = new URL(fileName);
            } else {
                URL baseURL = new URL(basePath);
                url = new URL(baseURL, fileName);

                // check if the file exists
                InputStream in = null;
                try {
                    in = url.openStream();
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
            }

            logger.debug("Configuration loaded from the URL ", url);
        } catch (IOException e) {
            url = null;
        }

        // attempt to load from an absolute path
        if (url == null) {
            File tempFile = new File(fileName);
            if (tempFile.isAbsolute() && tempFile.exists()) {
                try {
                    url = tempFile.toURL();

                    logger.debug("Configuration loaded from the absolute path ",
                                        fileName);
                } catch (MalformedURLException e) {
                    logger.error("Error parsing the URL ", e);
                }
            }
        }

        // attempt to load from the base directory
        if (url == null) {
            try {
                File tfile = getFile(basePath, fileName);
                if (tfile != null && tfile.exists()) {
                    url = tfile.toURL();
                }

                if (url != null) {
                    logger.debug("Configuration loaded from the base path ",
                                fileName);
                }
            } catch (IOException e) {
                logger.error("Skipping base directory search ...", e);
            }
        }

        // attempt to load from the context classpath
        if (url == null) {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            url = loader.getResource(fileName);

            if (url != null) {
                logger.debug("Configuration loaded from the context classpath ("
                                    ,fileName, ")");
            }
        }

        // attempt to load from the system classpath
        if (url == null) {
            url = ClassLoader.getSystemResource(fileName);

            if (url != null) {
                logger.debug("Configuration loaded from the system classpath (",
                                    fileName, ")");
            }
        }

        // if still null, throw config exception as resource cannot be located
        if (url == null) {
            logger.error("Locater failed to find the requested resource :"
                    + fileName);
            throw new ConfigFileNotFoundException("Cannot locate file :" + fileName);
        }

        return url;
    }
    
    /**
     * For a given base directory and the file name, this method tries to
     * retrieve the <code>File</code> instance.
     * 
     * @param basePath
     *            the base directory to consider, can be <code>null</code>
     * @param fileName
     *            the name of the file to be loaded
     * @return the <code>File</code> representing the <code>basePath</code>
     *         and <code>fileName</code>
     */

    public static File getFile(String basePath, String fileName) {

        File file = null;
        if (basePath == null) {
            file = new File(fileName);
        }

        else {
            StringBuffer fName = new StringBuffer();
            fName.append(basePath);

            if (!basePath.endsWith(File.separator)) {
                fName.append(File.separator);
            }
            if (fileName.startsWith("." + File.separator)) {
                fName.append(fileName.substring(2));
            } else {
                fName.append(fileName);
            }

            file = new File(fName.toString());

        }

        return file;

    }

}
