/**
 * 
 */
package com.baxter.config.processor;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import com.baxter.config.om.ConfigID;
import com.baxter.config.processor.desc.Descriptor;

/**
 * Abstract implementation of XSLT Processor.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
public abstract class AbstractXSLTProcessor extends AbstractProcessor
{

  protected static final String PARAM_XSL = "xsl";

  protected static final String XSLT_PARAM_PRODUCT_ID = "configurationProductId";

  protected static final String XSLT_PARAM_VERSION = "configurationVersion";

  protected static final String XSLT_PARAM_COMPONENT_ID = "configurationComponentId";

  protected static final String XSLT_PARAM_VARIANT = "configurationVariant";

  private final TransformerFactory transformerFactory = TransformerFactory.newInstance();

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
  protected AbstractXSLTProcessor(final Descriptor descriptor)
  {
	super(descriptor);
	// TODO setup transformer factory
  }

  @Override
  protected void setParameter(final String name, final String value)
  {
	if (PARAM_XSL.equals(name))
	{
	  this.stylesheet = value;
	}
	else
	{
	  super.setParameter(name, value);
	}
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
	  if (this.templates == null)
	  {
		rLock.unlock();
		wLock.lock();
		try
		{
		  if (this.templates == null)
		  {
			try
			{
			  this.templates = transformerFactory.newTemplates(getXslSource());
			}
			catch (final TransformerConfigurationException e)
			{
			  logger.error("Failed to build Templates", e);
			  throw new ProcessorException(e);
			}
			catch (final IOException e)
			{
			  logger.error("Failed to read XSL source", e);
			  throw new ProcessorException(e);
			}
		  }
		  rLock.lock();
		}
		finally
		{
		  wLock.unlock();
		}
	  }
	}
	finally
	{
	  rLock.unlock();
	}
	return this.templates;
  }

  /**
   * Returns the root XSL source for this transformer.
   * 
   * @return XSL source
   */
  protected Source getXslSource() throws IOException
  {
	if (this.stylesheet == null)
	{
	  throw new IllegalStateException("Stylesheet not configured");
	}
	final URL stylesheetUrl = new URL(getDescriptor().getXslUrl(), this.stylesheet);
	return new StreamSource(stylesheetUrl.openStream(), stylesheetUrl.toString());
  }

  /**
   * Performs initialization of transformer.
   * 
   * @param transformer
   *          transformer to setup
   * @param configurationId
   *          actual requestetd configuration
   */
  protected void setupTransformer(final Transformer transformer, final ConfigID configurationId)
  {
	transformer.setParameter(XSLT_PARAM_PRODUCT_ID, getDescriptor().getProductId());
	transformer.setParameter(XSLT_PARAM_VERSION, getDescriptor().getVersion());
	transformer.setParameter(XSLT_PARAM_COMPONENT_ID, configurationId.getComponentId());
	if (configurationId.getVariant() != null)
	{
	  transformer.setParameter(XSLT_PARAM_VARIANT, configurationId.getVariant());
	}
	transformer.setErrorListener(new ErrorListener()
	{

	  @Override
	  public void warning(final TransformerException exception) throws TransformerException
	  {
		logger.warn("XSLT Warning", exception);
	  }

	  @Override
	  public void fatalError(final TransformerException exception) throws TransformerException
	  {
		logger.warn("XSLT Fatal", exception);
	  }

	  @Override
	  public void error(final TransformerException exception) throws TransformerException
	  {
		logger.warn("XSLT Error", exception);
	  }
	});
	// TODO URI Resolver?
  }

}
