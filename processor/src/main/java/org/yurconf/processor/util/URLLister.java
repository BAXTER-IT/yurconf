/*
 * Configuration Processors
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package org.yurconf.processor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

/**
 * List content under a specified URL.
 *
 * @author Arpad Roziczky
 * @since 1.5
 */
public abstract class URLLister
{

  private static final URLLister JAR_URLLISTER = new JARURLLister();

  private static final URLLister FILE_URLLISTER = new FileURLLister();

  private static final FilenameAcceptor ALL_ACCEPTOR = new FilenameAcceptor()
  {

	@Override
	public boolean accept(final String path)
	{
	  return true;
	}
  };

  /**
   * Returns the URL Lister appropriate for specified URL.
   *
   * @param url
   *          input url to process
   * @return some implementation of URL Lister
   */
  public static URLLister getInstance(final URL url)
  {
	if (JARURLLister.URL_PATTERN.matcher(url.toString()).matches())
	{
	  return getJarListerInstance();
	}
	if (url.toString().startsWith(FileURLLister.URL_PREFIX))
	{
	  return getFileListerInstance();
	}
	throw new IllegalArgumentException("Unsupported URL");
  }

  /**
   * Returns the lister for JAR URLs.
   *
   * @return jar lister
   */
  static URLLister getJarListerInstance()
  {
	return JAR_URLLISTER;
  }

  /**
   * Returns the lister for File URLs.
   *
   * @return file lister
   */
  static URLLister getFileListerInstance()
  {
	return FILE_URLLISTER;
  }

  /**
   * Returns a list of entries under specified URL. These are the entries paths relative to the input URL.
   *
   * @param url
   *          input URL
   * @return list of string paths
   * @throws IOException
   *           if IO failed during the listing
   */
  public abstract List<String> list(final URL url) throws IOException;

  /**
   * Returns a list of entries under specified URL, whose names satisfy specified pattern. These are the entries paths relative to
   * the input URL.
   *
   * @param url
   *          input URL
   * @param mask
   *          the entry name pattern like in OS filenames, e.g. "somedir/prefix*.ext"
   * @return list of string paths
   * @throws IOException
   *           if IO failed during the listing
   */
  public abstract List<String> list(final URL url, final String mask) throws IOException;

  /**
   * Returns a list of entries under specified URL, whose names satisfy specified pattern. These are the entries paths relative to
   * the input URL.
   *
   * @param url
   *          input URL
   * @param pattern
   *          the entry name pattern as regexp
   * @return list of string paths
   * @throws IOException
   *           if IO failed during the listing
   */
  public abstract List<String> list(final URL url, final Pattern pattern) throws IOException;

  private static class FileURLLister extends URLLister
  {
	/**
	 * Prefix for file URL.
	 */
	private static final String URL_PREFIX = "file:";

	@Override
	public List<String> list(final URL url) throws IOException
	{
	  return list(url, ALL_ACCEPTOR);
	}

	@Override
	public List<String> list(final URL url, final String mask) throws IOException
	{
	  return list(url, new MaskFilenameAcceptor(mask));
	}

	@Override
	public List<String> list(final URL url, final Pattern pattern) throws IOException
	{
	  return list(url, new RegexpFilenameAcceptor(pattern));
	}

	private List<String> list(final URL url, final FilenameAcceptor filenameAcceptor) throws IOException
	{
	  try
	  {
		final File listRoot = new File(url.toURI());
		return listFilesRecursively(listRoot, null, filenameAcceptor);
	  }
	  catch (final URISyntaxException e)
	  {
		throw new IOException("Failed to convert URL to File", e);
	  }
	}

	private List<String> listFilesRecursively(final File root, final String prefix, final FilenameAcceptor filenameAcceptor)
	{
	  final List<String> list = new ArrayList<String>();

	  for (final File f : root.listFiles())
	  {
		final String path = prefix == null ? f.getName() : prefix + "/" + f.getName();
		if (f.isDirectory())
		{
		  list.addAll(listFilesRecursively(f, path, filenameAcceptor));
		}
		else
		{
		  if (filenameAcceptor.accept(path))
		  {
			list.add(path);
		  }
		}
	  }

	  return list;
	}

  }

  /**
   * URL Lister for JAR content.
   *
   * @author Arpad Roziczky
   * @since 1.5
   */
  private static class JARURLLister extends URLLister
  {

	/**
	 * RegExp for JAR Entry URL.
	 */
	private static final Pattern URL_PATTERN = Pattern.compile("jar:file:([^!]+)!/(.*)");

	@Override
	public List<String> list(final URL url, final String mask) throws IOException
	{
	  return list(url, new MaskFilenameAcceptor(mask));
	}

	@Override
	public List<String> list(final URL url, final Pattern pattern) throws IOException
	{
	  return list(url, new RegexpFilenameAcceptor(pattern));
	}

	@Override
	public List<String> list(final URL url) throws IOException
	{
	  return list(url, ALL_ACCEPTOR);
	}

	final List<String> list(final URL url, final FilenameAcceptor filenameAcceptor) throws IOException
	{
	  // Now we have a URL which points to something in a JAR
	  // JAR URL follows the convention jar:file:<jarFilePath>!<entryPath>
	  final Matcher m = URL_PATTERN.matcher(url.toString());
	  if (m.matches())
	  {
		final String jarFileName = m.group(1);
		final String pathInJar = m.group(2);
		return listFromJar(jarFileName, pathInJar, filenameAcceptor);
	  }
	  else
	  {
		throw new IllegalArgumentException("URL is not a JAR Entry URL");
	  }
	}

	/**
	 *
	 * @param jarFileName
	 *          The path where the jar file can be found
	 * @param pathInJar
	 *          The package path from where the content will be listed
	 * @param acceptor
	 *          the acceptor that determines if the path part conforms some selection criteria
	 * @return returns a content of a jar file from a specified directory
	 * @throws IOException
	 */
	private List<String> listFromJar(final String jarFileName, final String pathInJar, final FilenameAcceptor acceptor)
	    throws IOException
	{
	  final List<String> items = new ArrayList<String>();
	  final JarInputStream jarFile = new JarInputStream(new FileInputStream(jarFileName));
	  final int charsToTrim = pathInJar.length();
	  try
	  {
		for (JarEntry entry = jarFile.getNextJarEntry(); entry != null; entry = jarFile.getNextJarEntry())
		{
		  final String entryPath = entry.getName();
		  if ((entryPath.startsWith(pathInJar)) && !entry.isDirectory())
		  {
			final String pathPart = entryPath.substring(charsToTrim);
			if (acceptor.accept(pathPart))
			{
			  items.add(pathPart);
			}
		  }
		  jarFile.closeEntry();
		}
		return items;
	  }
	  finally
	  {
		jarFile.close();
	  }
	}

  }

  /**
   * Filename acceptor. Verifies if the path part conforms to particular criteria.
   *
   * @author xpdev
   * @since 1.5
   */
  private interface FilenameAcceptor
  {
	boolean accept(String path);
  }

  private static final class RegexpFilenameAcceptor implements FilenameAcceptor
  {

	private final Pattern pattern;

	private RegexpFilenameAcceptor(final Pattern pattern)
	{
	  this.pattern = pattern;
	}

	@Override
	public boolean accept(final String path)
	{
	  final Matcher m = this.pattern.matcher(path);
	  return m.matches();
	}
  }

  private static final class MaskFilenameAcceptor implements FilenameAcceptor
  {

	private final String mask;

	private MaskFilenameAcceptor(final String mask)
	{
	  this.mask = mask;
	}

	@Override
	public boolean accept(final String path)
	{
	  return FilenameUtils.wildcardMatch(path, this.mask);
	}
  }

}
