/*
 * Yurconf Processor Fundamental
 * This software is distributed as is.
 *
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 *
 * Join our team to help make it better.
 */
package org.yurconf.processor.desc;

import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processor descriptor loader.
 *
 * @author ykryshchuk
 * @since 1.5
 */
@XmlTransient
public final class Loader
{

  /**
   * Logger instance.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(Loader.class);

  /**
   * Singleton Loader instance.
   */
  private static final Loader SINGLETON = new Loader();

  /**
   * JAXB Context.
   */
  private final JAXBContext jaxbContext;

  /**
   * Hidden constructor.
   */
  private Loader()
  {
	try
	{
	  this.jaxbContext = JAXBContext.newInstance(Descriptor.class.getPackage().getName());
	}
	catch (final JAXBException e)
	{
	  LOGGER.error("Failed to create JAXBContext", e);
	  throw new ExceptionInInitializerError(e);
	}
  }

  /**
   * Returns Loader instance.
   *
   * @return loader
   */
  public static Loader getInstance()
  {
	return SINGLETON;
  }

  /**
   * Loads the processor descriptor from a given URL.
   *
   * @param url
   *          descriptor URL
   * @return loaded descriptor
   * @throws DescriptorException
   *           if cannot load the descriptor
   */
  public Descriptor load(final URL url, final boolean setup) throws DescriptorException
  {
	try
	{
	  final Unmarshaller unmarshaller = this.jaxbContext.createUnmarshaller();
	  final Object o = unmarshaller.unmarshal(url);
	  if (Descriptor.class.isInstance(o))
	  {
		final Descriptor descriptor = Descriptor.class.cast(o);
		if (setup)
		{
		  try
		  {
			LOGGER.debug("Setting up {}", descriptor);
			descriptor.setUrl(url);
		  }
		  catch (final URISyntaxException e)
		  {
			LOGGER.error("Cannot set descriptor URL", e);
			throw new DescriptorException("Unable to setup descriptor", e);
		  }
		}
		return descriptor;
	  }
	  else
	  {
		LOGGER.warn("Descriptor unmarshalled as {} ({})", o, o == null ? null : o.getClass());
		throw new DescriptorException("Unexpected descriptor object " + o);
	  }
	}
	catch (final JAXBException e)
	{
	  throw new DescriptorException(e);
	}
  }

  /**
   * Returns the prepared JAXB Context.
   * @return JAXB context
   */
  JAXBContext getJaxbContext()
  {
	return this.jaxbContext;
  }

}
