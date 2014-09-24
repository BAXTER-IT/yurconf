/*
 * Yurconf Server
 * This software is distributed as is.
 *
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 *
 * Join our team to help make it better.
 */
package org.yurconf.server;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yurconf.processor.AbstractProcessor;
import org.yurconf.processor.desc.Descriptor;

class ClassLoader extends URLClassLoader
{

  private static final Logger LOGGER = LoggerFactory.getLogger(ClassLoader.class);

  public static ClassLoader createInstance(final String location)
  {
	final ClassLoader cl = new ClassLoader(location);
	return cl;
  }

  private ClassLoader(final String location)
  {
	super(new URL[0], AbstractProcessor.class.getClassLoader());
	final Path path = FileSystems.getDefault().getPath(location);
	try
	{
	  Files.walkFileTree(path, new JarFileVisitor());
	}
	catch (final IOException e)
	{
	  LOGGER.error("Failed to walk processors path {}", location, e);
	}
  }

  private class JarFileVisitor extends SimpleFileVisitor<Path>
  {
	@Override
	public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException
	{
	  final File file = path.toFile();
	  if (file.getName().endsWith(".jar"))
	  {
		try (final JarFile jar = new JarFile(file))
		{
		  final JarEntry descriptorEntry = jar.getJarEntry(Descriptor.PROCESSOR_XML_RESOURCE);
		  if (descriptorEntry != null)
		  {
			final URL jarUrl = path.toUri().toURL();
			LOGGER.trace("Adding JAR {} to class loader", jarUrl);
			addURL(jarUrl);
		  }
		}
		catch (final IOException e)
		{
		  LOGGER.error("Cannot read JAR {}", file, e);
		}
	  }
	  return super.visitFile(path, attrs);
	}
  }

}
