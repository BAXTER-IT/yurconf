/*
 * Configuration Processors
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
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
