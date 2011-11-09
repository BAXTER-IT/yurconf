/**
 * 
 */
package com.baxter.config.processor.desc;

import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baxter.config.processor.ProcessorException;

/**
 * Processor descriptor loader.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
public class Loader
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
	  this.jaxbContext = JAXBContext.newInstance(Descriptor.class);
	}
	catch (final JAXBException e)
	{
	  LOGGER.error("Failed to create JAXBContext", e);
	  throw new ExceptionInInitializerError("Cannot initialize Descriptor Loader");
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
   * @throws ProcessorException
   *           if cannot load the descriptor
   */
  public Descriptor load(final URL url) throws ProcessorException
  {
	try
	{
	  final Unmarshaller unmarshaller = this.jaxbContext.createUnmarshaller();
	  final Object o = unmarshaller.unmarshal(url);
	  if (Descriptor.class.isInstance(o))
	  {
		final Descriptor descriptor = Descriptor.class.cast(o);
		descriptor.setUrl(url);
		return descriptor;
	  }
	  else
	  {
		LOGGER.warn("Descriptor unmarshalled as {} ({})", o, o == null ? null : o.getClass());
		throw new ProcessorException("Unexpected descriptor object " + o);
	  }
	}
	catch (final JAXBException e)
	{
	  throw new ProcessorException(e);
	}
  }

}
