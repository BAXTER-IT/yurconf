/**
 * 
 */
package com.baxter.config.processor.upgrade;

import com.baxter.config.processor.ProcessorFactory;
import com.baxter.config.processor.desc.Descriptor;

/**
 * @author xpdev
 * @since ${developmentVersion}
 */
public interface UpgradeContext
{

  /**
   * Returns the processor descriptor.
   * 
   * @return the descriptor
   */
  Descriptor getDescriptor();

  /**
   * Returns the factory appropriate for upgrade context.
   * 
   * @return factory instance
   */
  ProcessorFactory getProcessorFactory();

}
