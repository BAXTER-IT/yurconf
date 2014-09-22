/*
 * Yurconf Processor Fundamental
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.processor.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.yurconf.processor.AbstractProcessor;
import org.yurconf.processor.ProcessorContext;
import org.yurconf.processor.ProcessorException;
import org.yurconf.processor.ProcessorFactory;
import org.yurconf.processor.desc.Descriptor;

/**
 * Default implementation of Directory Configuration Processor. Its purpose is to build the (zip) archive with files gathered from
 * specified directory and its variants.
 *
 * TODO need unit tests.
 *
 * TODO add description with example to documentation
 *
 * @author ykryshchuk
 * @since 1.5
 */
public class DirectoryProcessor extends AbstractProcessor
{

  private static final String PARAM_DIR = "dir";

  /**
   * Creates processor for specified descriptor.
   *
   * @param descriptor
   *          processor descriptor
   */
  public DirectoryProcessor(final Descriptor descriptor, final ProcessorFactory processorFactory)
  {
	super(descriptor, processorFactory);
  }

  private void readVariant(final Map<String, File> files, final String dirName, final String variant)
  {
	final File productDir = getFactory().getRepository().getProductDirectory(getDescriptor().getProductId());
	final File workdir = new VariantDirectory(productDir, dirName, variant);
	if (workdir.isDirectory())
	{
	  putFileEntries(files, workdir, null, variant);
	}
	else
	{
	  if (workdir.exists())
	  {
		logger.warn("Not a directory {}", workdir.getAbsolutePath());
	  }
	}
  }

  private void putFileEntries(final Map<String, File> files, final File dir, final String prefix, final String variant)
  {
	final String extendedPrefix = (prefix == null) ? "" : (prefix + "/");
	for (File file : dir.listFiles())
	{
	  final String entryName = extendedPrefix + file.getName();
	  if (file.isDirectory())
	  {
		putFileEntries(files, file, entryName, variant);
	  }
	  else
	  {
		if (files.containsKey(entryName))
		{
		  logger.debug("Overwriting directory file {} by variant {}", entryName, variant);
		}
		files.put(entryName, file);
	  }
	}
  }

  @Override
  public void process(final ProcessorContext context) throws ProcessorException
  {
	// 1. Set the content type
	context.setContentType("application/zip", "UTF-8");

	// 2. Determine the source file location
	final Map<String, File> files = new HashMap<String, File>();
	final String directory = getParameterByName(context.getParameters(), PARAM_DIR);
	readVariant(files, directory, null);
	for (String variant : context.getConfigID().getVariants())
	{
	  readVariant(files, directory, variant);
	}

	// 3. Write compressed files to result
	try
	{
	  final ZipOutputStream zipStream = new ZipOutputStream(context.getOutputStream());
	  try
	  {
		for (Entry<String, File> fileEntry : files.entrySet())
		{
		  final ZipEntry zipEntry = new ZipEntry(fileEntry.getKey());
		  zipStream.putNextEntry(zipEntry);
		  try
		  {
			final InputStream stream = new FileInputStream(fileEntry.getValue());
			try
			{
			  IOUtils.copy(stream, zipStream);
			}
			finally
			{
			  stream.close();
			}
		  }
		  finally
		  {
			zipStream.closeEntry();
		  }
		}
	  }
	  finally
	  {
		zipStream.finish();
	  }
	}
	catch (final IOException e)
	{
	  logger.error("Failed to process", e);
	  throw new ProcessorException(e);
	}

  }
}
