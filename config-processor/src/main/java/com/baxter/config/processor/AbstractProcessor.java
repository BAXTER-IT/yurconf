/**
 * 
 */
package com.baxter.config.processor;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baxter.config.om.ConfigParameter;
import com.baxter.config.om.Version;
import com.baxter.config.processor.desc.Descriptor;

/**
 * Abstract Configuration Processor. The instances of processors are typically thread-safe.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
public abstract class AbstractProcessor
{

  protected final Logger logger;

  /**
   * The reference to configuration descriptor.
   */
  private final Descriptor descriptor;

  /**
   * Processor supported version.
   */
  private final Version version;

  /**
   * Owner factory that created this processor.
   */
  private final ProcessorFactory factory;

  /**
   * Initializes processor with descriptor.
   * 
   * @param descriptor
   *          configuration descriptor
   */
  protected AbstractProcessor(final Descriptor descriptor, final ProcessorFactory processorFactory)
  {
	this.logger = LoggerFactory.getLogger(getClass());
	this.descriptor = descriptor;
	this.version = Version.valueOf(descriptor.getVersion());
	this.factory = processorFactory;
  }

  /**
   * Performs configuration processing.
   * 
   * @param context
   *          current processor context
   * @throws ProcessorException
   *           if error occurred during the processing
   */
  public abstract void process(ProcessorContext context) throws ProcessorException;

  /**
   * Returns the processor version.
   * 
   * @return version
   */
  Version getVersion()
  {
	return this.version;
  }

  /**
   * Determines if the specified version is supported by this processor.
   * 
   * @param version
   *          version to check
   * @return true if version is null or smaller or equal than processor's version
   */
  boolean isVersionSupported(final Version version)
  {
	if (version == null)
	{
	  return true;
	}
	else
	{
	  return getVersion().compareTo(version) >= 0;
	}
  }

  /**
   * Returns descriptor.
   * 
   * @return descriptor
   */
  public Descriptor getDescriptor()
  {
	return this.descriptor;
  }

  public ProcessorFactory getFactory()
  {
	return this.factory;
  }

  protected static String getParameterByName(final List<ConfigParameter> parameters, final String name)
  {
	for (ConfigParameter param : parameters)
	{
	  if (name.equals(param.getName()))
	  {
		return param.getValue();
	  }
	}
	throw new NoSuchElementException("No parameter " + name);
  }

}
