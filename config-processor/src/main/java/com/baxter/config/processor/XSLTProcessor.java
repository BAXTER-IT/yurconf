/**
 * 
 */
package com.baxter.config.processor;

import java.io.IOException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
	final Transformer transformer = getTransformer(context.getConfigID());
	// Prepare the input source
	final Source source = new DOMSource();
	// Write content type
	context.setContentType(transformer.getOutputProperty(OutputKeys.MEDIA_TYPE),
	    transformer.getOutputProperty(OutputKeys.ENCODING));
	// Do XSLT
	try
	{
	  final Result result = new StreamResult(context.getOutputStream());
	  try
	  {
		transformer.transform(source, result);
	  }
	  catch (final TransformerException e)
	  {
		logger.error("XSLT failed", e);
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
