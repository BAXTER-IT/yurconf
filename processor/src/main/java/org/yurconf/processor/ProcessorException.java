/*
 * Yurconf Processor Fundamental
 * This software is distributed as is.
 * 
 * We do not care about any damages that could be caused
 * by this software directly or indirectly.
 * 
 * Join our team to help make it better.
 */
package org.yurconf.processor;

/**
 * Base class for processor exceptions.
 *
 * @author ykryshchuk
 * @since 1.5
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

  /** {@inheritDoc} */
  public ProcessorException(final String message, final Throwable cause)
  {
	super(message, cause);
  }

}
