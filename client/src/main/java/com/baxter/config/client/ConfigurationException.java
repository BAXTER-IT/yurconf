/*
 * Baxter Configuration Client
 * Copyright (C) 2012-2013  BAXTER Technologies
 * 
 * This software is a property of BAXTER Technologies
 * and should remain that way. If you got this source
 * code from elsewhere please immediately inform Franck.
 */
package com.baxter.config.client;

/**
 * @author ykryshchuk
 *
 */
public class ConfigurationException extends Exception
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public ConfigurationException(final String message, final Throwable cause)
  {
	super(message, cause);
  }

  public ConfigurationException(final String message)
  {
	super(message);
  }

  public ConfigurationException(final Throwable cause)
  {
	super(cause);
  }

}
