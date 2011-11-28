/**
 * 
 */
package com.baxter.config.processor.upgrade;

import java.io.File;
import java.net.URL;

import com.baxter.config.processor.desc.Descriptor;

/**
 * @author xpdev
 * @since ${developmentVersion}
 */
public interface UpgradeContext
{

  /**
   * Root directory of processor's repo.
   * @return directory
   */
  File getProcessorRepositoryRoot();

  /**
   * Returns the URL to processor's default sources directory.
   * @return the URL to META-INF/config/default
   */
  URL getSourceBase();
  
  /**
   * Returns the URL to processor's default sources directory.
   * @return the URL to META-INF/config/xsl
   */
  URL getStylesheetBase();
  
  Descriptor getDescriptor();

}
