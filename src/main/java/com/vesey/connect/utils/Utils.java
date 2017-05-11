/*
 * Utils.java
 *
 * Created on 12 November 2007, 11:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.vesey.connect
.utils;

//import com.vesey.database.generated.entity.Suppliertypes.SupplierType;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Permission;
import java.security.PermissionCollection;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.activation.MimetypesFileTypeMap;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


import org.jboss.logging.Logger;
import org.joda.time.DateTime;

import com.vesey.connect.entity.User;

/**
 *
 * @author richardh
 */
public class Utils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient static final Logger log = Logger.getLogger(Utils.class);

	
	public static String getFormattedUserName(User theUser) {
		if (theUser != null) {
			return theUser.getForename() + " " + theUser.getSurname();
		} else {
			//log.error("getFormattedUserName: User is NULL");
			return null;
		}
	}

	
	public static String truncate(String s, int len) {
		if (s == null) {
			return null;
		}
		return s.substring(0, Math.min(len, s.length()));
	}

	public static boolean isEmpty(String argParam) {
		if ((argParam == null) || (argParam.trim().equals(""))) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEmpty(Collection<?> argParam) {
		if ((argParam == null) || (argParam.isEmpty())) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEmpty(byte[] argParam) {
		if ((argParam == null) || (argParam.length == 0)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEmpty(BigInteger argParam) {
		if ((argParam == null) || (argParam.compareTo(BigInteger.ZERO) == 0)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEmpty(BigDecimal argParam) {
		if ((argParam == null) || (argParam.compareTo(BigDecimal.ZERO) == 0)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEmpty(Double argParam) {
		if ((argParam == null) || (argParam.compareTo(new Double(0)) == 0)) {
			return true;
		} else {
			return false;
		}
	}


	public static boolean isEmpty(Float argParam) {
		if ((argParam == null) || (argParam.compareTo(0.0f) == 0)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEmpty(Integer argParam) {
		if ((argParam == null) || (argParam.equals(0))) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEmpty(Date argParam) {
		if (argParam == null) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEmpty(Boolean argParam) {
		if (argParam == null) {
			return true;
		} else {
			return false;
		}
	}

	public static String makeSafe(String str) {
		if (isEmpty(str)) {
			return "";
		} else {
			return str;
		}
	}

	public static String makeSafe(Boolean bool) {
		if (isEmpty(bool)) {
			return "";
		} else {
			if (bool) {
				return "Yes";
			} else {
				return "No";
			}
		}
	}

	public static int makeSafe(Integer i) {
		if (isEmpty(i)) {
			return 0;
		} else {
			return i;
		}
	}

	protected static ClassLoader getCurrentClassLoader(Object defaultObject) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		if (loader == null) {
			loader = defaultObject.getClass().getClassLoader();
		}

		return loader;
	}


	public static String getElapsedString(Date d) {
		// log.info("getElapsedString: Start");
		if (d != null) {
			Calendar currentDate = Calendar.getInstance();
			Calendar eventDate = Calendar.getInstance();
			eventDate.setTime(d);
			int daysElapsed = daysUntil(currentDate, eventDate);
			// log.info("getElapsedString: Dayes elapesed = " + daysElapsed);
			if (daysElapsed < -1) {
				if (daysElapsed >= -5) {
					return "(" + daysElapsed * -1 + " days ago)";
				} else {
					return "";
				}
			} else if (daysElapsed == -1) {
				return "(yesterday)";
			} else if (daysElapsed == 0) {
				return "(today)";
			} else if (daysElapsed == +1) {
				return "(tomorrow)";
			} else {
				if (daysElapsed <= 5) {
					return "(" + daysElapsed + " days time)";
				} else {
					return "";
				}
			}
		} else {
			// log.info("getElapsedString: Date is null - exiting.");
			return "";
		}
	}

	public static int daysUntil(Calendar fromDate, Calendar toDate) {
		return daysSinceEpoch(toDate) - daysSinceEpoch(fromDate);
	}

	public static int daysSinceEpoch(Calendar day) {
		int year = day.get(Calendar.YEAR);
		int month = day.get(Calendar.MONTH);
		int daysThisYear = cumulDaysToMonth[month] + day.get(Calendar.DAY_OF_MONTH) - 1;
		if ((month > 1) && isLeapYear(year)) {
			daysThisYear++;
		}

		return daysToYear(year) + daysThisYear;
	}

	public static boolean isLeapYear(int year) {
		return (year % 400 == 0) || ((year % 100 != 0) && (year % 4 == 0));
	}

	static int daysToYear(int year) {
		return (365 * year) + numLeapsToYear(year);
	}

	static int numLeapsToYear(int year) {
		int num4y = (year - 1) / 4;
		int num100y = (year - 1) / 100;
		int num400y = (year - 1) / 400;
		return num4y - num100y + num400y;
	}

	private static final int[] cumulDaysToMonth = { 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334 };

	public static String getStringProperty(String propertyName) {
		String returnValue = null;
		try {
			returnValue = System.getProperty(propertyName);
		} catch (Exception e) {
			log.error("getStringProperty: Exception looking up " + propertyName + " (not in AS 7?) : " + e.getLocalizedMessage());
		}
		// if (Utils.isEmpty(returnValue)) {
		// try {
		// ResourceBundle propertiesFile =
		// ResourceBundle.getBundle("searchpoint");
		// returnValue = propertiesFile.getString(propertyName).trim();
		// } catch (Exception e) {
		// log.error("getStringProperty: Exception looking up " + propertyName +
		// " (not in AS 6?) : " + e.getLocalizedMessage());
		// }
		// }

		// log.info("getStringProperty: Returning : " + returnValue);
		return returnValue;
	}

	

	public static boolean serverInTestMode() {
		String inTest = getStringProperty("serverInTestMode");
		if (!inTest.equalsIgnoreCase("true")) {
			return false;
		} else {
			return true;
		}
	}


	public static Boolean writeFileToOutputStream(OutputStream out, String fullPath) {
		log.info("writeFileToOutputStream: Start, File = " + fullPath);

		if (fullPath == null) {
			log.error("writeFileToOutputStream: fullPath is NULL");
			return false;
		}
		if (out == null) {
			log.error("writeFileToOutputStream: OutputStream is NULL");
			return false;
		}
		FileInputStream theFile = null;
		try {
			theFile = new FileInputStream(fullPath);
		} catch (FileNotFoundException ex) {
			log.error("writeFileToOutputStream: Unable to create FileInputStream  from : " + fullPath + "error =  " + ex.getLocalizedMessage());
			return false;
		}
		byte buffer[] = new byte[1024];
		int numRead = 0;

		log.info("writeFileToOutputStream: writing to output stream...");
		while (numRead != -1) {
			try {
				numRead = theFile.read(buffer, 0, 1024);
			} catch (IOException ex) {
				log.error("writeFileToOutputStream: Unable to read from file : " + fullPath + "error =  " + ex.getLocalizedMessage());
				numRead = -1;
			}
			if (numRead > 0) {
				try {
					out.write(buffer, 0, numRead);
				} catch (IOException ex) {
					log.error("writeFileToOutputStream: Unable to write to output stream, error =  " + ex.getLocalizedMessage());
					log.error("writeFileToOutputStream: buffer = " + buffer);
					log.error("writeFileToOutputStream: numRead = " + numRead);
					numRead = -1;
				}

			}
		}

		if (theFile != null) {
			try {
				theFile.close();
			} catch (Exception e1) {
				log.error("writeFileToOutputStream: Exception closing file : " + e1.getMessage());
			}
		}

		log.info("writeFileToOutputStream: End");
		return true;

	}

	/**
	 * Returns the full path to a temp file.
	 *
	 * @param extention
	 * @return - temp file full path.
	 */
	public static String getTempFile(String extention) {
		String tempFileName = getTempDirectory() + File.separator + UUID.randomUUID().toString() + extention;
		File tempFile = new File(tempFileName);
		tempFile.getParentFile().mkdirs();

		return tempFileName;
	}

	public static String getTempFile() {
		String tempFileName = getTempDirectory() + File.separator + UUID.randomUUID().toString() + ".tmp";
		File tempFile = new File(tempFileName);
		tempFile.getParentFile().mkdirs();

		return tempFileName;
	}

	/**
	 * Saves the given data to the given full path file name.
	 *
	 * @param fileName
	 *            - full path of file to save (or overwrite if it is already
	 *            there).
	 * @param fileData
	 *            - data to save.
	 * @return - true if ok, otherwise false.
	 */
	public static File saveBAOS(String fileName, ByteArrayOutputStream fileData) {
		FileOutputStream fos = null;
		File theFile = null;
		try {
			theFile = new File(fileName);
			theFile.getParentFile().mkdirs();
			log.info("saveBAOS: File being saved = " + theFile.getAbsolutePath());
			fos = new FileOutputStream(fileName);

			fileData.writeTo(fos);
		} catch (Exception e) {
			log.error("saveBAOS: Error saving file: " + e.getMessage());
			return null;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e1) {
					log.error("saveBAOS: Error closing file: " + e1.getMessage());
				}
			}
		}
		log.info("saveBAOS: End (Success)");
		return theFile;
	}


	/**
	 * Returns the full path to the temp directory.
	 *
	 * @return - temp directory full path.
	 */
	public static String getTempDirectory() {
		String tempPath = getStringProperty("rootFileLocation");
		if (Utils.isEmpty(tempPath)) {
			tempPath = "c:";
		}
		tempPath = tempPath + File.separator + "temp";
		return tempPath;
	}

	/**
	 * Returns the file extension of the given file, including the ".". e.g.
	 * ".zip" or ".pdf".
	 *
	 * @param fileName
	 * @return
	 */
	public static String getFileExtension(String fileName) {
		String fileExtension = "";
		if (fileName == null) {
			log.error("getFileExtension: Error - file name is null");
			return null;
		}
		int dotPos = fileName.lastIndexOf(".");
		if (dotPos > 0) {
			fileExtension = fileName.substring(dotPos);
		}
		return fileExtension;
	}

	public static String getRelativeFileName(String fileName) {
		String relativeFileName;
		if (fileName == null) {
			log.error("getRelativeFileName: Error - file name is null");
			return null;
		}
		int dotPos = fileName.lastIndexOf("\\") + 1;
		if (dotPos > 0) {
			relativeFileName = fileName.substring(dotPos);
		} else {
			relativeFileName = fileName;
		}
		return relativeFileName;
	}

	public static String getFolderFromFileName(String fileName) {
		String relativeFileName;
		if (fileName == null) {
			log.error("getFolderFromFileName: Error - file name is null");
			return null;
		}
		int dotPos = fileName.lastIndexOf("\\");
		if (dotPos > 0) {
			relativeFileName = fileName.substring(0, dotPos);
		} else {
			relativeFileName = null;
		}
		return relativeFileName;
	}

	
	public static String getAlternateFileName(String filename, Integer count) {
		String fileExt = getFileExtension(filename);
		String baseFilename = filename.substring(0, filename.lastIndexOf("."));

		if (!Utils.isEmpty(baseFilename)) {
			if (baseFilename.contains("_")) {
				String endFilename = baseFilename.substring(filename.lastIndexOf("_") + 1, baseFilename.length());
				if (!Utils.isEmpty(endFilename)) {
					try {
						Integer i = Integer.parseInt(endFilename);
						if (i != null) {
							// we have successfully found a number on the end of
							// the string - remove it
							baseFilename = filename.substring(0, filename.lastIndexOf("_"));
						}
					} catch (Exception e) {
						// ignore
					}
				}
			}
		}

		String retStr = baseFilename + "_" + count.toString() + fileExt;
		log.info("getAlternateFileName: Returning :" + retStr);
		return retStr;
	}


	/**
	 * Gets the byte array for the given input stream.
	 *
	 * @param is
	 * @return
	 */
	public static byte[] getBytes(InputStream is) throws IOException {
		log.info("getBytes: Start");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int numRead = 0;
		byte[] buffer = new byte[1024];
		while (numRead > -1) {
			numRead = is.read(buffer, 0, 1024);
			if (numRead > -1) {
				bos.write(buffer, 0, numRead);
			}
		}
		log.info("getBytes: End. BOS size = " + bos.size());
		return bos.toByteArray();
	}

	public static byte[] getBytes(String fileName) {
		log.info("getBytes: Start. FileName = " + fileName);
		File file = new File(fileName);
		if (file.exists()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(fileName);
			} catch (FileNotFoundException ex) {
				log.error("getBytes: File not Found Exception : " + ex.getMessage());
				return null;
			}
			try {
				log.info("getBytes: Returning bytes...");
				return getBytes(fis);
			} catch (IOException ex) {
				log.error("getBytes: IO Exception : " + ex.getMessage());
				return null;
			}
		} else {
			log.error("getBytes: File not Found - " + fileName);
			return null;
		}
	}


	public static Integer copyInputStream(InputStream in, OutputStream out) {
		Integer fileSize = new Integer(0);
		byte[] buffer = new byte[1024];
		int len;
		try {
			while ((len = in.read(buffer)) >= 0) {
				out.write(buffer, 0, len);
				fileSize = fileSize + len;
			}
		} catch (IOException ex) {
			log.error("copyInputStream: Error reading from input stream : " + ex.getLocalizedMessage());
			return null;
		}
		try {
			in.close();
		} catch (IOException ex) {
			log.error("copyInputStream: Error closing input stream : " + ex.getLocalizedMessage());
			return null;
		}
		try {
			out.close();
		} catch (IOException ex) {
			log.error("copyInputStream: Error closing output stream : " + ex.getLocalizedMessage());
			return null;
		}
		return fileSize;
	}

	/**
	 * Used to create and output the given file to the page.
	 *
	 * @param out
	 * @param fullFilePath
	 *            - the full file path to output.
	 * @throws IOException
	 */
	public static void sendOutputFile(OutputStream out, String fullFilePath) {
		// log.info("sendOutputFile: Start");
		log.info("sendOutputFile: Outputting = " + fullFilePath);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fullFilePath);
			if (fis != null) {
				byte[] buffer = new byte[1024];
				int numRead = fis.read(buffer);
				while (numRead != -1) {
					out.write(buffer, 0, numRead);
					numRead = fis.read(buffer);
				}
			}
		} catch (Exception e) {
			log.error("sendOutputFile: error getting output file: " + e.getMessage());
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException ex) {
					log.error("sendOutputFile: error closing output file: " + ex.getMessage());
				}
			}
		}
	}

	public static byte[] resize(byte[] original, double targetWidth, double targetHeight, Boolean multipass) {

		log.info("resize: Start");
		if ((original == null) || (original.length == 0)) {
			log.error("resize: original is null or empty");
			return null;
		}
		// log.info("resize: original size = " + original.length);

		ByteArrayInputStream bais = new ByteArrayInputStream(original);
		BufferedImage originalBufferedImage = null;

		try {
			originalBufferedImage = ImageIO.read(bais);
		} catch (Exception e) {
			// log.warn("resize: Initial exception : " +
			// e.getLocalizedMessage());

			// log.info("resize: Find a suitable reader");
			Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("JPEG");
			ImageReader reader = null;
			while (readers.hasNext()) {
				reader = readers.next();
				if (reader.canReadRaster()) {
					break;
				}
			}

			// Stream the image file (the original CMYK image)
			// log.info("resize: Stream the original image");
			ImageInputStream input;
			try {
				input = ImageIO.createImageInputStream(original);
			} catch (IOException ex) {
				log.error("resize: Exception creating Image File : " + ex.getLocalizedMessage());
				return null;
			}
			reader.setInput(input);

			// Read the image raster
			// log.info("resize: Read the raster");
			Raster raster;
			try {
				raster = reader.readRaster(0, null);
			} catch (IOException ex) {
				log.error("resize: Exception reading Raster data : " + ex.getLocalizedMessage());
				return null;
			}

			// Create a new RGB image
			// log.info("resize: Create the RGB image");
			originalBufferedImage = new BufferedImage(raster.getWidth(), raster.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

			// Fill the new image with the old raster
			originalBufferedImage.getRaster().setRect(raster);
		}

		if (originalBufferedImage == null) {
			log.error("resize: Original Buffered Image is null");
			return null;
		}

		// log.info("resize: getting Width and Height");
		double originalWidth = originalBufferedImage.getWidth(null);
		double originalHeight = originalBufferedImage.getHeight(null);

		double scaleFactor = getScaleFactor(originalWidth, originalHeight, targetWidth, targetHeight);

		int newW = (int) (originalBufferedImage.getWidth() * scaleFactor);
		int newH = (int) (originalBufferedImage.getHeight() * scaleFactor);

		try {
			BufferedImage outBufferedImage = getScaledInstance(originalBufferedImage, newW, newH, RenderingHints.VALUE_INTERPOLATION_BICUBIC, multipass);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(outBufferedImage, "png", baos);
			byte[] bytesOut = baos.toByteArray();
			log.info("resize: End.");
			return bytesOut;
		} catch (Exception ex) {
			log.error("resize: Exception whilst resizing image : " + ex.getMessage());
			return null;
		}
	}

	/**
	 * Convenience method that returns a scaled instance of the provided
	 * {@code BufferedImage}.
	 *
	 * @param img
	 *            the original image to be scaled
	 * @param targetWidth
	 *            the desired width of the scaled instance, in pixels
	 * @param targetHeight
	 *            the desired height of the scaled instance, in pixels
	 * @param hint
	 *            one of the rendering hints that corresponds to
	 *            {@code RenderingHints.KEY_INTERPOLATION} (e.g. null null null
	 *            null null null null null null null null null null null
	 *            {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
	 *            {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR},
	 *            {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
	 * @param higherQuality
	 *            if true, this method will use a multi-step scaling technique
	 *            that provides higher quality than the usual one-step technique
	 *            (only useful in downscaling cases, where {@code targetWidth}
	 *            or {@code targetHeight} is smaller than the original
	 *            dimensions, and generally only when the {@code BILINEAR} hint
	 *            is specified)
	 * @return a scaled version of the original {@code BufferedImage}
	 */
	public static BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight, Object hint, boolean higherQuality) {
		log.info("getScaledInstance: Start");
		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = (BufferedImage) img;
		int w, h;

		if (higherQuality) {
			// Use multi-step technique: start with original size, then
			// scale down in multiple passes with drawImage()
			// until the target size is reached
			w = img.getWidth();
			h = img.getHeight();
		} else {
			// Use one-step technique: scale directly from original
			// size to target size with a single drawImage() call
			w = targetWidth;
			h = targetHeight;
		}

		// log.info("getScaledInstance: Target Width = " + targetWidth);
		// log.info("getScaledInstance: Target Height = " + targetHeight);
		// log.info("getScaledInstance: W Width = " + w);
		// log.info("getScaledInstance: H Height = " + h);
		do {
			// log.info("getScaledInstance: Iterating...");
			if (higherQuality) {
				if (w > targetWidth) {
					w /= 2;
					if (w < targetWidth) {
						w = targetWidth;
					}
				}
				if (h > targetHeight) {
					h /= 2;
					if (h < targetHeight) {
						h = targetHeight;
					}
				}

				if (w <= targetWidth && h <= targetHeight) {
					h = targetHeight;
					w = targetWidth;
				}
			}

			// log.info("getScaledInstance: Creating New Buffered Image...");
			BufferedImage tmp = new BufferedImage(w, h, type);
			// log.info("getScaledInstance: Creating Graphics2D object...");
			Graphics2D g2 = tmp.createGraphics();
			// log.info("getScaledInstance: Setting Rendering Hint...");
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
			// log.info("getScaledInstance: Drawing Image...");
			g2.drawImage(ret, 0, 0, w, h, null);
			// log.info("getScaledInstance: Disposing...");
			g2.dispose();

			ret = tmp;
			// log.info("getScaledInstance: Target Width Now " + w);
			// log.info("getScaledInstance: Target Height Now " + h);
		} while (w != targetWidth || h != targetHeight);

		log.info("getScaledInstance: End");
		return ret;
	}

	public static double getScaleFactor(double width, double height, double targetWidth, double targetHeight) {
		double wScaleFactor = targetWidth / width;
		double hScaleFactor = targetHeight / height;
		if (wScaleFactor > hScaleFactor) {
			return hScaleFactor;
		} else {
			return wScaleFactor;
		}
	}

	/**
	 * @return the VAT_RATE
	 */
	public static BigDecimal getVatRate() {
		return new BigDecimal(getFloatProperty("vatRate"));
	}

	private static float getFloatProperty(String propertyName) {
		String floatValueAsString = getStringProperty(propertyName);
		float returnValue = Float.parseFloat(floatValueAsString);
		return returnValue;
	}



	public static String convertToFullPath(String relativePath) {
		String rootPath = getStringProperty("rootFileLocation");
		return rootPath + File.separator + relativePath;
	}


	public static String deSpace(String fileName) {
		log.info("deSpace: Original FileName= " + fileName);
		String retString = fileName.replace(" ", "_").trim();
		retString = retString.replace("%20", "_");

		// remove drive letters etc.
		Integer beginIndex = retString.lastIndexOf("\\");
		if (beginIndex > 0) {
			retString = retString.substring(beginIndex, retString.length());
		}
		beginIndex = retString.lastIndexOf("/");
		if (beginIndex > 0) {
			retString = retString.substring(beginIndex, retString.length());
		}
		beginIndex = retString.lastIndexOf(":");
		if (beginIndex > 0) {
			retString = retString.substring(beginIndex, retString.length());
		}
		retString = retString.replace("\\", "_");
		retString = retString.replace("/", "_");
		retString = retString.replace(":", "_");

		log.info("deSpace: FileName Now = " + retString);
		return retString;
	}

	public static String removeSpaces(String string) {
		if (string != null) {
			String retString = string.replace(" ", "").trim();
			return retString;
		} else {
			return string;
		}

	}

	public static String stripLeadingAndTrailingQuotes(String str) {
		if (str.startsWith("\"")) {
			str = str.substring(1, str.length());
		}
		if (str.endsWith("\"")) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	public static String clean(String in) {
		if (in != null) {
			String retString = in.replace("&", " and ").trim();
			return retString;
		} else {
			return null;
		}
	}

	public static BigDecimal getAmountLessVAT(BigDecimal incVAT) {
		if ((incVAT != null) && (incVAT.compareTo(new BigDecimal("0.0")) > 0)) {
			BigDecimal inverseVATRate = Utils.getVatRate().add(new BigDecimal("1.0"));
			// log.info("getAmountLessVAT: Inverse VAT Rate = " +
			// inverseVATRate);

			BigDecimal retVal = incVAT.divide(inverseVATRate, 4, RoundingMode.HALF_UP);
			retVal = retVal.setScale(2, RoundingMode.HALF_UP);
			return retVal;
		} else {
			return new BigDecimal("0");
		}
	}


	public static String getCookie(String value) {
		//log.info("getCookie: Start. Looking for cookie :  " + value);
		if (FacesContext.getCurrentInstance() != null) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			Cookie[] cookies = httpServletRequest.getCookies();
			if (cookies != null) {
				// log.info("getCookie: cookies returned : " + cookies.length);
				for (int i = 0; i < cookies.length; i++) {
					// log.info("getCookie: Iterating. This cookie = [" +
					// cookies[i].getName() + ", " + cookies[i].getValue() + ",
					// " + cookies[i].getDomain() + ", " + cookies[i].getPath()
					// + "]");
					if (cookies[i].getName().equalsIgnoreCase(value)) {
						log.info("getCookie: Found Cookie = " + cookies[i].getValue());
						return cookies[i].getValue();
					}
				}
			}
		}
		//log.info("getCookie: Cookie not found");
		return null;
	}

	public static Boolean setCookie(String key, String value) {
		return setCookie(key, value, null);
	}

	public static Boolean setCookie(String key, String value, Integer age) {
		// create cookies
		log.info("setCookie: Start");
		HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		String cookie = "";
		if (age != null) {
			cookie = createCookie(key, value, age);
		} else {
			cookie = createCookie(key, value, 63115200); // 2 years
		}

		if (httpServletResponse != null) {
			log.info("setCookie: writing cookie : key = " + key + ", Value = " + value);
			httpServletResponse.addHeader("Set-Cookie", cookie.toString());
			log.info("setCookie: End");
			return true;
		} else {
			log.error("setCookie: End. httpServletResponse is NULL - unable to set cookie");
			return false;
		}
	}

	public static String createCookie(String key, String value, Integer age) {
		int expiration = age;
		StringBuilder cookie = new StringBuilder(key + "=" + value + ";");

		DateFormat df = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss 'GMT'", Locale.UK);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, age);
		cookie.append("Expires=" + df.format(cal.getTime()) + "; ");

		cookie.append("Version=1; ");
		cookie.append("Path=/; ");
		cookie.append("Max-Age=" + expiration + "; ");
		cookie.append("httpOnly; ");
		return cookie.toString();
		// resp.setHeader("Set-Cookie", cookie.toString());
	}

	public static boolean moveFolder(String folder, String destinationFolder) {
		log.info("moveFolder: Start");
		log.info("moveFolder: Folder to be moved = " + folder);
		log.info("moveFolder: Destination Folder = " + destinationFolder);
		// File (or directory) to be moved
		File file = new File(folder);

		// Destination directory
		File dir = new File(destinationFolder);

		// Move file to new directory
		boolean success = file.renameTo(new File(dir, file.getName()));
		if (!success) {
			// File was not successfully moved
			log.error("moveFolder: File was not moved");
			return false;
		}
		log.info("moveFolder: End");
		return true;
	}

	public static String toProperCase(String theString) {
		log.info("toProperCase: Start. The String = " + theString);
		if (Utils.isEmpty(theString)) {
			log.info("toProperCase: String is Null");
			return null;
		}
		theString = Utils.clean(theString);
		java.io.StringReader in = new java.io.StringReader(theString.toLowerCase());
		boolean precededBySpace = true;
		StringBuilder properCase = new StringBuilder();
		while (true) {
			try {
				int i = in.read();
				if (i == -1) {
					break;
				}
				char c = (char) i;
				if (c == ' ' || c == '"' || c == '(' || c == '.' || c == '/' || c == '\\' || c == ',') {
					properCase.append(c);
					precededBySpace = true;
				} else {
					if (precededBySpace) {
						properCase.append(Character.toUpperCase(c));
					} else {
						properCase.append(c);
					}
					precededBySpace = false;
				}
			} catch (IOException ex) {
				log.error("toProperCase: IOException : " + ex.getLocalizedMessage());
				return theString;
			}
		}

		log.info("toProperCase: End. Returning = " + properCase.toString());
		return properCase.toString();

	}


	public static String generateGuid() {
		return UUID.randomUUID().toString();
	}


	public static String readResourceFile(InputStream is) {
		String retVal;
		if (is != null) {
			log.info("getXMLSource: Got Stream: " + is);
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
				retVal = writer.toString();
			} catch (Exception e) {
				log.error("getXMLSource: Exception: " + e.getLocalizedMessage());
				retVal = null;
			} finally {
				try {
					is.close();
				} catch (IOException ex) {
					log.error("getXMLSource: IOException: " + ex.getLocalizedMessage());
					retVal = null;
				}
			}
			return retVal;
		} else {
			log.info("getXMLSource: Failed to get XML as Stream: " + is);
			return null;
		}
	}

	public static Integer calculateAverage(List<Integer> marks) {
		Integer sum = 0;
		if (!Utils.isEmpty(marks)) {
			for (Integer mark : marks) {
				sum += mark;
			}
			Float fl = sum.floatValue() / marks.size();
			return fl.intValue();
		}
		return null;
	}

	public static XMLGregorianCalendar getGregorianCalendarObjectFromDate(Date date) {
		if (date != null) {
			try {
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(date);
				XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
				return xmlCal;
			} catch (DatatypeConfigurationException ex) {
				log.error("getGregorianCalendarObjectFromDate: DatetypeConfigurationException - " + ex);
				return null;
			}
		} else {
			return null;
		}
	}

	public static String preparePostcode(String postcode) {
		// replace spaces
		String retString = postcode.replace(" ", "").trim();
		return retString;
	}

	public static String strSeparator = "__,__";

	public static String convertArrayToString(List<String> list) {
		String str = null;
		if (!isEmpty(list)) {
			str = "";
			Iterator<String> iter = list.iterator();
			while (iter.hasNext()) {
				String thisStr = iter.next();
				str += thisStr;
				if (iter.hasNext()) {
					str += strSeparator;
				}
			}
		}
		return str;
	}

	public static List<String> convertStringToArray(String str) {
		if (!isEmpty(str)) {
			String[] arr = str.split(strSeparator);
			return Arrays.asList(arr);
		} else {
			return null;
		}
	}

	public static void addToWhereClause(StringBuffer whereClause, String thisClause) {
		if (whereClause.length() > 0) {
			// Not the first time...
			whereClause.append(" AND ");
		} else {
			// first time
			whereClause.append(" WHERE ");
		}

		whereClause.append(thisClause);
	}

	public static void addToHavingClause(StringBuffer havingClause, String thisClause) {
		if (havingClause.length() > 0) {
			// Not the first time...
			havingClause.append(" HAVING ");
		} else {
			// first time
			havingClause.append(" AND ");
		}

		havingClause.append(thisClause);
	}


	public static String getMimeType(String fileName) {
		if ((fileName == null) || (fileName.equals(""))) {
			log.error("getMimeType: File is null or blank");
			return null;
		}
		File fileInfo = new File(fileName);

		String actualName = fileInfo.getName();
		MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
		mimetypesFileTypeMap.addMimeTypes("application/pdf pdf PDF");
		mimetypesFileTypeMap.addMimeTypes("application/zip zip ZIP");
		mimetypesFileTypeMap.addMimeTypes("image/bmp bmp BMP");
		mimetypesFileTypeMap.addMimeTypes("image/gif gif GIF");
		mimetypesFileTypeMap.addMimeTypes("image/jpg jpg JPG");
		mimetypesFileTypeMap.addMimeTypes("image/tif tif TIF");
		mimetypesFileTypeMap.addMimeTypes("image/png png PNG");
		mimetypesFileTypeMap.addMimeTypes("text/csv csv CSV");
		mimetypesFileTypeMap.addMimeTypes("application/vnd.ms-excel xls XLS");
		// log.info("getMimeType: MIME Type found = " +
		// mimetypesFileTypeMap.getContentType(actualName));
		return mimetypesFileTypeMap.getContentType(actualName);
	}


	public static boolean isDifferentDate(Date formerCompletedDate, Date newCompletionDate) {
		if (formerCompletedDate != null && newCompletionDate != null) {
			DateTime formerDateTime = new DateTime(formerCompletedDate).withTime(0, 0, 0, 0);
			DateTime newDateTime = new DateTime(newCompletionDate).withTime(0, 0, 0, 0);
			if (!formerDateTime.equals(newDateTime)) {
				log.info("isDifferentDate: Dates are different - returning true");
				return true;
			} else {
				log.info("isDifferentDate: Dates are same - returning false");
				return false;
			}
		} else {
			log.info("isDifferentDate: one or both dates are null - returning true");
			return true;
		}

	}


}
