/*
 * Yurconf Processor Fundamental
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.processor;

import org.yurconf.om.ConfigID;
import org.yurconf.om.Version;

public interface ProcessorFactory
{

  /**
   * Returns the processor for specified request. Each call to this method
   * returns new instance of processor.
   *
   * @param configId
   *          configuration identifier
   * @param version
   *          configuration version
   * @return the matched processor
   * @throws ProcessorException
   *           if cannot match the processor for specified input
   */
  AbstractProcessor getProcessor(ConfigID configId, Version version) throws ProcessorException;

  ConfigurationRepository getRepository();

}
