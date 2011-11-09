/**
 * 
 */
package com.baxter.config.processor;

/**
 * Abstract Configuration Processor. The instances of processors are typically thread-safe.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
public abstract class AbstractProcessor
{

  /**
   * Performs configuration processing.
   * 
   * @param context
   *          current processor context
   * @throws ProcessorException
   *           if error occurred during the processing
   */
  public abstract void process(ProcessorContext context) throws ProcessorException;

}
