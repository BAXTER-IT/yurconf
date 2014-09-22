/*
 * Yurconf Processor Fundamental
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.processor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
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
public abstract class UriLister
{

  private static final UriLister JAR_URILISTER = new JarUriLister();

  private static final UriLister FILE_URILISTER = new FileUriLister();

  private static final FilenameAcceptor ALL_ACCEPTOR = new FilenameAcceptor()
  {

	@Override
	public boolean accept(final String path)
	{
	  return true;
	}
  };

  /**
   * Returns the URI Lister appropriate for specified URI.
   *
   * @param uri
   *          input URI to process
   * @return some implementation of URI Lister
   */
  public static UriLister getInstance(final URI uri)
  {
	if (JarUriLister.URI_SCHEME.equals(uri.getScheme()))
	{
	  return getJarListerInstance();
	}
	if (FileUriLister.URI_SCHEME.equals(uri.getScheme()))
	{
	  return getFileListerInstance();
	}
	throw new IllegalArgumentException("Unsupported URL");
  }

  /**
   * Returns the lister for JAR URIs.
   *
   * @return jar lister
   */
  static UriLister getJarListerInstance()
  {
	return JAR_URILISTER;
  }

  /**
   * Returns the lister for File URIs.
   *
   * @return file lister
   */
  static UriLister getFileListerInstance()
  {
	return FILE_URILISTER;
  }

  /**
   * Returns a list of entries under specified URI. These are the entries paths relative to the input URI.
   *
   * @param uri
   *          input URI
   * @return list of string paths
   * @throws IOException
   *           if IO failed during the listing
   */
  public abstract List<String> list(final URI uri) throws IOException;

  /**
   * Returns a list of entries under specified URI, whose names satisfy specified pattern. These are the entries paths relative to
   * the input URI.
   *
   * @param uri
   *          input URI
   * @param mask
   *          the entry name pattern like in OS filenames, e.g. "somedir/prefix*.ext"
   * @return list of string paths
   * @throws IOException
   *           if IO failed during the listing
   */
  public abstract List<String> list(final URI uri, final String mask) throws IOException;

  /**
   * Returns a list of entries under specified URI, whose names satisfy specified pattern. These are the entries paths relative to
   * the input URI.
   *
   * @param uri
   *          input URI
   * @param pattern
   *          the entry name pattern as regexp
   * @return list of string paths
   * @throws IOException
   *           if IO failed during the listing
   */
  public abstract List<String> list(final URI uri, final Pattern pattern) throws IOException;

  private static class FileUriLister extends UriLister
  {
	/**
	 * Prefix for file URI.
	 */
	private static final String URI_SCHEME = "file";

	@Override
	public List<String> list(final URI uri) throws IOException
	{
	  return list(uri, ALL_ACCEPTOR);
	}

	@Override
	public List<String> list(final URI uri, final String mask) throws IOException
	{
	  return list(uri, new MaskFilenameAcceptor(mask));
	}

	@Override
	public List<String> list(final URI uri, final Pattern pattern) throws IOException
	{
	  return list(uri, new RegexpFilenameAcceptor(pattern));
	}

	private List<String> list(final URI uri, final FilenameAcceptor filenameAcceptor) throws IOException
	{
	  final File listRoot = new File(uri);
	  return listFilesRecursively(listRoot, null, filenameAcceptor);
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
   * URI Lister for JAR content.
   *
   * @author Arpad Roziczky
   * @since 1.5
   */
  private static class JarUriLister extends UriLister
  {

	/**
	 * RegExp for JAR Entry URI.
	 */
	private static final Pattern URI_PATTERN = Pattern.compile("jar:file:([^!]+)!/(.*)");

	private static final String URI_SCHEME = "jar:";

	@Override
	public List<String> list(final URI uri, final String mask) throws IOException
	{
	  return list(uri, new MaskFilenameAcceptor(mask));
	}

	@Override
	public List<String> list(final URI uri, final Pattern pattern) throws IOException
	{
	  return list(uri, new RegexpFilenameAcceptor(pattern));
	}

	@Override
	public List<String> list(final URI uri) throws IOException
	{
	  return list(uri, ALL_ACCEPTOR);
	}

	final List<String> list(final URI uri, final FilenameAcceptor filenameAcceptor) throws IOException
	{
	  // Now we have a URL which points to something in a JAR
	  // JAR URL follows the convention jar:file:<jarFilePath>!<entryPath>
	  final Matcher m = URI_PATTERN.matcher(uri.toString());
	  if (m.matches())
	  {
		final String jarFileName = m.group(1);
		final String pathInJar = m.group(2);
		return listFromJar(jarFileName, pathInJar, filenameAcceptor);
	  }
	  else
	  {
		throw new IllegalArgumentException("URI is not a JAR Entry URI");
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
