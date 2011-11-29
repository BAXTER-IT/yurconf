/**
 * 
 */
package com.baxter.config.processor.upgrade;

import java.io.File;

import com.baxter.config.processor.desc.Descriptor;

/**
 * @author xpdev
 * @since ${developmentVersion}
 */
public interface UpgradeContext
{

  /**
   * Root directory of processor's repo.
   * 
   * @return directory
   */
  File getProcessorRepositoryRoot();

  /**
   * Returns the processor descriptor.
   * 
   * @return the descriptor
   */
  Descriptor getDescriptor();

}
