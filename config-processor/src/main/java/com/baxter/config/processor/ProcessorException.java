/**
 * 
 */
package com.baxter.config.processor;

/**
 * Base class for processor exceptions.
 * 
 * @author ykryshchuk
 * @since ${developmentVersion}
 */
public class ProcessorException extends Exception
{

  /**
   * Serialization identifier.
   */
  private static final long serialVersionUID = 1L;

  /** {@inheritDoc} */
  public ProcessorException(final String message)
  {
	super(message);
  }

  /** {@inheritDoc} */
  public ProcessorException(final Throwable cause)
  {
	super(cause);
  }

}
