/**
 * 
 */
package com.baxter.config.processor.impl;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;

import com.baxter.config.processor.ProcessorContext;
import com.baxter.config.processor.ProcessorException;
import com.baxter.config.processor.desc.Descriptor;

/**
 * Default implementation of Configuration XSLT Processor.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
public class XSLTProcessor extends AbstractXSLTProcessor
{

  /**
   * Creates processor for specified descriptor.
   * 
   * @param descriptor
   *          processor descriptor
   */
  public XSLTProcessor(final Descriptor descriptor)
  {
	super(descriptor);
  }

  @Override
  public void process(final ProcessorContext context) throws ProcessorException
  {
	logger.trace("Processing with {}, stylesheet {}", getDescriptor(), getStylesheet());
	final Transformer transformer = getTransformer(context.getConfigID());
	// Write content type
	context.setContentType(transformer.getOutputProperty(OutputKeys.MEDIA_TYPE),
	    transformer.getOutputProperty(OutputKeys.ENCODING));
	// Do XSLT
	try
	{
	  final Result result = new StreamResult(context.getOutputStream());
	  try
	  {
		transformer.transform(getXmlSource(context), result);
	  }
	  catch (final TransformerException e)
	  {
		logger.error("XSLT failed", e);
		throw new ProcessorException(e);
	  }
	  catch (final ParserConfigurationException e)
	  {
		logger.error("Could not create source XML", e);
		throw new ProcessorException(e);
	  }
	}
	catch (final IOException e)
	{
	  logger.error("Failed to IO", e);
	  throw new ProcessorException(e);
	}
  }

}
