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

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yurconf.processor.AbstractProcessor;
import org.yurconf.processor.ProcessorException;

/**
 *
 * @author yura
 * @since 1.5
 */
class YurconfUriResolver implements URIResolver
{

  private static final Logger LOGGER = LoggerFactory.getLogger(YurconfUriResolver.class);

  /**
   *
   */
  private final AbstractProcessor abstractProcessor;

  /**
   * @param abstractXSLTProcessor
   */
  YurconfUriResolver(final AbstractProcessor processor)
  {
	this.abstractProcessor = processor;
  }

  URI resolve(final URI href, final URI base) throws TransformerException
  {
	final YurconfProtocol protocol = YurconfProtocol.forUri(href);
	if (protocol == null)
	{
	  final YurconfProtocol baseProtocol = YurconfProtocol.forUri(base);
	  if (baseProtocol != null)
	  {
		try
		{
		  final URI resolved = new URI(base.getSchemeSpecificPart()).resolve(href);
		  if (resolved.getScheme() == null)
		  {
			return new URI(base.getScheme(), resolved.getSchemeSpecificPart(), null);
		  }
		  else
		  {
			return resolved;
		  }
		}
		catch (final URISyntaxException e)
		{
		  LOGGER.error("Failed to resolve URI {} with base {}", new Object[] { href, base, e });
		}
	  }
	}
	return href;
  }

  @Override
  public Source resolve(final String href, final String base) throws TransformerException
  {
	LOGGER.trace("Resolving URI for href {} with base {}", href, base);
	try
	{
	  final URI hrefUri = new URI(href);
	  final URI baseUri = new URI(base);
	  final URI resolved = resolve(hrefUri, baseUri);
	  final YurconfProtocol protocol = YurconfProtocol.forUri(resolved);
	  if (protocol != null)
	  {
		return protocol.getSource(resolved, abstractProcessor);
	  }
	  else
	  {
		return null;
	  }
	}
	catch (final URISyntaxException | ProcessorException e)
	{
	  throw new TransformerException("Could not build URI while resolving source", e);
	}
  }
}
