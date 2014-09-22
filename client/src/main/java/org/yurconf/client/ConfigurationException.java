/*
 * Yurconf Client
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.client;

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
