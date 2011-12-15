/**
 * 
 */
package com.baxter.config.processor.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

import org.apache.commons.io.IOUtils;

import com.baxter.config.om.ConfigParameter;
import com.baxter.config.processor.ProcessorContext;
import com.baxter.config.processor.ProcessorException;
import com.baxter.config.processor.ProcessorFactory;
import com.baxter.config.processor.desc.Descriptor;

/**
 * Default implementation of Configuration XSLT Processor.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
public class AsIsProcessor extends AbstractXSLTProcessor
{

  private static final String PARAM_FILE = "file";

  /**
   * Creates processor for specified descriptor.
   * 
   * @param descriptor
   *          processor descriptor
   */
  public AsIsProcessor(final Descriptor descriptor, final ProcessorFactory processorFactory)
  {
	super(descriptor, processorFactory);
  }

  private String getParameterByName(final List<ConfigParameter> parameters, final String name)
  {
	for (ConfigParameter param : parameters)
	{
	  if (name.equals(param.getName()))
	  {
		return param.getValue();
	  }
	}
	throw new NoSuchElementException("No parameter " + name);
  }

  private File getRequestedFile(final ProcessorContext context)
  {
	final File productDir = getFactory().getRepository().getProductDirectory(getDescriptor().getProductId());
	final String filename = getParameterByName(context.getParameters(), PARAM_FILE);
	final Stack<String> variants = new Stack<String>();
	variants.addAll(context.getConfigID().getVariants());
	if (!variants.isEmpty())
	{
	  while (!variants.isEmpty())
	  {
		final String variant = variants.pop();
		final String variantFileName = filename + "-" + variant;
		final File variantFile = new File(productDir, variantFileName);
		if (variantFile.isFile())
		{
		  return variantFile;
		}
		else
		{
		  logger.warn("Requested variant ({}) not found for file {}", variant, filename);
		}
	  }
	  logger.warn("Variants do not exist for {}. Falling back to orifinal file", filename);
	}
	return new File(productDir, filename);
  }

  @Override
  public void process(final ProcessorContext context) throws ProcessorException
  {
	// 1. Determine the source file location
	final File file = getRequestedFile(context);
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
