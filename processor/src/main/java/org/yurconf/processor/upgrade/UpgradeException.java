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

import org.yurconf.processor.ProcessorException;

/**
 * @author xpdev
 * @since 1.5
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
