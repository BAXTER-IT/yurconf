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

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;

import org.yurconf.processor.ProcessorContext;
import org.yurconf.processor.ProcessorException;
import org.yurconf.processor.ProcessorFactory;
import org.yurconf.processor.desc.Descriptor;

/**
 * Default implementation of Configuration XSLT Processor.
 *
 * @author ykryshchuk
 * @since 1.5
 */
public class XSLTProcessor extends AbstractXSLTProcessor
{

  /**
   * Creates processor for specified descriptor.
   *
   * @param descriptor
   *          processor descriptor
   */
  public XSLTProcessor(final Descriptor descriptor, final ProcessorFactory processorFactory)
  {
	super(descriptor, processorFactory);
  }

  @Override
  public void process(final ProcessorContext context) throws ProcessorException
  {
	final long startTime = System.currentTimeMillis();
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
		final long beforeXsltTime = System.currentTimeMillis();
		logger.trace("Prepared to XSLT in {} ms", (beforeXsltTime - startTime));
		final Source xmlSource = getXmlSource(context);
		transformer.transform(xmlSource, result);
		final long afterXsltTime = System.currentTimeMillis();
		logger.trace("XSLT completed within {} ms", (afterXsltTime - beforeXsltTime));
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
