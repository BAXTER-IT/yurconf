/*
 * Configuration Processors
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package com.baxter.config.processor.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baxter.config.processor.AbstractProcessor;

/**
 * Custom Baxter Protocols for XSLT Processing.
 *
 * @author xpdev
 * @since 1.5
 */
enum BaxterProtocol
{

  /**
   * The protocol to access shared/predefined XSL stylesheets.
   */
  XSL
  {
	private static final String SCHEME = "baxterxsl";

	@Override
	Source getSource(final URI uri, final AbstractProcessor processor)
	{
	  final String xslPath = uri.getSchemeSpecificPart();
	  final String xslResourcePath = "META-INF/config/xsl/" + xslPath;
	  LOGGER.trace("XSL resource path: {}", xslResourcePath);
	  final InputStream xslStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(xslResourcePath);
	  return new StreamSource(xslStream, uri.toString());
	}

	@Override
	Result getResult(final URI uri, final AbstractProcessor processor) throws TransformerException
	{
	  throw new UnsupportedOperationException("Result not supported");
	}

	@Override
	boolean supports(final URI uri)
	{
	  return SCHEME.equals(uri.getScheme());
	}
  },

  /**
   * The protocol to access files in repository.
   */
  REPO
  {
	private static final String SCHEME = "baxterrepo";

	@Override
	Source getSource(final URI uri, final AbstractProcessor processor) throws TransformerException
	{
	  try
	  {
		final File file = getRepositoryFile(uri, processor);
		LOGGER.trace("Repo file: {}", file.getAbsolutePath());
		return new StreamSource(new FileInputStream(file), uri.toString());
	  }
	  catch (final FileNotFoundException e)
	  {
		LOGGER.debug("Failed to lookup the file in repository", e);
		LOGGER.warn("Could not resolve {} to repository file", uri);
		throw new TransformerException(e);
	  }
	}

	@Override
	Result getResult(final URI uri, final AbstractProcessor processor) throws TransformerException
	{
	  final File repoFile = getRepositoryFile(uri, processor);
	  LOGGER.trace("Output result file {}", repoFile.getAbsolutePath());
	  return new StreamResult(repoFile);
	}

	private File getRepositoryFile(final URI uri, final AbstractProcessor processor)
	{
	  final String repoPath = uri.getSchemeSpecificPart();
	  if (repoPath.startsWith("/"))
	  {
		return new File(processor.getFactory().getRepository().getRoot(), repoPath.substring(1));
	  }
	  else
	  {
		return new File(processor.getFactory().getRepository().getProductDirectory(processor.getDescriptor().getProductId()),
		    repoPath);
	  }
	}

	@Override
	boolean supports(final URI uri)
	{
	  return SCHEME.equals(uri.getScheme());
	}
  },
  ;

  private static final Logger LOGGER = LoggerFactory.getLogger(BaxterProtocol.class);

  /**
   * Returns the Source from this protocol.
   *
   * @param href
   *          source hyper reference
   * @param processor
   *          the calling processor
   * @return transformation source
   * @throws TransformerException
   *           if failed to get source for any reason
   */
  abstract Source getSource(URI uri, AbstractProcessor processor) throws TransformerException;

  abstract Result getResult(URI uri, AbstractProcessor processor) throws TransformerException;

  /**
   * Determines if the protocol supports specified href.
   *
   * @param href
   *          reference to test
   * @return true if this protocol can be used to access source specified by href
   */
  abstract boolean supports(URI uri);

  /**
   * Determines the protocol element to be used to access the specified uri.
   *
   * @param uri
   *          source reference
   * @return protocol element or null if no one can handle this
   */
  static BaxterProtocol protocolFor(final URI uri)
  {
	if (uri != null) {
	  for (final BaxterProtocol protocol : values())
	  {
		if (protocol.supports(uri))
		{
		  return protocol;
		}
	  }
	}
	return null;
  }

}
