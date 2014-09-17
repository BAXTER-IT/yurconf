/*
 * Configuration Processors
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package org.yurconf.processor.desc;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yurconf.processor.ProcessorException;

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
		try
		{
		  descriptor.setUrl(url);
		}
		catch (final MalformedURLException e)
		{
		  LOGGER.error("Cannot set descriptor URL", e);
		  throw new ProcessorException("Unable to setup descriptor", e);
		}
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

  /**
   * Returns the prepared JAXB Context.
   * @return JAXB context
   */
  JAXBContext getJaxbContext()
  {
	return this.jaxbContext;
  }

}
