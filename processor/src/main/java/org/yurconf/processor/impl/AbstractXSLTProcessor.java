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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.lib.FeatureKeys;
import net.sf.saxon.lib.OutputURIResolver;
import net.sf.saxon.lib.StandardOutputResolver;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.yurconf.om.ConfigID;
import org.yurconf.om.ConfigParameter;
import org.yurconf.processor.AbstractProcessor;
import org.yurconf.processor.ProcessorContext;
import org.yurconf.processor.ProcessorException;
import org.yurconf.processor.ProcessorFactory;
import org.yurconf.processor.desc.Descriptor;
import org.yurconf.processor.util.UriManipulator;

/**
 * Abstract implementation of XSLT Processor.
 *
 * @author ykryshchuk
 * @since 1.5
 */
public abstract class AbstractXSLTProcessor extends AbstractProcessor
{

  protected static final String XSLT_PARAM_PRODUCT_ID = "configurationProductId";

  protected static final String XSLT_PARAM_VERSION = "configurationVersion";

  protected static final String XSLT_PARAM_COMPONENT_ID = "configurationComponentId";

  protected static final String XML_NS_CONF = "http://yurconf.org";

  private static final DocumentBuilderFactory DBF = DocumentBuilderFactory.newInstance();

  private final TransformerFactory transformerFactory;

  /**
   * Path to a stylesheet.
   */
  private String stylesheet;

  /**
   * Cached Templates instance.
   */
  private Templates templates;

  private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

  private final ReadLock rLock = rwLock.readLock();

  private final WriteLock wLock = rwLock.writeLock();

  /**
   * Initializes processor.
   *
   * @param descriptor
   *          configuration processor descriptor
   */
  protected AbstractXSLTProcessor(final Descriptor descriptor, final ProcessorFactory processorFactory)
  {
	super(descriptor, processorFactory);
	transformerFactory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl", Thread.currentThread()
	    .getContextClassLoader());
	transformerFactory.setURIResolver(new YurconfUriResolver(this));
	transformerFactory.setAttribute(FeatureKeys.OUTPUT_URI_RESOLVER, new YurconfOutputURIResolver());
  }

  public String getStylesheet()
  {
	return stylesheet;
  }

  public void setStylesheet(final String stylesheet)
  {
	this.stylesheet = stylesheet;
  }

  /**
   * Returns the default transformer for this XSLT processor.
   *
   * @return transformer
   * @throws ProcessorException
   *           if cannot create transformer
   */
  protected Transformer getTransformer(final ConfigID configurationIdentifier) throws ProcessorException
  {
	try
	{
	  final Transformer transformer = getTemplates().newTransformer();
	  setupTransformer(transformer, configurationIdentifier);
	  return transformer;
	}
	catch (final TransformerConfigurationException e)
	{
	  logger.error("Could not create transformer", e);
	  throw new ProcessorException(e);
	}
  }

  /**
   * Returns cached templates object that can be used to create transformers.
   *
   * @return the templates object
   */
  protected Templates getTemplates() throws ProcessorException
  {
	rLock.lock();
	try
	{
	  if (templates == null)
	  {
		rLock.unlock();
		wLock.lock();
		try
		{
		  if (templates == null)
		  {
			try
			{
			  final Source xslSource = getXslSource();
			  logger.trace("XSL Source {}", xslSource.getSystemId());
			  templates = transformerFactory.newTemplates(xslSource);
			}
			catch (final TransformerConfigurationException e)
			{
			  logger.error("Failed to build Templates", e);
			  throw new ProcessorException(e);
			}
		  }
		}
		finally
		{
		  rLock.lock();
		  wLock.unlock();
		}
	  }
	}
	finally
	{
	  rLock.unlock();
	}
	return templates;
  }

  /**
   * Returns the root XSL source for this transformer.
   *
   * @return XSL source
   */
  protected Source getXslSource() throws ProcessorException
  {
	if (getStylesheet() == null)
	{
	  throw new IllegalStateException("Stylesheet not configured");
	}
	try
	{
	  final URI stylesheetUri = new URI(getStylesheet());
	  logger.trace("Stylesheet URI {}", stylesheetUri);
	  final YurconfProtocol p = YurconfProtocol.forUri(stylesheetUri);
	  if (YurconfProtocol.PROCESSOR_RESOURCE == p)
	  {
		return YurconfProtocol.PROCESSOR_RESOURCE.getSource(stylesheetUri, AbstractXSLTProcessor.this);
	  }
	  else
	  {
		final URI templateUri = UriManipulator.resolve(getDescriptor().getRootUri(), stylesheetUri);
		return new StreamSource(templateUri.toURL().openStream(), templateUri.toString());
	  }
	}
	catch (final IOException | URISyntaxException e)
	{
	  throw new ProcessorException("Cannot build stylesheet URI", e);
	}
  }

  protected Source getXmlSource(final ProcessorContext context) throws ParserConfigurationException, ProcessorException
  {
	final DocumentBuilder docBuilder = DBF.newDocumentBuilder();
	final Document doc = docBuilder.newDocument();
	final Element configSrc = doc.createElementNS(XML_NS_CONF, "configuration-source");
	final ConfigID configId = context.getConfigID();
	if (configId != null)
	{
	  final Element request = doc.createElementNS(XML_NS_CONF, "request");
	  request.setAttribute("productId", configId.getProductId());
	  request.setAttribute("componentId", configId.getComponentId());
	  request.setAttribute("type", configId.getType());
	  request.setAttribute("base", context.getConfigurationBaseUrl().toString());
	  // add config variants
	  for (final String cVariant : configId.getVariants())
	  {
		final Element variant = doc.createElementNS(XML_NS_CONF, "variant");
		variant.setAttribute("id", cVariant);
		request.appendChild(variant);
	  }
	  // add config parameters
	  for (final ConfigParameter cParam : context.getParameters())
	  {
		final Element param = doc.createElementNS(XML_NS_CONF, "parameter");
		param.setAttribute("id", cParam.getName());
		param.setTextContent(cParam.getValue());
		request.appendChild(param);
	  }
	  configSrc.appendChild(request);
	}
	doc.appendChild(configSrc);
	return new DOMSource(doc);
  }

  /**
   * Performs initialization of transformer.
   *
   * @param transformer
   *          transformer to setup
   * @param configurationId
   *          actual requestetd configuration, may be null
   */
  protected void setupTransformer(final Transformer transformer, final ConfigID configurationId)
  {
	transformer.setParameter(XSLT_PARAM_PRODUCT_ID, getDescriptor().getProductId());
	transformer.setParameter(XSLT_PARAM_VERSION, getDescriptor().getVersion());
	if (configurationId != null)
	{
	  transformer.setParameter(XSLT_PARAM_COMPONENT_ID, configurationId.getComponentId());
	}
	transformer.setErrorListener(JustLogErrorListener.getInstance());
	transformer.setURIResolver(transformerFactory.getURIResolver());
  }

  private class YurconfOutputURIResolver implements OutputURIResolver
  {

	private final OutputURIResolver standardResolver = StandardOutputResolver.getInstance();

	@Override
	public OutputURIResolver newInstance()
	{
	  logger.trace("Creating new Output URI Resolver");
	  return this;
	}

	@Override
	public void close(final Result result) throws TransformerException
	{
	  standardResolver.close(result);
	}

	@Override
	public Result resolve(final String href, final String base) throws TransformerException
	{
	  try
	  {
		final URI uri = new URI(href);
		final YurconfProtocol p = YurconfProtocol.forUri(uri);
		if (YurconfProtocol.REPOSITORY_SOURCE == p)
		{
		  return YurconfProtocol.REPOSITORY_SOURCE.getResult(uri, AbstractXSLTProcessor.this);
		}
		else
		{
		  return standardResolver.resolve(href, base);
		}
	  }
	  catch (final URISyntaxException | ProcessorException e)
	  {
		throw new TransformerException("Could not build output URI from " + href, e);
	  }
	}

  }

}
