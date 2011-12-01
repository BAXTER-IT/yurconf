/**
 * 
 */
package com.baxter.config.processor.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom Baxter Protocols for XSLT Processing.
 * 
 * @author xpdev
 * @since ${developmentVersion}
 */
enum BaxterProtocol
{

  /**
   * The protocol to access shared/predefined XSL stylesheets.
   */
  XSL
  {
	private final String prefix = "baxterxsl:";

	@Override
	Source getSource(final String href, final AbstractXSLTProcessor processor)
	{
	  final String xslPath = href.substring(prefix.length());
	  final String xslResourcePath = "/META-INF/config/xsl/" + xslPath;
	  final InputStream xslStream = getClass().getResourceAsStream(xslResourcePath);
	  final Source xslSource = new StreamSource(xslStream, href);
	  return xslSource;
	}

	@Override
	Result getResult(String href, AbstractXSLTProcessor processor) throws TransformerException
	{
	  throw new UnsupportedOperationException("Result not supported");
	}

	@Override
	boolean supports(String href)
	{
	  return href.startsWith(prefix);
	}
  },

  /**
   * The protocol to access files in repository.
   */
  REPO
  {
	private final String prefix = "baxterrepo:";

	@Override
	Source getSource(final String href, final AbstractXSLTProcessor processor) throws TransformerException
	{
	  try
	  {
		return new StreamSource(new FileInputStream(getRepositoryFile(href, processor)), href);
	  }
	  catch (final FileNotFoundException e)
	  {
		LOGGER.debug("Failed to lookup the file in repository", e);
		LOGGER.warn("Could not resolve {} to repository file", href);
		throw new TransformerException(e);
	  }
	}

	@Override
	Result getResult(final String href, final AbstractXSLTProcessor processor) throws TransformerException
	{
	  final File repoFile = getRepositoryFile(href, processor);
	  LOGGER.debug("Output result file {}", repoFile.getAbsolutePath());
	  return new StreamResult(repoFile);
	}

	private File getRepositoryFile(final String href, final AbstractXSLTProcessor processor)
	{
	  final String repoPath = href.substring(prefix.length());
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
	boolean supports(final String href)
	{
	  return href.startsWith(prefix);
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
  abstract Source getSource(String href, AbstractXSLTProcessor processor) throws TransformerException;

  abstract Result getResult(String href, AbstractXSLTProcessor processor) throws TransformerException;

  /**
   * Determines if the protocol supports specified href.
   * 
   * @param href
   *          reference to test
   * @return true if this protocol can be used to access source specified by href
   */
  abstract boolean supports(String href);

  /**
   * Determines the protocol element to be used to access the specified href.
   * 
   * @param href
   *          source reference
   * @return protocol element
   * @throws IllegalArgumentException
   *           if protocol specified by href is not supported
   */
  static BaxterProtocol protocolFor(final String href)
  {
	for (BaxterProtocol protocol : values())
	{
	  if (protocol.supports(href))
	  {
		return protocol;
	  }
	}
	throw new IllegalArgumentException("Unsupported protocol");
  }

}
