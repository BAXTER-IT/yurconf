package com.baxter.config.processor;

import com.baxter.config.om.ConfigID;
import com.baxter.config.om.Version;

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