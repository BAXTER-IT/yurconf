/**
 * 
 */
package com.baxter.config.processor;

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

  /**
   * The reference to configuration descriptor.
   */
  private final Descriptor descriptor;

  /**
   * Processor supported version.
   */
  private final Version version;

  /**
   * Initializes processor with descriptor.
   * 
   * @param descriptor
   *          configuration descriptor
   */
  protected AbstractProcessor(final Descriptor descriptor)
  {
	this.descriptor = descriptor;
	this.version = Version.valueOf(descriptor.getVersion());
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
  protected Descriptor getDescriptor()
  {
	return this.descriptor;
  }

}
