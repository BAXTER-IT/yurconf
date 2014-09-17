/*
 * Configuration Processors
 * Copyright (C) 2012-2013  BAXTER Technologies
 *
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
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
