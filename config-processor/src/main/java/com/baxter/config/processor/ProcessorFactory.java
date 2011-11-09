/**
 * 
 */
package com.baxter.config.processor;

import com.baxter.config.om.ConfigID;
import com.baxter.config.om.Version;

/**
 * Processor Factory that returns a target processor for specified input.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
public class ProcessorFactory
{

  /**
   * Singleton factory instance.
   */
  private static final ProcessorFactory SINGLETON = new ProcessorFactory();

  /**
   * Hidden constructor.
   */
  private ProcessorFactory()
  {

  }

  /**
   * Returns the factory instance.
   * 
   * @return processor factory instance
   */
  public static ProcessorFactory getInstance()
  {
	return SINGLETON;
  }

  /**
   * Returns the processor for specified request. Each call to this method returns new instance of processor.
   * 
   * @param configId
   *          configuration identifier
   * @param version
   *          configuration version
   * @return the matched processor
   * @throws IllegalArgumentException
   *           if cannot match the processor for specified input
   */
  public AbstractProcessor getProcessor(final ConfigID configId, final Version version)
  {
	// TODO
	throw new UnsupportedOperationException("not yet implemented");
  }

}
