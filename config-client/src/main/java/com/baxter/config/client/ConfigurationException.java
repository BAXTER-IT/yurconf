/**
 * 
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
