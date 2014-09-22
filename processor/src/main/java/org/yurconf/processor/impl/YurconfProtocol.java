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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yurconf.processor.AbstractProcessor;
import org.yurconf.processor.ProcessorException;
import org.yurconf.processor.desc.Descriptor;

/**
 * Custom Yurconf Protocols for XSLT Processing.
 *
 * @author xpdev
 * @since 1.5
 */
enum YurconfProtocol
{

  /**
   * The protocol to access shared/predefined XSL stylesheets.
   */
  PROCESSOR_RESOURCE
  {

	@Override
	Source getSource(final URI uri, final AbstractProcessor processor) throws ProcessorException
	{
	  final String productId = uri.getHost();
	  final String path = uri.getPath();
	  final Descriptor descriptor = processor.getFactory().getRepository().getDescriptor(productId);
	  final URI resolvedUri = descriptor.getRootUri().resolve(path.substring(1));
	  LOGGER.trace("URI {} resolved to {}", uri, resolvedUri);
	  try
	  {
		return new StreamSource(resolvedUri.toURL().openStream(), uri.toString());
	  }
	  catch (final IOException e)
	  {
		throw new ProcessorException(e);
	  }
	}

	@Override
	Result getResult(final URI uri, final AbstractProcessor processor) throws ProcessorException
	{
	  throw new UnsupportedOperationException("Result not supported");
	}

  },

  /**
   * The protocol to access files in repository.
   */
  REPOSITORY_SOURCE
  {

	@Override
	Source getSource(final URI uri, final AbstractProcessor processor) throws ProcessorException
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
		throw new ProcessorException(e);
	  }
	}

	@Override
	Result getResult(final URI uri, final AbstractProcessor processor) throws ProcessorException
	{
	  final File repoFile = getRepositoryFile(uri, processor);
	  LOGGER.trace("Output result file {}", repoFile.getAbsolutePath());
	  return new StreamResult(repoFile);
	}

	private File getRepositoryFile(final URI uri, final AbstractProcessor processor)
	{
	  if (uri.isOpaque())
	  {
		final String path = uri.getSchemeSpecificPart();
		if (path.startsWith("/"))
		{
		  final File repoRoot = processor.getFactory().getRepository().getRoot();
		  return new File(repoRoot, path.substring(1));
		}
		else
		{
		  final File productRepo = processor.getFactory().getRepository()
			  .getProductDirectory(processor.getDescriptor().getProductId());
		  return new File(productRepo, path);
		}
	  }
	  else
	  {
		final String productId = uri.getHost() == null ? processor.getDescriptor().getProductId() : uri.getHost();
		return new File(processor.getFactory().getRepository().getProductDirectory(productId), uri.getPath().substring(1));
	  }
	}

  },
  ;

  private static final String SCHEME = "yurconf";

  private static final String AT = "@";

  private static final Logger LOGGER = LoggerFactory.getLogger(YurconfProtocol.class);

  /**
   * Returns the Source from this protocol.
   *
   * @param href
   *            source hyper reference
   * @param processor
   *            the calling processor
   * @return transformation source
   * @throws TransformerException
   *             if failed to get source for any reason
   */
  abstract Source getSource(URI uri, AbstractProcessor processor) throws ProcessorException;

  abstract Result getResult(URI uri, AbstractProcessor processor) throws ProcessorException;

  /**
   * Determines the protocol element to be used to access the specified uri.
   *
   * @param uri
   *            source reference
   * @return protocol element or null if no one can handle this
   */
  static YurconfProtocol forUri(final URI uri)
  {
	if (uri.isAbsolute() && SCHEME.equals(uri.getScheme()))
	{
	  if (uri.isOpaque())
	  {
		return REPOSITORY_SOURCE;
	  }
	  else
	  {
		final String authority = uri.getAuthority();
		if (authority == null)
		{
		  return REPOSITORY_SOURCE;
		}
		else
		{
		  if (authority.startsWith(PROCESSOR_RESOURCE.AT))
		  {
			return PROCESSOR_RESOURCE;
		  }
		  else
		  {
			return REPOSITORY_SOURCE;
		  }
		}
	  }
	}
	return null;
  }

}
