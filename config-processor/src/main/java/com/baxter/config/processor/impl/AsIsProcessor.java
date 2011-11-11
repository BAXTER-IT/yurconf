/**
 * 
 */
package com.baxter.config.processor.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.baxter.config.processor.ProcessorContext;
import com.baxter.config.processor.ProcessorException;
import com.baxter.config.processor.desc.Descriptor;

/**
 * Default implementation of Configuration XSLT Processor.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
public class AsIsProcessor extends AbstractXSLTProcessor
{

  /**
   * Creates processor for specified descriptor.
   * 
   * @param descriptor
   *          processor descriptor
   */
  public AsIsProcessor(final Descriptor descriptor)
  {
	super(descriptor);
  }

  @Override
  public void process(final ProcessorContext context) throws ProcessorException
  {
	// 1. Determine the source file location
	final File productDir = getFactory().getRepository().getProductDirectory(getDescriptor().getProductId());
	final String filename = context.getConfigID().getComponentId()
	    + (context.getConfigID().getVariant() == null ? "" : ("-" + context.getConfigID().getVariant()));
	final File file = new File(productDir, filename);
	if (!file.isFile())
	{
	  logger.error("Could not find source file " + file.getAbsolutePath());
	  throw new ProcessorException("Source file not found");
	}

	// 2. Determine the content type
	// TODO CFG-26
	// context.setContentType(null, null);

	// 3. Write the source file to stream
	try
	{
	  final InputStream stream = new FileInputStream(file);
	  try
	  {
		IOUtils.copy(stream, context.getOutputStream());
	  }
	  finally
	  {
		stream.close();
	  }
	}
	catch (final IOException e)
	{
	  logger.error("Failed to process", e);
	  throw new ProcessorException(e);
	}
  }
}
