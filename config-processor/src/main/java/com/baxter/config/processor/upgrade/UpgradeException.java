/**
 * 
 */
package com.baxter.config.processor.upgrade;

import com.baxter.config.processor.ProcessorException;

/**
 * @author xpdev
 * @since ${developmentVersion}
 */
public class UpgradeException extends ProcessorException 
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  public UpgradeException(final String message)
  {
	super(message);
  }

  public UpgradeException(final Throwable cause)
  {
	super(cause);
  }


}
