package com.baxter.config.processor;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * List content under a specified URL.
 * 
 * @author Arpad Roziczky
 * @since ${developmentVersion}
 */
abstract class URLLister
{

  private static final URLLister JAR_URLLISTER = new JARURLLister();

  /**
   * Returns the URL Lister appropriate for specified URL.
   * 
   * @param url
   *          input url to process
   * @return some implementation of URL Lister
   */
  static URLLister getInstance(final URL url)
  {
	if (JARURLLister.URL_PATTERN.matcher(url.toString()).matches())
	{
	  return getJarListerInstance();
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
   * Returns a list of entries under specified URL. These are the entries paths relative to the input URL.
   * 
   * @param url
   *          input URL
   * @return list of string paths
   * @throws IOException
   *           if IO failed during the listing
   */
  abstract List<String> list(final URL url) throws IOException;

  /**
   * URL Lister for JAR content.
   * 
   * @author Arpad Roziczky
   * @since ${developmentVersion}
   */
  private static class JARURLLister extends URLLister
  {

	/**
	 * RegExp for JAR Entry URL.
	 */
	private static final Pattern URL_PATTERN = Pattern.compile("jar:file:([^!]+)!/(.*)");

	@Override
	List<String> list(final URL url) throws IOException
	{
	  // Now we have a URL which points to something in a JAR
	  // JAR URL follows the convention jar:file:<jarFilePath>!<entryPath>
	  final Matcher m = URL_PATTERN.matcher(url.toString());
	  if (m.matches())
	  {
		final String jarFileName = m.group(1);
		final String pathInJar = m.group(2);
		return listFromJar(jarFileName, pathInJar);
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
	 * @return returns a content of a jar file from a specified directory
	 * @throws IOException
	 */
	private List<String> listFromJar(final String jarFileName, final String pathInJar) throws IOException
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
			items.add(entryPath.substring(charsToTrim));
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

}