/*
 * Yurconf Processor Fundamental
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.processor.upgrade;

import org.yurconf.processor.ProcessorFactory;
import org.yurconf.processor.desc.Descriptor;

/**
 * @author xpdev
 * @since 1.5
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
