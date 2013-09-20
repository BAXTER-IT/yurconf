package com.baxter.config.processor.impl;

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baxter.config.processor.AbstractProcessor;

/**
 * 
 * @author yura
 * @sinceDevelopmentVersion
 */
class BaxterURIResolver implements URIResolver
{

  private static final Logger LOGGER = LoggerFactory.getLogger(BaxterURIResolver.class);

  /**
   * 
   */
  private final AbstractProcessor abstractProcessor;

  /**
   * @param abstractXSLTProcessor
   */
  BaxterURIResolver(final AbstractProcessor processor)
  {
	this.abstractProcessor = processor;
  }

  URI resolve(final URI href, final URI base) throws TransformerException
  {
	final BaxterProtocol protocol = BaxterProtocol.protocolFor(href);
	if (protocol == null)
	{
	  final BaxterProtocol baseProtocol = BaxterProtocol.protocolFor(base);
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
	  final BaxterProtocol protocol = BaxterProtocol.protocolFor(resolved);
	  if (protocol != null)
	  {
		return protocol.getSource(resolved, abstractProcessor);
	  }
	  else
	  {
		return null;
	  }
	}
	catch (final URISyntaxException e)
	{
	  throw new TransformerException("Could not build URI while resolving source", e);
	}
  }
}